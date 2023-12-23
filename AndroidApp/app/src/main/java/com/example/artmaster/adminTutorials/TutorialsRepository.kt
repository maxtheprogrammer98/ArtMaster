package com.example.artmaster.adminTutorials

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


const val PATHS_COLLECTION_REF = "tutoriales"
class TutorialsRepository {

    // Reference to the Firebase Firestore collection for tutorials
    private val tutorialRef: CollectionReference = Firebase
        .firestore.collection(PATHS_COLLECTION_REF)


    // Check if there is a logged-in user
    fun hasUser(): Boolean = Firebase.auth.currentUser != null


    // Get all tutorials as a Flow of Resources
    fun getTutorials(): Flow<TutorialResources<List<Tutorials>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = tutorialRef
                .orderBy("nombre", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val tutorials = snapshot.toObjects(Tutorials::class.java)
                        TutorialResources.Success(data = tutorials)
                    } else {
                        TutorialResources.Error(throwable = e?.cause)
                    }
                    trySend(response)
                }

        } catch (e: Exception) {
            // Handle exceptions and send an error resource
            Log.e("TutorialsRepository", "Error in getTutorials", e)
            trySend(TutorialResources.Error(e.cause))
        }

        awaitClose {
            // Remove the snapshot listener when the channel is closed
            snapshotStateListener?.remove()
        }
    }



    // Add a new tutorial to the collection
    fun addTutorial(
        calificacion: Float,
        descripcion: String,
        imagen: String,
        informacion: String,
        nombre: String,
        rutaNombre: String,
        video: String,
        onComplete: (Boolean) -> Unit
    ) {
        val documentId = tutorialRef.document().id

        if (documentId.isNotBlank()) {
            // Create a new tutorial object
            val tutorial = Tutorials(
                tutorialID = documentId,
                calificacion,
                descripcion,
                imagen,
                informacion,
                rutaNombre,
                nombre,
                video,
            )

            // Set the tutorial in the Firestore collection
            tutorialRef
                .document(documentId)
                .set(tutorial)
                .addOnCompleteListener { result ->
                    // Invoke the onComplete callback with the success status
                    onComplete.invoke(result.isSuccessful)
                }
        } else {
            // Handle the case where documentId is blank or null
            Log.d("TutorialRepository", "Error: Unable to generate document ID")
            onComplete.invoke(false)
        }
    }

    // Get a single tutorial by ID
    fun getTutorial(
        tutorialID: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (Tutorials?) -> Unit,
    ) {
        tutorialRef
            .document(tutorialID)
            .get()
            .addOnSuccessListener {
                onSuccess.invoke(it?.toObject(Tutorials::class.java))
            }
            .addOnFailureListener { result ->
                // Invoke the onError callback with the cause of the failure
                onError.invoke(result.cause)
            }
    }

    // Delete a tutorial by ID
    fun deleteTutorial(
        tutorialID: String,
        onComplete: (Boolean) -> Unit
    ) {
        tutorialRef.document(tutorialID)
            .delete()
            .addOnCompleteListener {
                // Invoke the onComplete callback with the success status
                onComplete.invoke(it.isSuccessful)
            }
    }

    // Update a tutorial by ID
    fun updateTutorial(
        tutorialID: String,
        calificacion: Float,
        descripcion: String,
        imagen: String,
        informacion: String,
        nombre: String,
        rutaNombre: String,
        video: String,
        onResult: (Boolean) -> Unit
    ) {
        if (tutorialID.isNotBlank()) {
            // Log the update operation
            Log.d("UpdateTutorial", "Updating path with ID: $tutorialID")
            val updateData = hashMapOf(
                "calificacion" to calificacion,
                "descripcion" to descripcion,
                "imagen" to imagen,
                "informacion" to informacion,
                "nombre" to nombre,
                "rutaNombre" to rutaNombre,
                "video" to video,
            )
            tutorialRef.document(tutorialID)
                .update(updateData as Map<String, Any>)
                .addOnCompleteListener {
                    // Invoke the onResult callback with the success status
                    onResult(it.isSuccessful)
                }
        } else {
            // Log an error if the noteId is blank
            Log.d("UpdateTutorial", "Error updating tutorial")
        }
    }


}


// Sealed class representing different states of a resource
sealed class TutorialResources<T>(
    val data: T? = null,
    val throwable: Throwable? = null,
) {
    class Loading<T>: TutorialResources<T>()
    class Success<T>(data: T?): TutorialResources<T>(data = data)
    class Error<T>(throwable: Throwable?):TutorialResources<T>(throwable = throwable)
}
