package com.example.artmaster.adminUsers

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * ViewModel for the UsersActivity.
 * Manages the state and actions related to adding, editing, and updating users.
 *
 * @param repository The repository for interacting with Firebase Firestore.
 */
class UsersViewModel(
    private val repository: UsersRepository = UsersRepository()
) : ViewModel() {

    var userUiState by mutableStateOf(UserUiState())
        private set

    // Check if a user is logged in
    private val hasUser: Boolean
        get() = repository.hasUser()


    // Load the user
    fun loadUsers() {
        Log.d("UsersViewModel", "Loading users...")
        if (hasUser) {
            viewModelScope.launch {
                try {
                    Log.d("UserViewModel", "Loading users")
                    repository.getUsers().collect { resource ->
                        when (resource) {
                            is UsersResources.Success -> {
                                userUiState = userUiState.copy(userList = resource)
                                Log.d("UserViewModel", "Users loaded successfully: ${resource.data}")
                            }
                            is UsersResources.Error -> {
                                userUiState = userUiState.copy(userList = resource)
                                Log.e("UserViewModel", "Error loading users: ${resource.throwable}")
                            }
                            is UsersResources.Loading -> {
                                Log.d("UserViewModel", "Loading users...")
                            }
                        }
                    }
                } catch (e: Exception) {
                    // Handle exceptions
                    userUiState = userUiState.copy(userList = UsersResources.Error(throwable = e))
                }
            }
        } else {
            // Handle the case where the user is not logged in
            userUiState = userUiState.copy(userList = UsersResources.Error(
                throwable = Throwable(message = "El usuario no ha iniciado sesion")
            ))
        }


    }





    // Delete a user and update the UI state
    fun deleteUser(userID: String) = repository.deleteUser(userID) {
        userUiState = userUiState.copy(userDeletedStatus = it)
    }



}

// Data class representing the state of the UI in the note list screen
data class UserUiState(
    val userList: UsersResources<List<Users>> = UsersResources.Loading(),
    val userDeletedStatus: Boolean = false,
    val userNames: UsersResources<List<String>> = UsersResources.Loading()
)
