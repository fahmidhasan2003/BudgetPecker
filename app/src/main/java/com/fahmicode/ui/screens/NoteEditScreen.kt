package com.fahmicode.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fahmicode.data.AppRepository
import com.fahmicode.data.model.CalculationItem
import com.fahmicode.data.model.Note
import com.fahmicode.ui.NoteEditViewModel
import com.fahmicode.ui.SaveStatus
import com.fahmicode.ui.theme.AccentColor
import org.json.JSONArray
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditScreen(
    noteId: Long,
    repository: AppRepository,
    onBack: () -> Unit
) {
    val editViewModel: NoteEditViewModel = viewModel(
        factory = NoteEditViewModel.Factory(repository, noteId)
    )

    val note by editViewModel.note.collectAsStateWithLifecycle()
    val saveStatus by editViewModel.saveStatus.collectAsStateWithLifecycle()
    val canUndo by editViewModel.canUndo.collectAsStateWithLifecycle()
    val canRedo by editViewModel.canRedo.collectAsStateWithLifecycle()

    if (note == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = AccentColor)
        }
        return
    }

    val currentNote = note!!
    val isDefaultColor = currentNote.colorHex == 0xFF121212L
    val backgroundColor = if (isDefaultColor) MaterialTheme.colorScheme.background else Color(currentNote.colorHex)
    val contentColor = if (isDefaultColor) MaterialTheme.colorScheme.onBackground else Color.Black
    val isDark = if (isDefaultColor) isSystemInDarkTheme() else false

    val colors = listOf(
        0xFF121212, 0xFFF8D7DA, 0xFFD4EDDA, 0xFFD1ECF1,
        0xFFFFF3CD, 0xFFE1D5E7, 0xFFE2E3E5, 0xFFBEE5EB,
        0xFFC3E6CB, 0xFFF5C6CB, 0xFFFFEBAA, 0xFFD6D8D9
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(if (noteId == -1L) "New Note" else "Edit Note", color = contentColor)
                        Spacer(modifier = Modifier.width(8.dp))
                        when (saveStatus) {
                            SaveStatus.Saving -> Text("Saving...", style = MaterialTheme.typography.labelSmall, color = contentColor.copy(alpha = 0.5f))
                            SaveStatus.Saved -> Icon(Icons.Default.CloudDone, null, modifier = Modifier.size(14.dp), tint = AccentColor)
                            is SaveStatus.Error -> Text("Error", style = MaterialTheme.typography.labelSmall, color = Color.Red)
                            else -> {}
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        editViewModel.flushChanges()
                        onBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = contentColor)
                    }
                },
                actions = {
                    IconButton(onClick = { editViewModel.undo() }, enabled = canUndo) {
                        Icon(Icons.Default.Undo, contentDescription = "Undo", tint = if (canUndo) contentColor else contentColor.copy(alpha = 0.3f))
                    }
                    IconButton(onClick = { editViewModel.redo() }, enabled = canRedo) {
                        Icon(Icons.Default.Redo, contentDescription = "Redo", tint = if (canRedo) contentColor else contentColor.copy(alpha = 0.3f))
                    }
                    IconButton(onClick = { editViewModel.togglePin() }) {
                        Icon(
                            if (currentNote.isPinned) Icons.Default.PushPin else Icons.Default.PushPin,
                            contentDescription = "Pin",
                            tint = if (currentNote.isPinned) AccentColor else contentColor
                        )
                    }
                    IconButton(onClick = { editViewModel.deleteNote(onBack) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = backgroundColor,
        bottomBar = {
            SingleLineFormattingToolbar(
                backgroundColor = backgroundColor,
                contentColor = contentColor
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            BasicTextField(
                value = currentNote.title,
                onValueChange = { editViewModel.updateTitle(it) },
                textStyle = MaterialTheme.typography.headlineSmall.copy(
                    color = contentColor,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.fillMaxWidth(),
                cursorBrush = SolidColor(AccentColor),
                decorationBox = { innerTextField ->
                    if (currentNote.title.isEmpty()) Text("Title", style = MaterialTheme.typography.headlineSmall, color = contentColor.copy(alpha = 0.3f))
                    innerTextField()
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            BasicTextField(
                value = currentNote.content,
                onValueChange = { editViewModel.updateContent(it) },
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = contentColor),
                modifier = Modifier.fillMaxWidth().heightIn(min = 200.dp),
                cursorBrush = SolidColor(AccentColor),
                decorationBox = { innerTextField ->
                    if (currentNote.content.isEmpty()) Text("Write something...", style = MaterialTheme.typography.bodyLarge, color = contentColor.copy(alpha = 0.3f))
                    innerTextField()
                }
            )

            if (currentNote.containsTable) {
                CalculationTableEditor(
                    note = currentNote,
                    onUpdateNote = { editViewModel.updateNote(it) },
                    contentColor = contentColor
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CalculationTableEditor(
    note: Note,
    onUpdateNote: (Note) -> Unit,
    contentColor: Color
) {
    val tableItems = remember(note.tableDataJson) {
        val list = mutableListOf<CalculationItem>()
        if (!note.tableDataJson.isNullOrEmpty()) {
            try {
                val array = JSONArray(note.tableDataJson)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(CalculationItem(obj.getString("description"), obj.getDouble("amount")))
                }
            } catch (_: Exception) {}
        }
        list
    }

    Surface(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        color = contentColor.copy(alpha = 0.05f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, contentColor.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Calculation Table", fontWeight = FontWeight.Bold, color = AccentColor)
                Text("Total: ৳${tableItems.sumOf { it.amount }}", fontWeight = FontWeight.Bold, color = contentColor)
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            // Table Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("SL", modifier = Modifier.width(30.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = contentColor)
                Text("Description", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = contentColor)
                Text("Amount", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = contentColor)
                Spacer(modifier = Modifier.size(24.dp))
            }

            tableItems.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${index + 1}",
                        modifier = Modifier.width(30.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor
                    )
                    TextField(
                        value = item.description,
                        onValueChange = { desc ->
                            val newList = tableItems.toMutableList()
                            newList[index] = item.copy(description = desc)
                            updateNoteWithTable(note, newList, onUpdateNote)
                        },
                        placeholder = { Text("Desc", color = contentColor.copy(alpha = 0.4f)) },
                        modifier = Modifier.weight(2f),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            focusedTextColor = contentColor,
                            unfocusedTextColor = contentColor,
                            cursorColor = AccentColor
                        ),
                        singleLine = true
                    )
                    TextField(
                        value = if (item.amount == 0.0) "" else item.amount.toString(),
                        onValueChange = { amtStr ->
                            val amt = amtStr.toDoubleOrNull() ?: 0.0
                            val newList = tableItems.toMutableList()
                            newList[index] = item.copy(amount = amt)
                            updateNoteWithTable(note, newList, onUpdateNote)
                        },
                        placeholder = { Text("0.0", color = contentColor.copy(alpha = 0.4f)) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            focusedTextColor = contentColor,
                            unfocusedTextColor = contentColor,
                            cursorColor = AccentColor
                        ),
                        singleLine = true
                    )
                    IconButton(onClick = {
                        val newList = tableItems.toMutableList()
                        newList.removeAt(index)
                        updateNoteWithTable(note, newList, onUpdateNote)
                    }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.RemoveCircleOutline, null, tint = Color.Red.copy(alpha = 0.7f))
                    }
                }
            }

            TextButton(
                onClick = {
                    val newList = tableItems.toMutableList()
                    newList.add(CalculationItem("", 0.0))
                    updateNoteWithTable(note, newList, onUpdateNote)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(Icons.Default.Add, null, tint = AccentColor)
                Text("Add Row", color = AccentColor)
            }
        }
    }
}

private fun updateNoteWithTable(note: Note, items: List<CalculationItem>, onUpdate: (Note) -> Unit) {
    val jsonArray = JSONArray()
    items.forEach { item ->
        jsonArray.put(JSONObject().apply {
            put("description", item.description)
            put("amount", item.amount)
        })
    }
    onUpdate(note.copy(tableDataJson = jsonArray.toString(), updatedAt = System.currentTimeMillis()))
}
