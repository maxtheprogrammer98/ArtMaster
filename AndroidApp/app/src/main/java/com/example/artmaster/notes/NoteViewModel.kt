package com.example.artmaster.notes

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * ViewModel for handling the list of notes.
 * Manages the state and actions related to loading and deleting notes.
 *
 * @param repository The repository for interacting with Firebase Firestore.
 */
class NoteViewModel(
    private val repository: StorageRepository = StorageRepository()
): ViewModel() {
    // Mutable state for the UI, using NoteUiState
    var noteUiState by mutableStateOf(NoteUiState())
    // Get the current user from Firebase Authentication
    val user = repository.user()
    // Check if a user is logged in
    val hasUser: Boolean
        get() = repository.hasUser()
    // Get the user ID
    private val userId: String
        get() = repository.getUserId()

    // Load the user's notes
    fun loadNotes() {
        if (hasUser) {
            if (userId.isNotBlank()) {
                Log.d("NoteViewModel", "Loading notes for user: $userId")
                getUserNotes(userId)
            }
        }else {
            // Handle the case where the user is not logged in
            noteUiState = noteUiState.copy(notesList = Resources.Error(
                throwable = Throwable(message = "El usuario no ha iniciado sesion")
            ))
        }
    }

    // Coroutine function to get user notes and update the UI state
    private fun getUserNotes(userId: String) = viewModelScope.launch {
        repository.getUserNotes(userId).collect {
            noteUiState = noteUiState.copy(notesList = it)
        }
    }

    // Delete a note and update the UI state
    fun deleteNote(noteId: String) = repository.deleteNote(noteId) {
        noteUiState = noteUiState.copy(noteDeletedStatus = it)
    }
}

// Data class representing the state of the UI in the note list screen
data class NoteUiState(
    val notesList: Resources<List<Notes>> = Resources.Loading(),
    val noteDeletedStatus: Boolean = false,
)