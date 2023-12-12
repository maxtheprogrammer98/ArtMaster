package com.example.artmaster.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser

/**
 * ViewModel for the details screen.
 * Manages the state and actions related to adding, editing, and updating notes.
 *
 * @param repository The repository for interacting with Firebase Firestore.
 */
class DetailViewModel(
    private val repository: StorageRepository = StorageRepository()
): ViewModel() {
    // Mutable state for the UI, using DetailUiState
    var detailUiState by mutableStateOf(DetailUiState())
        private set
    // Check if a user is logged in
    private val hasUser: Boolean
        get() = repository.hasUser()
    // Get the current user from Firebase Authentication
    private val user: FirebaseUser?
        get() = repository.user()
    // Handle title changes in the UI
    fun onTitleChange(title: String) {
        detailUiState = detailUiState.copy(title = title)
    }
    // Handle content changes in the UI
    fun onContentChange(content: String) {
        detailUiState = detailUiState.copy(content = content)
    }

    // Add a new note to the Firestore collection
    fun addNote() {
        if (hasUser) {
            repository.addNote(
                userId = user!!.uid,
                title = detailUiState.title,
                content = detailUiState.content,
                timestamp = Timestamp.now(),
            ) {
                // Update the UI state based on the result of the operation
                detailUiState = detailUiState.copy(noteAddedStatus = it)
            }
        }
    }

    // Set the edit fields in the UI based on the selected note
    private fun setEditFields(note: Notes) {
        detailUiState = detailUiState.copy(
            title = note.title,
            content = note.content,
        )
    }

    // Retrieve a specific note from Firestore based on its ID
    fun getNote(noteId: String) {
        repository.getNote(
            noteId = noteId,
            onError = {},
        ) {
            // Update the UI state with the selected note
            detailUiState = detailUiState.copy(selectedNote = it)
            // Set the edit fields based on the selected note
            detailUiState.selectedNote?.let { it1 -> setEditFields(it1) }
        }
    }

    // Update an existing note in the Firestore collection
    fun updateNote(noteId: String) {
        repository.updateNote(
            noteId = noteId,
            title = detailUiState.title,
            content = detailUiState.content,
        ) {
            // Update the UI state based on the result of the operation
            detailUiState = detailUiState.copy(updateNoteStatus = it)
        }
    }

    // Reset the flags related to note addition and updating
    fun resetNoteAddedStatus() {
        detailUiState = detailUiState.copy(
            noteAddedStatus = false,
            updateNoteStatus = false
        )
    }

    // Reset the entire UI state
    fun resetState() {
        detailUiState = DetailUiState()
    }

}

// Data class representing the state of the UI in the detail screen
data class DetailUiState(
    val title: String = "",
    val content: String = "",
    val noteAddedStatus: Boolean = false,
    val updateNoteStatus: Boolean = false,
    val selectedNote:Notes? = null
)