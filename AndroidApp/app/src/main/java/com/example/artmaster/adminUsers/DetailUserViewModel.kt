package com.example.artmaster.adminUsers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * ViewModel for the details user screen.
 * Manages the state and actions related to adding, editing, and updating users.
 *
 * @param repository The repository for interacting with Firebase Firestore.
 */
class DetailUserViewModel(
    private val repository: UsersRepository = UsersRepository()
) : ViewModel() {

    var detailUserUiState by mutableStateOf(DetailUserUiState())
        private set


    // START HANDLE CHANGES IN THE UI

    fun onEmailChange(email: String) {
        detailUserUiState = detailUserUiState.copy(email = email)
    }

    fun onNameChange(name: String) {
        detailUserUiState = detailUserUiState.copy(name = name)
    }

    fun onIsAdminChange(isAdmin: Boolean) {
        detailUserUiState = detailUserUiState.copy(isAdmin = isAdmin)
    }

    fun onPhotoUrlChange(photoUrl: String) {
        detailUserUiState = detailUserUiState.copy(photoUrl = photoUrl)
    }

    fun onCompletadosChange(completados: List<String>) {
        detailUserUiState = detailUserUiState.copy(completados = completados)
    }

    fun onFavortiosChange(completados: List<String>) {
        detailUserUiState = detailUserUiState.copy(completados = completados)
    }

    fun onDrawingArrayChange(completados: List<String>) {
        detailUserUiState = detailUserUiState.copy(completados = completados)
    }
    // HANDLE CHANGES IN THE UI


    // Add a new user to the Firestore collection
    fun addUser() {
        repository.addUser(
            email = detailUserUiState.email,
            name = detailUserUiState.name,
            isAdmin = detailUserUiState.isAdmin,
            photoUrl = detailUserUiState.photoUrl,
            completados = detailUserUiState.completados,
            favoritos = detailUserUiState.favoritos,
            drawingArray = detailUserUiState.drawingArray
        ) {
            // Update the UI state based on the result of the operation
            detailUserUiState = detailUserUiState.copy(userAddedStatus = it)
        }
    }

    // Set the edit fields in the UI based on the selected user
    private fun setEditFields(user: Users) {
        detailUserUiState = detailUserUiState.copy(
            email = user.email,
            name = user.name,
            isAdmin = user.isAdmin,
            photoUrl = user.photoUrl,
            completados = user.completados,
            favoritos = user.favoritos,
            drawingArray = user.drawingArray
        )
    }

    // Retrieve a specific user from Firestore based on its ID
    fun getUser(userID: String) {
        repository.getUser(
            userID = userID,
            onError = {},
        ) {
            // Update the UI state with the selected user
            detailUserUiState = detailUserUiState.copy(selectedUser = it)
            // Set the edit fields based on the selected user
            detailUserUiState.selectedUser?.let { it1 -> setEditFields(it1) }
        }
    }

    // Update an existing user in the Firestore collection
    fun updateUser(userID: String) {
        repository.updateUser(
            userID = userID,
            name = detailUserUiState.name,
            isAdmin = detailUserUiState.isAdmin,
            photoUrl = detailUserUiState.photoUrl,
        ) {
            // Update the UI state based on the result of the operation
            detailUserUiState = detailUserUiState.copy(updateUserStatus = it)
        }
    }

    // Reset the flags related to path addition and updating
    fun resetUserAddedStatus() {
        detailUserUiState = detailUserUiState.copy(
            userAddedStatus = false,
            updateUserStatus = false
        )
    }

    // Reset the entire UI state
    fun resetState() {
        detailUserUiState = DetailUserUiState()
    }

}


// Data class representing the state of the UI in the user list screen
data class DetailUserUiState(
    val email: String = "",
    val name: String = "",
    val isAdmin: Boolean = false,
    val photoUrl: String = "",
    val completados: List<String> = emptyList(),
    val favoritos: List<String> = emptyList(),
    val drawingArray: List<String> = emptyList(),
    val userAddedStatus: Boolean = false,
    val updateUserStatus: Boolean = false,
    val selectedUser: Users? = null,
)
