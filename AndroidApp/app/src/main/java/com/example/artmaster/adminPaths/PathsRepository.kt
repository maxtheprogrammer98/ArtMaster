package com.example.artmaster.adminPaths

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val PATHS_COLLECTION_REF = "rutas"

class PathsRepository {


    // Reference to the Firebase Firestore collection for paths
    private val pathsRef: CollectionReference = Firebase
        .firestore.collection(PATHS_COLLECTION_REF)



    fun getAllPaths(): Flow<PathResources<List<Paths>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = pathsRef
                .orderBy("nombre", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, e ->
                    val response = if (e == null) {
                        if (snapshot != null) {
                            val paths = snapshot.toObjects(Paths::class.java)
                            PathResources.Success(data = paths)
                        } else {
                            PathResources.Success(data = emptyList())
                        }
                    } else {
                        PathResources.Error(throwable = e.cause)
                    }
                    trySend(response)
                }

        } catch (e: Exception) {
            // Handle exceptions and send an error resource
            trySend(PathResources.Error(e))
            e.printStackTrace()
        }

        awaitClose {
            // Remove the snapshot listener when the channel is closed
            snapshotStateListener?.remove()
        }
    }



    // Add a new path to the collection
    fun addPath(
        dificultad: String,
        imagen: String,
        informacion: String,
        nombre: String,
        tutorialesID: List<String>,
        onComplete: (Boolean) -> Unit
    ) {
        val documentId = pathsRef.document().id

        if (documentId.isNotBlank()) {
            // Create a new path object
            val path = Paths(
                pathsID = documentId,
                dificultad,
                imagen,
                informacion,
                nombre,
                tutorialesID
            )

            // Set the path in the Firestore collection
            pathsRef
                .document(documentId)
                .set(path)
                .addOnCompleteListener { result ->
                    // Invoke the onComplete callback with the success status
                    onComplete.invoke(result.isSuccessful)
                }
        } else {
            // Handle the case where documentId is blank or null
            Log.d("PathsRepository", "Error: Unable to generate document ID")
            onComplete.invoke(false)
        }
    }

    // Get a single path by ID
    fun getPath(
        pathsID: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (Paths?) -> Unit,
    ) {
        pathsRef
            .document(pathsID)
            .get()
            .addOnSuccessListener {
                onSuccess.invoke(it?.toObject(Paths::class.java))
            }
            .addOnFailureListener { result ->
                // Invoke the onError callback with the cause of the failure
                onError.invoke(result.cause)
            }
    }

    // Delete a note by ID
    fun deletePath(
        pathID: String,
        onComplete: (Boolean) -> Unit
    ) {
        pathsRef.document(pathID)
            .delete()
            .addOnCompleteListener {
                // Invoke the onComplete callback with the success status
                onComplete.invoke(it.isSuccessful)
            }
    }

    // Update a path by ID
    fun updatePath(
        pathID: String,
        dificultad: String,
        imagen: String,
        informacion: String,
        nombre: String,
        tutorialesID: List<String>,
        onResult: (Boolean) -> Unit
    ) {
        if (pathID.isNotBlank()) {
            // Log the update operation
            Log.d("UpdatePath", "Updating path with ID: $pathID")
            val updateData = hashMapOf(
                "dificultad" to dificultad,
                "imagen" to imagen,
                "informacion" to informacion,
                "nombre" to nombre,
                "tutorialesID" to tutorialesID,
            )
            pathsRef.document(pathID)
                .update(updateData)
                .addOnCompleteListener {
                    // Invoke the onResult callback with the success status
                    onResult(it.isSuccessful)
                }
        } else {
            // Log an error if the noteId is blank
            Log.d("UpdatePath", "Error updating path")
        }
    }
}

// Sealed class representing different states of a resource
sealed class PathResources<T>(
    val data: T? = null,
    val throwable: Throwable? = null,
) {
    class Loading<T>: PathResources<T>()
    class Success<T>(data: T?): PathResources<T>(data = data)
    class Error<T>(throwable: Throwable?):PathResources<T>(throwable = throwable)
}
