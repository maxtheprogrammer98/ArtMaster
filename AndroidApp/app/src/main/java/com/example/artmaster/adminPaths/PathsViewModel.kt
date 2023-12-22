package com.example.artmaster.adminPaths

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artmaster.notes.NoteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PathsViewModel(
    private val repository: PathsRepository = PathsRepository()
) : ViewModel() {

    var pathUiState by mutableStateOf(PathUiState())

    // This flow will represent the state of your paths
    private val _pathsState = MutableStateFlow(pathUiState.pathList)
    val pathsState: StateFlow<PathResources<List<Paths>>> = _pathsState



    // START HANDLE CHANGES IN THE UI

    fun onImagenChange(imagen: String) {
        pathUiState = pathUiState.copy(imagen = imagen)
    }

    fun onDificultadChange(dificultad: String) {
        pathUiState = pathUiState.copy(dificultad = dificultad)
    }

    fun onInformacionChange(informacion: String) {
        pathUiState = pathUiState.copy(informacion = informacion)
    }

    fun onNombreChange(nombre: String) {
        pathUiState = pathUiState.copy(nombre = nombre)
    }

    fun onTutorialesIDChange(tutorialesID: List<String>) {
        pathUiState = pathUiState.copy(tutorialesID = tutorialesID)
    }

    // HANDLE CHANGES IN THE UI


    // Function to load paths
    fun loadPaths() {
        viewModelScope.launch {
            repository.getAllPaths().collect { pathsResource ->
                _pathsState.value = pathsResource
            }
        }
    }

    // Add a new note to the Firestore collection
    fun addPath() {
        repository.addPath(
            dificultad = pathUiState.dificultad,
            imagen = pathUiState.imagen,
            informacion = pathUiState.informacion,
            nombre = pathUiState.nombre,
            tutorialesID = pathUiState.tutorialesID
        ) {
            // Update the UI state based on the result of the operation
            pathUiState = pathUiState.copy(pathAddedStatus = it)
        }
    }

    // Set the edit fields in the UI based on the selected path
    private fun setEditFields(path: Paths) {
        pathUiState = pathUiState.copy(
            dificultad = path.dificultad,
            imagen = path.imagen,
            informacion = path.informacion,
            nombre = path.nombre,
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
            pathUiState = pathUiState.copy(selectedPath = it)
            // Set the edit fields based on the selected note
            pathUiState.selectedPath?.let { it1 -> setEditFields(it1) }
        }
    }

    // Update an existing path in the Firestore collection
    fun updatePath(pathID: String) {
        repository.updatePath(
            pathID = pathID,
            dificultad = pathUiState.dificultad,
            imagen = pathUiState.imagen,
            informacion = pathUiState.informacion,
            nombre = pathUiState.nombre,
            tutorialesID = pathUiState.tutorialesID
        ) {
            // Update the UI state based on the result of the operation
            pathUiState = pathUiState.copy(updatePathStatus = it)
        }
    }


    // Delete a note and update the UI state
    fun deletePath(pathID: String) = repository.deletePath(pathID) {
        pathUiState = pathUiState.copy(pathDeletedStatus = it)
    }


    // Reset the flags related to path addition and updating
    fun resetPathAddedStatus() {
        pathUiState = pathUiState.copy(
            pathAddedStatus = false,
            updatePathStatus = false
        )
    }

    // Reset the entire UI state
    fun resetState() {
        pathUiState = PathUiState()
    }

}

// Data class representing the state of the UI in the note list screen
data class PathUiState(
    val pathsID: String = "",
    val dificultad: String = "",
    val imagen: String = "",
    val informacion: String = "",
    val nombre: String = "",
    val tutorialesID: List<String> = emptyList(),
    val pathAddedStatus: Boolean = false,
    val updatePathStatus: Boolean = false,
    val selectedPath: Paths? = null,
    val pathList: PathResources<List<Paths>> = PathResources.Loading(),
    val pathDeletedStatus: Boolean = false
)
