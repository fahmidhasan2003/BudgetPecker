package com.fahmicode.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fahmicode.data.model.ChecklistItem
import com.fahmicode.data.model.Note
import com.fahmicode.data.model.Transaction
import com.fahmicode.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onExpandPopup: () -> Unit = {}
) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    var showAddNoteDialog by remember { mutableStateOf<Note?>(null) }

    val filteredNotes = remember(notes, searchQuery) {
        if (searchQuery.isBlank()) notes else {
            notes.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                it.content.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search notes...", fontSize = 14.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp)
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp)) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = null)
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddNoteDialog = Note(title = "", content = "") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { padding ->
        if (filteredNotes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.AutoMirrored.Filled.Notes, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outline)
                    Text("No notes found", color = MaterialTheme.colorScheme.outline)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(padding)
            ) {
                items(filteredNotes) { note ->
                    NoteCard(
                        note = note,
                        onClick = { showAddNoteDialog = note },
                        onDelete = { viewModel.deleteNote(note) },
                        onImportToExpense = { /* Handled in Dialog or quick action */ }
                    )
                }
            }
        }
    }

    if (showAddNoteDialog != null) {
        NoteDetailDialog(
            note = showAddNoteDialog!!,
            onDismiss = { showAddNoteDialog = null },
            onSave = { updatedNote ->
                viewModel.addNote(updatedNote)
                showAddNoteDialog = null
            },
            viewModel = viewModel
        )
    }
}

@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onImportToExpense: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(note.colorHex)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = note.title.ifBlank { "Untitled" },
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Box {
                    IconButton(onClick = { showMenu = true }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.MoreVert, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            leadingIcon = { Icon(Icons.Default.Edit, null) },
                            onClick = { showMenu = false; onClick() }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            leadingIcon = { Icon(Icons.Default.Delete, null) },
                            onClick = { showMenu = false; onDelete() }
                        )
                        if (note.amount != null) {
                            DropdownMenuItem(
                                text = { Text("Import to Expense") },
                                leadingIcon = { Icon(Icons.Default.Output, null) },
                                onClick = { showMenu = false; onImportToExpense() }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )

            if (note.checklistItems.isNotEmpty()) {
                val completedCount = note.checklistItems.count { it.isChecked }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$completedCount/${note.checklistItems.size} Completed",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (note.amount != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Payments, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF2E7D32))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "৳${note.amount} (${note.expenseCategory ?: "Other"})",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun NotesPopup(
    viewModel: MainViewModel,
    onExpand: () -> Unit,
    onDismiss: () -> Unit
) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    var showAddNoteDialog by remember { mutableStateOf<Note?>(null) }
    var showImportConfirm by remember { mutableStateOf<Note?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.6f),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Smart Notes", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                Row {
                    IconButton(onClick = onExpand) {
                        Icon(Icons.Default.Fullscreen, contentDescription = "Full Screen")
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (notes.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No notes yet", color = MaterialTheme.colorScheme.outline)
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(notes) { note ->
                        NoteListTile(
                            note = note,
                            onClick = { showAddNoteDialog = note },
                            onDelete = { viewModel.deleteNote(note) },
                            onImport = { showImportConfirm = note }
                        )
                    }
                }
            }

            Button(
                onClick = { showAddNoteDialog = Note(title = "", content = "") },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("New Note")
            }
        }
    }

    if (showAddNoteDialog != null) {
        NoteDetailDialog(
            note = showAddNoteDialog!!,
            onDismiss = { showAddNoteDialog = null },
            onSave = { updatedNote ->
                viewModel.addNote(updatedNote)
                showAddNoteDialog = null
            },
            viewModel = viewModel
        )
    }

    if (showImportConfirm != null) {
        ImportConfirmDialog(
            note = showImportConfirm!!,
            onConfirm = {
                viewModel.addTransaction(
                    Transaction(
                        amount = showImportConfirm!!.amount ?: 0.0,
                        title = showImportConfirm!!.title.ifBlank { "Note Import" },
                        note = showImportConfirm!!.content,
                        category = showImportConfirm!!.expenseCategory ?: "Others",
                        isIncome = false,
                        dateMillis = System.currentTimeMillis()
                    )
                )
                showImportConfirm = null
            },
            onDismiss = { showImportConfirm = null }
        )
    }
}

