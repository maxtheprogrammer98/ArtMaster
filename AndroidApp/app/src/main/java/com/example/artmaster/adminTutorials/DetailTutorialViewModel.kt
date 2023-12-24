package com.example.artmaster.adminTutorials


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * ViewModel for the details tutorial screen.
 * Manages the state and actions related to adding, editing, and updating tutorials.
 *
 * @param repository The repository for interacting with Firebase Firestore.
 */
class DetailTutorialViewModel(
    private val repository: TutorialsRepository = TutorialsRepository()
) : ViewModel() {

    var detailTutorialUiState by mutableStateOf(DetailTutorialUiState())
        private set


    // START HANDLE CHANGES IN THE UI

    fun onImagenChange(imagen: String) {
        detailTutorialUiState = detailTutorialUiState.copy(imagen = imagen)
    }

    fun onCalificacionChange(calificacion: Float) {
        detailTutorialUiState = detailTutorialUiState.copy(calificacion = calificacion)
    }

    fun onDescripcionChange(descripcion: String) {
        detailTutorialUiState = detailTutorialUiState.copy(descripcion = descripcion)
    }

    fun onInformacionChange(informacion: String) {
        detailTutorialUiState = detailTutorialUiState.copy(informacion = informacion)
    }

    fun onVideoChange(video: String) {
        detailTutorialUiState = detailTutorialUiState.copy(video = video)
    }

    fun onNombreChange(nombre: String) {
        detailTutorialUiState = detailTutorialUiState.copy(nombre = nombre)
    }

    fun onRutaNombreChange(rutaNombre: String) {
        detailTutorialUiState = detailTutorialUiState.copy(rutaNombre = rutaNombre)
    }

    // HANDLE CHANGES IN THE UI


    // Add a new tutorial to the Firestore collection
    fun addTutorial() {
        repository.addTutorial(
            nombre = detailTutorialUiState.nombre,
            informacion = detailTutorialUiState.informacion,
            descripcion = detailTutorialUiState.descripcion,
            imagen = detailTutorialUiState.imagen,
            video = detailTutorialUiState.video,
            rutaNombre = detailTutorialUiState.rutaNombre,
            calificacion = detailTutorialUiState.calificacion,
        ) {
            // Update the UI state based on the result of the operation
            detailTutorialUiState = detailTutorialUiState.copy(tutorialAddedStatus = it)
        }
    }

    // Set the edit fields in the UI based on the selected tutorial
    private fun setEditFields(tutorial: Tutorials) {
        detailTutorialUiState = detailTutorialUiState.copy(
            nombre = tutorial.nombre,
            informacion = tutorial.informacion,
            calificacion = tutorial.calificacion,
            imagen = tutorial.imagen,
            video = tutorial.video,
            descripcion = tutorial.descripcion,
            rutaNombre = tutorial.rutaNombre,
        )
    }

    // Retrieve a specific tutorial from Firestore based on its ID
    fun getTutorial(tutorialID: String) {
        repository.getTutorial(
            tutorialID = tutorialID,
            onError = {},
        ) {
            // Update the UI state with the selected tutorial
            detailTutorialUiState = detailTutorialUiState.copy(selectedTutorial = it)
            // Set the edit fields based on the selected tutorial
            detailTutorialUiState.selectedTutorial?.let { it1 -> setEditFields(it1) }
        }
    }

    // Update an existing tutorial in the Firestore collection
    fun updateTutorial(tutorialID: String) {
        repository.updateTutorial(
            tutorialID = tutorialID,
            calificacion = detailTutorialUiState.calificacion,
            imagen = detailTutorialUiState.imagen,
            informacion = detailTutorialUiState.informacion,
            nombre = detailTutorialUiState.nombre,
            rutaNombre = detailTutorialUiState.rutaNombre,
            descripcion = detailTutorialUiState.descripcion,
            video = detailTutorialUiState.video,

        ) {
            // Update the UI state based on the result of the operation
            detailTutorialUiState = detailTutorialUiState.copy(updateTutorialStatus = it)
        }
    }

    // Reset the flags related to tutorial addition and updating
    fun resetTutorialAddedStatus() {
        detailTutorialUiState = detailTutorialUiState.copy(
            tutorialAddedStatus = false,
            updateTutorialStatus = false
        )
    }

    // Reset the entire UI state
    fun resetState() {
        detailTutorialUiState = DetailTutorialUiState()
    }

}


// Data class representing the state of the UI in the note list screen
data class DetailTutorialUiState(
    val nombre: String = "",
    val informacion: String = "",
    val calificacion: Float = 0.0f,
    val imagen: String = "",
    val video: String = "",
    val rutaNombre: String = "",
    val descripcion: String = "",
    val tutorialAddedStatus: Boolean = false,
    val updateTutorialStatus: Boolean = false,
    val selectedTutorial: Tutorials? = null,
)
