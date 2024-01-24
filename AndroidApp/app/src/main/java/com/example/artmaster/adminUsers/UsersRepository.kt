package com.example.artmaster.adminUsers

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

const val PATHS_COLLECTION_REF = "usuarios"
class UsersRepository {

    // Reference to the Firebase Firestore collection for tutorials
    private val userRef: CollectionReference = Firebase
        .firestore.collection(PATHS_COLLECTION_REF)


    // Check if there is a logged-in user
    fun hasUser(): Boolean = Firebase.auth.currentUser != null

    // Get all tutorials as a Flow of Resources
    fun getUsers(): Flow<UsersResources<List<Users>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = userRef
                .orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val users = snapshot.toObjects(Users::class.java)
                        UsersResources.Success(data = users)
                    } else {
                        UsersResources.Error(throwable = e?.cause)
                    }
                    trySend(response)
                }

        } catch (e: Exception) {
            // Handle exceptions and send an error resource
            Log.e("UsersRepository", "Error in getUsers", e)
            trySend(UsersResources.Error(e.cause))
        }

        awaitClose {
            // Remove the snapshot listener when the channel is closed
            snapshotStateListener?.remove()
        }
    }


    // Add a new tutorial to the collection
    fun addUser(
        email: String,
        name: String,
        isAdmin: Boolean,
        photoUrl: String,
        completados: List<String>,
        favoritos: List<String>,
        drawingArray: List<String>,
        onComplete: (Boolean) -> Unit
    ) {
        val documentId = userRef.document().id

        if (documentId.isNotBlank()) {
            // Create a new user object
            val user = Users(
                userID = documentId,
                email,
                name,
                isAdmin,
                photoUrl,
                completados,
                favoritos,
                drawingArray
            )

            // Set the user in the Firestore collection
            userRef
                .document(documentId)
                .set(user)
                .addOnCompleteListener { result ->
                    // Invoke the onComplete callback with the success status
                    onComplete.invoke(result.isSuccessful)
                }
        } else {
            // Handle the case where documentId is blank or null
            Log.d("UsersRepository", "Error: Unable to generate document ID")
            onComplete.invoke(false)
        }
    }

    // Get a single user by ID
    fun getUser(
        userID: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (Users?) -> Unit,
    ) {
        userRef
            .document(userID)
            .get()
            .addOnSuccessListener {
                onSuccess.invoke(it?.toObject(Users::class.java))
            }
            .addOnFailureListener { result ->
                // Invoke the onError callback with the cause of the failure
                onError.invoke(result.cause)
            }
    }

    // Delete a user by ID
    fun deleteUser(
        pathID: String,
        onComplete: (Boolean) -> Unit
    ) {
        userRef.document(pathID)
            .delete()
            .addOnCompleteListener {
                // Invoke the onComplete callback with the success status
                onComplete.invoke(it.isSuccessful)
            }
    }

    // Update a user by ID
    fun updateUser(
        userID: String,
        name: String,
        photoUrl: String,
        isAdmin: Boolean,
        onResult: (Boolean) -> Unit
    ) {
        if (userID.isNotBlank()) {
            // Log the update operation
            Log.d("UpdateUser", "Updating user with ID: $userID")
            val updateData = hashMapOf(
                "name" to name,
                "photoUrl" to photoUrl,
                "isAdmin" to isAdmin,
                "userID" to userID,
            )
            userRef.document(userID)
                .update(updateData as Map<String, Any>)
                .addOnCompleteListener {
                    // Invoke the onResult callback with the success status
                    onResult(it.isSuccessful)
                }
        } else {
            // Log an error if the noteId is blank
            Log.d("UpdateUser", "Error updating user")
        }
    }




}


// Sealed class representing different states of a resource
sealed class UsersResources<T>(
    val data: T? = null,
    val throwable: Throwable? = null,
) {
    class Loading<T>: UsersResources<T>()
    class Success<T>(data: T?): UsersResources<T>(data = data)
    class Error<T>(throwable: Throwable?):UsersResources<T>(throwable = throwable)
}