@Composable
fun NoteListTile(
    note: Note,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onImport: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(note.colorHex).copy(alpha = 0.5f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Filled.Notes, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(note.title.ifBlank { "Untitled" }, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(note.content, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = null)
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(text = { Text("Edit") }, onClick = { showMenu = false; onClick() })
                    DropdownMenuItem(text = { Text("Delete") }, onClick = { showMenu = false; onDelete() })
                    if (note.amount != null) {
                        DropdownMenuItem(text = { Text("Import to Expense") }, onClick = { showMenu = false; onImport() })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailDialog(
    note: Note,
    onDismiss: () -> Unit,
    onSave: (Note) -> Unit,
    viewModel: MainViewModel
) {
    var title by remember { mutableStateOf(note.title) }
    var content by remember { mutableStateOf(note.content) }
    var checklistItems = remember { mutableStateListOf<ChecklistItem>().apply { addAll(note.checklistItems) } }
    var amount by remember { mutableStateOf(note.amount?.toString() ?: "") }
    var selectedCategory by remember { mutableStateOf(note.expenseCategory ?: "Food") }
    var selectedColor by remember { mutableStateOf(note.colorHex) }

    val colors = listOf(
        0xFFFFFFFF, 0xFFF28B82, 0xFFFBBC04, 0xFFFFF475, 
        0xFFCCFF90, 0xFFA7FFEB, 0xFFCBF0F8, 0xFFAECBFA, 
        0xFFD7AEFB, 0xFFFDCFE8, 0xFFE6C9A8, 0xFFE8EAED
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f),
            colors = CardDefaults.cardColors(containerColor = Color(selectedColor))
        ) {
            Column(modifier = Modifier.padding(24.dp).fillMaxSize()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Edit Note", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
                    IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, contentDescription = null) }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    item {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            placeholder = { Text("Title") },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            )
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = content,
                            onValueChange = { content = it },
                            placeholder = { Text("Start typing...") },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            )
                        )
                    }

                    item {
                        Text("Checklist", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    }

                    items(checklistItems.size) { index ->
                        val item = checklistItems[index]
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = item.isChecked,
                                onCheckedChange = { 
                                    checklistItems[index] = item.copy(isChecked = it)
                                }
                            )
                            TextField(
                                value = item.text,
                                onValueChange = { checklistItems[index] = item.copy(text = it) },
                                modifier = Modifier.weight(1f),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                                    color = if (item.isChecked) Color.Gray else Color.Unspecified
                                ),
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent
                                )
                            )
                            IconButton(onClick = { checklistItems.removeAt(index) }) {
                                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(20.dp))
                            }
                        }
                    }

                    item {
                        TextButton(onClick = { checklistItems.add(ChecklistItem("")) }) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Text("Add Checklist Item")
                        }
                    }

                    item {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Text("Expense Import Info (Optional)", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    }

                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = amount,
                                onValueChange = { amount = it },
                                label = { Text("Amount") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp)
                            )

                            var expanded by remember { mutableStateOf(false) }
                            Box(modifier = Modifier.weight(1.5f)) {
                                OutlinedTextField(
                                    value = selectedCategory,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Category") },
                                    modifier = Modifier.fillMaxWidth(),
                                    trailingIcon = {
                                        IconButton(onClick = { expanded = true }) {
                                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                )
                                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                    ExpenseCategoryPresetsAll.forEach { cat ->
                                        DropdownMenuItem(
                                            text = { Text("${cat.emoji} ${cat.name}") },
                                            onClick = {
                                                selectedCategory = cat.name
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text("Background Color", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            colors.take(6).forEach { color ->
                                ColorCircle(color = color, isSelected = selectedColor == color, onClick = { selectedColor = color })
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            colors.drop(6).forEach { color ->
                                ColorCircle(color = color, isSelected = selectedColor == color, onClick = { selectedColor = color })
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        onSave(
                            note.copy(
                                title = title,
                                content = content,
                                checklistItems = checklistItems.toList(),
                                amount = amount.toDoubleOrNull(),
                                expenseCategory = if (amount.isNotBlank()) selectedCategory else null,
                                colorHex = selectedColor
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save Note")
                }
            }
        }
    }
}

@Composable
fun ColorCircle(color: Long, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(color))
            .clickable { onClick() }
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.5f),
                shape = CircleShape
            )
    )
}

@Composable
fun ImportConfirmDialog(
    note: Note,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Import") },
        text = {
            Text("Import ৳${note.amount} under ${note.expenseCategory} as an expense?")
        },
        confirmButton = {
            Button(onClick = onConfirm) { Text("Confirm") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
