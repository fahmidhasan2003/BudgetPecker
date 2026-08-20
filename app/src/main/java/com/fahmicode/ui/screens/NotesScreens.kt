package com.fahmicode.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fahmicode.data.model.*
import com.fahmicode.ui.MainViewModel
import com.fahmicode.ui.theme.AccentColor
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    var selectedNote by remember { mutableStateOf<Note?>(null) }
    var showEditor by remember { mutableStateOf(false) }
    var isGridView by remember { mutableStateOf(true) }

    val filteredNotes = remember(notes, searchQuery) {
        if (searchQuery.isBlank()) notes else {
            notes.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                        it.content.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.noteActionTrigger.collect { action ->
            when (action) {
                FabAction.TextNote -> {
                    selectedNote = Note(title = "", content = "")
                    showEditor = true
                }
                FabAction.CalculationTable -> {
                    selectedNote = Note(title = "", content = "", containsTable = true)
                    showEditor = true
                }
                FabAction.Image -> {
                    // Image note logic
                }
                FabAction.Audio -> {
                    // Audio note logic
                }
            }
        }
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "NotePecker",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Menu */ }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    windowInsets = WindowInsets(0, 0, 0, 0)
                )
                
                // Search Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search notes...", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = AccentColor,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                    )
                    
                    IconButton(onClick = { isGridView = !isGridView }, modifier = Modifier.size(40.dp)) {
                        Icon(
                            if (isGridView) Icons.Default.ViewStream else Icons.Default.GridView,
                            contentDescription = "Toggle Layout",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    IconButton(onClick = { /* Filter */ }, modifier = Modifier.size(40.dp)) {
                        Icon(Icons.Default.SwapVert, contentDescription = "Filter", tint = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (filteredNotes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.AutoMirrored.Filled.Notes,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = Color.White.copy(alpha = 0.1f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No notes yet.",
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    }
                }
            } else {
                // Pinned section logic
                if (filteredNotes.any { it.isPinned }) {
                    Text(
                        "Pinned",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                if (isGridView) {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalItemSpacing = 12.dp,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Show Pinned first
                        items(filteredNotes.filter { it.isPinned }, key = { "pinned_${it.id}" }) { note ->
                            NoteCard(
                                note = note,
                                onClick = {
                                    selectedNote = note
                                    showEditor = true
                                },
                                onDelete = { viewModel.deleteNote(note) },
                                onTogglePin = { viewModel.addNote(note.copy(isPinned = !note.isPinned)) }
                            )
                        }
                        
                        // Others
                        items(filteredNotes.filter { !it.isPinned }, key = { it.id }) { note ->
                            NoteCard(
                                note = note,
                                onClick = {
                                    selectedNote = note
                                    showEditor = true
                                },
                                onDelete = { viewModel.deleteNote(note) },
                                onTogglePin = { viewModel.addNote(note.copy(isPinned = !note.isPinned)) }
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        // Pinned
                        items(filteredNotes.filter { it.isPinned }, key = { "pinned_${it.id}" }) { note ->
                            NoteCard(
                                note = note,
                                onClick = {
                                    selectedNote = note
                                    showEditor = true
                                },
                                onDelete = { viewModel.deleteNote(note) },
                                onTogglePin = { viewModel.addNote(note.copy(isPinned = !note.isPinned)) }
                            )
                        }
                        
                        // Others
                        items(filteredNotes.filter { !it.isPinned }, key = { it.id }) { note ->
                            NoteCard(
                                note = note,
                                onClick = {
                                    selectedNote = note
                                    showEditor = true
                                },
                                onDelete = { viewModel.deleteNote(note) },
                                onTogglePin = { viewModel.addNote(note.copy(isPinned = !note.isPinned)) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showEditor) {
        NoteEditor(
            note = selectedNote ?: Note(title = "", content = ""),
            onDismiss = { showEditor = false },
            onSave = { updatedNote ->
                viewModel.addNote(updatedNote)
                showEditor = false
            }
        )
    }
}

@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onTogglePin: () -> Unit
) {
    val isDefaultColor = note.colorHex == 0xFF121212L
    val containerColor = if (isDefaultColor) MaterialTheme.colorScheme.surface else Color(note.colorHex)
    val isDark = if (isDefaultColor) isSystemInDarkTheme() else false 
    val contentColor = if (isDark) Color.White else Color.Black
    
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        border = BorderStroke(1.dp, if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    if (note.isPinned) {
                        Icon(
                            Icons.Default.PushPin,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp).rotate(45f),
                            tint = AccentColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = note.title.ifBlank { "Untitled" },
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = contentColor
                    )
                }
                
                Box {
                    IconButton(onClick = { showMenu = true }, modifier = Modifier.size(24.dp)) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Options",
                            modifier = Modifier.size(18.dp),
                            tint = contentColor.copy(alpha = 0.6f)
                        )
                    }
                    
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(if (note.isPinned) "Unpin" else "Pin") },
                            leadingIcon = { Icon(Icons.Default.PushPin, null, modifier = Modifier.size(18.dp)) },
                            onClick = {
                                onTogglePin()
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete", color = Color.Red) },
                            leadingIcon = { Icon(Icons.Default.Delete, null, tint = Color.Red, modifier = Modifier.size(18.dp)) },
                            onClick = {
                                onDelete()
                                showMenu = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis,
                color = contentColor.copy(alpha = 0.7f)
            )

            if (note.containsTable) {
                val sum = calculateTableSum(note.tableDataJson)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.TableChart, contentDescription = null, modifier = Modifier.size(14.dp), tint = if (isDark) AccentColor else Color.DarkGray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Calculation Table • Total: ৳$sum",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isDark) AccentColor else Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(note.updatedAt)),
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = contentColor.copy(alpha = 0.4f)
            )
        }
    }
}

fun calculateTableSum(json: String?): Double {
    if (json.isNullOrEmpty()) return 0.0
    return try {
        val array = org.json.JSONArray(json)
        var total = 0.0
        for (i in 0 until array.length()) {
            total += array.getJSONObject(i).optDouble("amount", 0.0)
        }
        total
    } catch (e: Exception) {
        0.0
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditor(
    note: Note,
    onDismiss: () -> Unit,
    onSave: (Note) -> Unit
) {
    var title by remember { mutableStateOf(note.title) }
    var content by remember { mutableStateOf(note.content) }
    var selectedColor by remember { mutableStateOf(note.colorHex) }
    var containsTable by remember { mutableStateOf(note.containsTable) }
    
    val tableItems = remember { 
        mutableStateListOf<CalculationItem>().apply {
            if (!note.tableDataJson.isNullOrEmpty()) {
                try {
                    val array = org.json.JSONArray(note.tableDataJson)
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        add(CalculationItem(obj.getString("description"), obj.getDouble("amount")))
                    }
                } catch (_: Exception) {}
            }
        }
    }

    val colors = listOf(
        0xFF121212, 0xFFF8D7DA, 0xFFD4EDDA, 0xFFD1ECF1,
        0xFFFFF3CD, 0xFFE1D5E7, 0xFFE2E3E5, 0xFFBEE5EB,
        0xFFC3E6CB, 0xFFF5C6CB, 0xFFFFEBAA, 0xFFD6D8D9
    )

    val isDefaultColor = selectedColor == 0xFF121212L
    val editorBackgroundColor = if (isDefaultColor) MaterialTheme.colorScheme.background else Color(selectedColor)
    val isDarkBackground = if (isDefaultColor) isSystemInDarkTheme() else false
    val contentColor = if (isDarkBackground) Color.White else Color.Black
    val secondaryContentColor = if (isDarkBackground) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = editorBackgroundColor
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(if (note.id == 0L) "New Note" else "Edit Note") },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            val jsonArray = org.json.JSONArray()
                            tableItems.forEach { item ->
                                jsonArray.put(org.json.JSONObject().apply {
                                    put("description", item.description)
                                    put("amount", item.amount)
                                })
                            }
                            
                            onSave(note.copy(
                                title = title,
                                content = content,
                                colorHex = selectedColor,
                                containsTable = containsTable,
                                tableDataJson = if (containsTable) jsonArray.toString() else null,
                                updatedAt = System.currentTimeMillis()
                            ))
                        }) {
                            Icon(Icons.Default.Check, contentDescription = "Save", tint = AccentColor)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                        color = contentColor,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    cursorBrush = SolidColor(AccentColor),
                    decorationBox = { innerTextField ->
                        if (title.isEmpty()) Text("Title", style = MaterialTheme.typography.headlineSmall, color = contentColor.copy(alpha = 0.3f))
                        innerTextField()
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(onClick = { /* Toggle Bold */ }) { Icon(Icons.Default.FormatBold, null, tint = contentColor) }
                    IconButton(onClick = { /* Toggle Italic */ }) { Icon(Icons.Default.FormatItalic, null, tint = contentColor) }
                    IconButton(onClick = { containsTable = !containsTable }) { 
                        Icon(Icons.Default.TableChart, null, tint = if (containsTable) AccentColor else contentColor)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = contentColor.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    BasicTextField(
                        value = content,
                        onValueChange = { content = it },
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = contentColor),
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        cursorBrush = SolidColor(AccentColor),
                        decorationBox = { innerTextField ->
                            if (content.isEmpty()) Text("Write something...", style = MaterialTheme.typography.bodyLarge, color = contentColor.copy(alpha = 0.3f))
                            innerTextField()
                        }
                    )

                    if (containsTable) {
                        CalculationTableEditor(
                            items = tableItems,
                            onAddItem = { tableItems.add(CalculationItem("", 0.0)) },
                            onRemoveItem = { tableItems.removeAt(it) },
                            onUpdateItem = { index, item -> tableItems[index] = item },
                            isDark = isDarkBackground
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Background Color", style = MaterialTheme.typography.labelLarge, color = contentColor)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.height(50.dp)) {
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            colors.forEach { color ->
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(color))
                                        .border(
                                            width = if (selectedColor == color) 2.dp else 0.dp,
                                            color = AccentColor,
                                            shape = CircleShape
                                        )
                                        .clickable { selectedColor = color }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalculationTableEditor(
    items: List<CalculationItem>,
    onAddItem: () -> Unit,
    onRemoveItem: (Int) -> Unit,
    onUpdateItem: (Int, CalculationItem) -> Unit,
    isDark: Boolean = false
) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        color = if (isDark) Color.White.copy(alpha = 0.05f) else Color.Black.copy(alpha = 0.05f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Calculation Table", fontWeight = FontWeight.Bold, color = AccentColor)
                Text("Total: ৳${items.sumOf { it.amount }}", fontWeight = FontWeight.Bold, color = if (isDark) Color.White else Color.Black)
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            // Table Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("SL", modifier = Modifier.width(30.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (isDark) Color.White else Color.Black)
                Text("Description", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (isDark) Color.White else Color.Black)
                Text("Amount", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (isDark) Color.White else Color.Black)
                Spacer(modifier = Modifier.size(24.dp))
            }

            items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${index + 1}",
                        modifier = Modifier.width(30.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isDark) Color.White else Color.Black
                    )
                    TextField(
                        value = item.description,
                        onValueChange = { onUpdateItem(index, item.copy(description = it)) },
                        placeholder = { Text("Desc") },
                        modifier = Modifier.weight(2f),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            focusedTextColor = if (isDark) Color.White else Color.Black,
                            unfocusedTextColor = if (isDark) Color.White else Color.Black
                        ),
                        singleLine = true
                    )
                    TextField(
                        value = if (item.amount == 0.0) "" else item.amount.toString(),
                        onValueChange = { onUpdateItem(index, item.copy(amount = it.toDoubleOrNull() ?: 0.0)) },
                        placeholder = { Text("0.0") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            focusedTextColor = if (isDark) Color.White else Color.Black,
                            unfocusedTextColor = if (isDark) Color.White else Color.Black
                        ),
                        singleLine = true
                    )
                    IconButton(onClick = { onRemoveItem(index) }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.RemoveCircleOutline, null, tint = Color.Red.copy(alpha = 0.7f))
                    }
                }
            }

            TextButton(
                onClick = onAddItem,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(Icons.Default.Add, null, tint = AccentColor)
                Text("Add Row", color = AccentColor)
            }
        }
    }
}
