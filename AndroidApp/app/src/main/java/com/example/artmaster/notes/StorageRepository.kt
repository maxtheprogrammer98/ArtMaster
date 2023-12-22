package com.example.artmaster.notes

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val NOTES_COLLECTION_REF = "notes"

class StorageRepository {
    // Function to get the current user
    fun user() = Firebase.auth.currentUser
    // Check if there is a logged-in user
    fun hasUser(): Boolean = Firebase.auth.currentUser != null
    // Get the user ID, or an empty string if the user is not logged in
    fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()

    // Reference to the Firebase Firestore collection for notes
    private val notesRef: CollectionReference = Firebase
        .firestore.collection(NOTES_COLLECTION_REF)

    // Get user notes as a Flow of Resources
    fun getUserNotes(
        userId: String
    ): Flow<Resources<List<Notes>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = notesRef
                .orderBy("nombre", Query.Direction.ASCENDING)
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val notes = snapshot.toObjects(Notes::class.java)
                        Resources.Success(data = notes)
                    } else {
                        Resources.Error(throwable = e?.cause)
                    }
                    trySend(response)
                }

        }catch (e: Exception) {
            // Handle exceptions and send an error resource
            trySend(Resources.Error(e.cause))
            e.printStackTrace()
        }

        awaitClose {
            // Remove the snapshot listener when the channel is closed
            snapshotStateListener?.remove()
        }

    }

    // Get a single note by ID
    fun getNote(
        noteId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (Notes?) -> Unit,
    ) {
        notesRef
            .document(noteId)
            .get()
            .addOnSuccessListener {
                onSuccess.invoke(it?.toObject(Notes::class.java))
            }
            .addOnFailureListener { result ->
                // Invoke the onError callback with the cause of the failure
                onError.invoke(result.cause)
            }
    }

    // Delete a note by ID
    fun deleteNote(noteId: String, onComplete: (Boolean) -> Unit) {
        notesRef.document(noteId)
            .delete()
            .addOnCompleteListener {
                // Invoke the onComplete callback with the success status
                onComplete.invoke(it.isSuccessful)
            }
    }

    // Update a note by ID
    fun updateNote(
        title: String,
        content: String,
        noteId: String,
        onResult: (Boolean) -> Unit
    ) {
        if (noteId.isNotBlank()) {
            // Log the update operation
            Log.d("UpdateNote", "Updating note with ID: $noteId")
            val updateData = hashMapOf<String, Any>(
                "content" to content,
                "title" to title,
            )
            notesRef.document(noteId)
                .update(updateData)
                .addOnCompleteListener {
                    // Invoke the onResult callback with the success status
                    onResult(it.isSuccessful)
                }
        } else {
            // Log an error if the noteId is blank
            Log.d("UpdateNote", "Error updating note")
        }
    }

    // Add a new note to the collection
    fun addNote(
        userId: String,
        title: String,
        content: String,
        timestamp: Timestamp,
        onComplete: (Boolean) -> Unit,
    ) {
        val documentId = notesRef.document().id

        if (documentId.isNotBlank()) {
            // Create a new note object
            val note = Notes(
                userId,
                title,
                content,
                timestamp,
                documentId = documentId
            )

            // Set the note in the Firestore collection
            notesRef
                .document(documentId)
                .set(note)
                .addOnCompleteListener { result ->
                    // Invoke the onComplete callback with the success status
                    onComplete.invoke(result.isSuccessful)
                }
        } else {
            // Handle the case where documentId is blank or null
            Log.d("StorageRepository", "ST: Error to add note")
            onComplete.invoke(false)
        }
    }



}

// Sealed class representing different states of a resource
sealed class Resources<T>(
    val data: T? = null,
    val throwable: Throwable? = null,
) {
    class Loading<T>: Resources<T>()
    class Success<T>(data: T?): Resources<T>(data = data)
    class Error<T>(throwable: Throwable?):Resources<T>(throwable = throwable)
}
