package com.example.artmaster.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class DetailViewModel(
    private val repository: StorageRepository = StorageRepository()
): ViewModel() {
    var detailUiState by mutableStateOf(DetailUiState())
        private set

    private val hasUser: Boolean
        get() = repository.hasUser()

    private val user: FirebaseUser?
        get() = repository.user()
    fun onTitleChange(title: String) {
        detailUiState = detailUiState.copy(title = title)
    }

    fun onContentChange(content: String) {
        detailUiState = detailUiState.copy(content = content)
    }

    fun addNote() {
        if (hasUser) {
            repository.addNote(
                userId = user!!.uid,
                title = detailUiState.title,
                content = detailUiState.content,
                timestamp = Timestamp.now(),
            ) {
                detailUiState = detailUiState.copy(noteAddedStatus = it)
            }
        }
    }

    fun setEditFields(note: Notes) {
        detailUiState = detailUiState.copy(
            title = note.title,
            content = note.content,
        )
    }

    fun getNote(noteId: String) {
        repository.getNote(
            noteId = noteId,
            onError = {},
        ) {
            detailUiState = detailUiState.copy(selectedNote = it)
            detailUiState.selectedNote?.let { it1 -> setEditFields(it1) }
        }
    }

    fun updateNote(noteId: String) {
        repository.updateNote(
            noteId = noteId,
            title = detailUiState.title,
            content = detailUiState.content,
        ) {
            detailUiState = detailUiState.copy(updateNoteStatus = it)
        }
    }

    fun resetNoteAddedStatus() {
        detailUiState = detailUiState.copy(
            noteAddedStatus = false,
            updateNoteStatus = false
        )
    }

    fun resetState() {
        detailUiState = DetailUiState()
    }

}

data class DetailUiState(
    val title: String = "",
    val content: String = "",
    val noteAddedStatus: Boolean = false,
    val updateNoteStatus: Boolean = false,
    val selectedNote:Notes? = null
)