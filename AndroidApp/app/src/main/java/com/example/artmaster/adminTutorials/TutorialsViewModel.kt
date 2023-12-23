package com.example.artmaster.adminTutorials

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


/**
 * ViewModel for the tutorials screen.
 * Manages the state and actions related to loading and delete tutorials.
 *
 * @param repository The repository for interacting with Firebase Firestore.
 */
class TutorialsViewModel(
    private val repository: TutorialsRepository = TutorialsRepository()
): ViewModel() {
    var tutorialsUiState by mutableStateOf(TutorialUiState())
        private set

    // Check if a user is logged in
    private val hasUser: Boolean
        get() = repository.hasUser()


    // Load tutorials
    fun loadTutorials() {
        Log.d("TutorialsViewModel", "Loading tutorials...")
        if (hasUser) {
            viewModelScope.launch {
                try {
                    Log.d("TutorialViewModel", "Loading tutorials")
                    repository.getTutorials().collect { resource ->
                        when (resource) {
                            is TutorialResources.Success -> {
                                tutorialsUiState = tutorialsUiState.copy(tutorialList = resource)
                                Log.d("TutorialsViewModel", "Tutorials loaded successfully: ${resource.data}")
                            }
                            is TutorialResources.Error -> {
                                tutorialsUiState = tutorialsUiState.copy(tutorialList = resource)
                                Log.e("TutorialViewModel", "Error loading tutorials: ${resource.throwable}")
                            }
                            is TutorialResources.Loading -> {
                                Log.d("TutorialViewModel", "Loading tutorials...")
                            }
                        }
                    }
                } catch (e: Exception) {
                    // Handle exceptions
                    tutorialsUiState = tutorialsUiState.copy(tutorialList = TutorialResources.Error(throwable = e))
                }
            }
        } else {
            // Handle the case where the user is not logged in
            tutorialsUiState = tutorialsUiState.copy(tutorialList = TutorialResources.Error(
                throwable = Throwable(message = "El usuario no ha iniciado sesion")
            ))
        }
    }


    // Delete a tutorial and update the UI state
    fun deleteTutorial(tutorialID: String) = repository.deleteTutorial(tutorialID) {
        tutorialsUiState = tutorialsUiState.copy(tutorialDeletedStatus = it)
    }



}


data class TutorialUiState(
    val tutorialList: TutorialResources<List<Tutorials>> = TutorialResources.Loading(),
    val tutorialDeletedStatus: Boolean = false
)