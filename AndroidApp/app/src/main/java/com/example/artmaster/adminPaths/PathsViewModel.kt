package com.example.artmaster.adminPaths

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


/**
 * ViewModel for the details path screen.
 * Manages the state and actions related to adding, editing, and updating paths.
 *
 * @param repository The repository for interacting with Firebase Firestore.
 */
class PathsViewModel(
    private val repository: PathsRepository = PathsRepository()
) : ViewModel() {

    var pathUiState by mutableStateOf(PathUiState())
        private set

    // Check if a user is logged in
    private val hasUser: Boolean
        get() = repository.hasUser()


    // Load the user's paths
    fun loadPaths() {
        Log.d("PathsViewModel", "Loading paths...")
        if (hasUser) {
            viewModelScope.launch {
                try {
                    Log.d("PathViewModel", "Loading paths")
                    repository.getAllPaths().collect { resource ->
                        when (resource) {
                            is PathResources.Success -> {
                                pathUiState = pathUiState.copy(pathList = resource)
                                Log.d("PathViewModel", "Paths loaded successfully: ${resource.data}")
                            }
                            is PathResources.Error -> {
                                pathUiState = pathUiState.copy(pathList = resource)
                                Log.e("PathViewModel", "Error loading paths: ${resource.throwable}")
                            }
                            is PathResources.Loading -> {
                                Log.d("PathViewModel", "Loading paths...")
                            }
                        }
                    }
                } catch (e: Exception) {
                    // Handle exceptions
                    pathUiState = pathUiState.copy(pathList = PathResources.Error(throwable = e))
                }
            }
        } else {
            // Handle the case where the user is not logged in
            pathUiState = pathUiState.copy(pathList = PathResources.Error(
                throwable = Throwable(message = "El usuario no ha iniciado sesion")
            ))
        }
    }





    // Delete a note and update the UI state
    fun deletePath(pathID: String) = repository.deletePath(pathID) {
        pathUiState = pathUiState.copy(pathDeletedStatus = it)
    }



}

// Data class representing the state of the UI in the note list screen
data class PathUiState(
    val pathList: PathResources<List<Paths>> = PathResources.Loading(),
    val pathDeletedStatus: Boolean = false
)
