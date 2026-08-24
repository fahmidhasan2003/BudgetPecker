package com.fahmicode.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fahmicode.data.AppRepository
import com.fahmicode.data.model.Note
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Stack

sealed class SaveStatus {
    object Idle : SaveStatus()
    object Saving : SaveStatus()
    object Saved : SaveStatus()
    data class Error(val message: String) : SaveStatus()
}

class NoteEditViewModel(
    private val repository: AppRepository,
    private val noteId: Long
) : ViewModel() {

    private val _note = MutableStateFlow<Note?>(null)
    val note: StateFlow<Note?> = _note.asStateFlow()

    private val _saveStatus = MutableStateFlow<SaveStatus>(SaveStatus.Idle)
    val saveStatus: StateFlow<SaveStatus> = _saveStatus.asStateFlow()

    private val _undoStack = MutableStateFlow<List<String>>(emptyList())
    private val _redoStack = MutableStateFlow<List<String>>(emptyList())
    
    val canUndo = _undoStack.map { it.isNotEmpty() }.stateIn(viewModelScope, SharingStarted.Lazily, false)
    val canRedo = _redoStack.map { it.isNotEmpty() }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    private var lastSnapshotText: String = ""
    private var isManualChange = true

    init {
        loadNote()
        setupAutoSave()
    }

    private fun loadNote() {
        viewModelScope.launch {
            val fetchedNote = repository.getNoteById(noteId)
            fetchedNote?.let {
                _note.value = it
                lastSnapshotText = it.content
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun setupAutoSave() {
        _note
            .filterNotNull()
            .drop(1) // Skip initial load
            .debounce(800)
            .onEach { _saveStatus.value = SaveStatus.Saving }
            .map { note ->
                repository.insertNote(note)
                SaveStatus.Saved
            }
            .onEach { _saveStatus.value = it }
            .catch { e -> _saveStatus.value = SaveStatus.Error(e.message ?: "Unknown error") }
            .launchIn(viewModelScope)
    }

    fun updateTitle(newTitle: String) {
        _note.value = _note.value?.copy(title = newTitle, updatedAt = System.currentTimeMillis())
    }

    fun updateContent(newContent: String) {
        val currentNote = _note.value ?: return
        if (currentNote.content == newContent) return

        if (isManualChange) {
            handleUndoRedoSnapshots(currentNote.content, newContent)
        }

        _note.value = currentNote.copy(content = newContent, updatedAt = System.currentTimeMillis())
    }

    fun updateCategory(newCategory: String) {
        _note.value = _note.value?.copy(category = newCategory, updatedAt = System.currentTimeMillis())
    }

    fun updateColor(newColor: Long) {
        _note.value = _note.value?.copy(colorHex = newColor, updatedAt = System.currentTimeMillis())
    }

    fun updateNote(updatedNote: Note) {
        _note.value = updatedNote
    }

    fun togglePin() {
        _note.value = _note.value?.let { it.copy(isPinned = !it.isPinned, updatedAt = System.currentTimeMillis()) }
    }

    private fun handleUndoRedoSnapshots(oldText: String, newText: String) {
        // Coalescing logic: snapshot if space/enter or significant change
        val shouldSnapshot = newText.endsWith(" ") || newText.endsWith("\n") || 
                            Math.abs(newText.length - lastSnapshotText.length) > 10
        
        if (shouldSnapshot) {
            val currentUndo = _undoStack.value.toMutableList()
            if (currentUndo.lastOrNull() != oldText) {
                currentUndo.add(oldText)
                if (currentUndo.size > 50) currentUndo.removeAt(0)
                _undoStack.value = currentUndo
                _redoStack.value = emptyList() // Clear redo on new edit
                lastSnapshotText = newText
            }
        }
    }

    fun undo() {
        val currentUndo = _undoStack.value.toMutableList()
        if (currentUndo.isEmpty()) return

        val previousText = currentUndo.removeAt(currentUndo.lastIndex)
        val currentText = _note.value?.content ?: ""

        val currentRedo = _redoStack.value.toMutableList()
        currentRedo.add(currentText)
        
        isManualChange = false
        _note.value = _note.value?.copy(content = previousText, updatedAt = System.currentTimeMillis())
        _undoStack.value = currentUndo
        _redoStack.value = currentRedo
        lastSnapshotText = previousText
        isManualChange = true
    }

    fun redo() {
        val currentRedo = _redoStack.value.toMutableList()
        if (currentRedo.isEmpty()) return

        val nextText = currentRedo.removeAt(currentRedo.lastIndex)
        val currentText = _note.value?.content ?: ""

        val currentUndo = _undoStack.value.toMutableList()
        currentUndo.add(currentText)

        isManualChange = false
        _note.value = _note.value?.copy(content = nextText, updatedAt = System.currentTimeMillis())
        _undoStack.value = currentUndo
        _redoStack.value = currentRedo
        lastSnapshotText = nextText
        isManualChange = true
    }

    fun flushChanges() {
        viewModelScope.launch {
            _note.value?.let {
                repository.insertNote(it)
                _saveStatus.value = SaveStatus.Saved
            }
        }
    }

    fun deleteNote(onDeleted: () -> Unit) {
        viewModelScope.launch {
            _note.value?.let {
                repository.deleteNote(it)
                onDeleted()
            }
        }
    }

    class Factory(private val repository: AppRepository, private val noteId: Long) : ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return NoteEditViewModel(repository, noteId) as T
        }
    }
}
