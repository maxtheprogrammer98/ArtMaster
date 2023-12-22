package com.example.artmaster.adminPaths

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * ViewModel for the details path screen.
 * Manages the state and actions related to adding, editing, and updating paths.
 *
 * @param repository The repository for interacting with Firebase Firestore.
 */
class DetailPathsViewModel(
    private val repository: PathsRepository = PathsRepository()
) : ViewModel() {

    var detailPathUiState by mutableStateOf(DetailPathUiState())
        private set


    // START HANDLE CHANGES IN THE UI

    fun onImagenChange(imagen: String) {
        detailPathUiState = detailPathUiState.copy(imagen = imagen)
    }

    fun onDificultadChange(dificultad: String) {
        detailPathUiState = detailPathUiState.copy(dificultad = dificultad)
    }

    fun onInformacionChange(informacion: String) {
        detailPathUiState = detailPathUiState.copy(informacion = informacion)
    }

    fun onNombreChange(nombre: String) {
        detailPathUiState = detailPathUiState.copy(nombre = nombre)
    }

    fun onTutorialesIDChange(tutorialesID: List<String>) {
        detailPathUiState = detailPathUiState.copy(tutorialesID = tutorialesID)
    }

    // HANDLE CHANGES IN THE UI


    // Add a new path to the Firestore collection
    fun addPath() {
        repository.addPath(
            nombre = detailPathUiState.nombre,
            informacion = detailPathUiState.informacion,
            dificultad = detailPathUiState.dificultad,
            imagen = detailPathUiState.imagen,
            tutorialesID = detailPathUiState.tutorialesID
        ) {
            // Update the UI state based on the result of the operation
            detailPathUiState = detailPathUiState.copy(pathAddedStatus = it)
        }
    }

    // Set the edit fields in the UI based on the selected path
    private fun setEditFields(path: Paths) {
        detailPathUiState = detailPathUiState.copy(
            nombre = path.nombre,
            informacion = path.informacion,
            dificultad = path.dificultad,
            imagen = path.imagen,
            tutorialesID = path.tutorialesID
        )
    }

    // Retrieve a specific path from Firestore based on its ID
    fun getPath(pathID: String) {
        repository.getPath(
            pathsID = pathID,
            onError = {},
        ) {
            // Update the UI state with the selected note
            detailPathUiState = detailPathUiState.copy(selectedPath = it)
            // Set the edit fields based on the selected note
            detailPathUiState.selectedPath?.let { it1 -> setEditFields(it1) }
        }
    }

    // Update an existing path in the Firestore collection
    fun updatePath(pathID: String) {
        repository.updatePath(
            pathID = pathID,
            dificultad = detailPathUiState.dificultad,
            imagen = detailPathUiState.imagen,
            informacion = detailPathUiState.informacion,
            nombre = detailPathUiState.nombre,
            tutorialesID = detailPathUiState.tutorialesID
        ) {
            // Update the UI state based on the result of the operation
            detailPathUiState = detailPathUiState.copy(updatePathStatus = it)
        }
    }

    // Reset the flags related to path addition and updating
    fun resetPathAddedStatus() {
        detailPathUiState = detailPathUiState.copy(
            pathAddedStatus = false,
            updatePathStatus = false
        )
    }

    // Reset the entire UI state
    fun resetState() {
        detailPathUiState = DetailPathUiState()
    }

}


// Data class representing the state of the UI in the note list screen
data class DetailPathUiState(
    val nombre: String = "",
    val informacion: String = "",
    val dificultad: String = "",
    val imagen: String = "",
    val tutorialesID: List<String> = emptyList(),
    val pathAddedStatus: Boolean = false,
    val updatePathStatus: Boolean = false,
    val selectedPath: Paths? = null,
)
