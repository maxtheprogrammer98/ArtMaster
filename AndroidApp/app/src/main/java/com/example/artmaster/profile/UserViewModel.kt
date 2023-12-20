package com.example.artmaster.profile

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class UserViewModel : ViewModel() {
    val state = mutableStateOf(User())

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            state.value = getUserFromFirestore()
        }
    }

    private suspend fun getUserFromFirestore(): User {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val userId = auth.currentUser?.uid

        var user = User()

        userId?.let {
            try {
                val document = db.collection("usuarios").document(it).get().await()
                Log.d("user", "Firestore document data: ${document.data}")
                user = document.toObject(User::class.java) ?: User()
                Log.d("user", "current user: $user")
            } catch (e: FirebaseFirestoreException) {
                Log.d("error", "getUser: $e")
            }
        }


        return user
    }



    fun updateUserPhoto(photoUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val auth = FirebaseAuth.getInstance()
            val userId = auth.currentUser?.uid
            userId?.let {
                try {
                    // Upload photo to Firebase Storage
                    val storageRef = FirebaseStorage.getInstance().reference
                    val photoRef = storageRef.child("profile_photos/$it/profile_photo.jpg")
                    val uploadTask = photoRef.putFile(photoUri)
                    uploadTask.await()

                    // Get the URL of the uploaded photo
                    val photoUrl = photoRef.downloadUrl.await()

                    // Update the user's photo URL in Firestore
                    updateUserField(it, "photoUrl", photoUrl.toString())
                } catch (e: Exception) {
                    Log.d("ProfileScreen", "ERROR: $e")

                    // Handle exceptions
                }
            }
        }
    }


    fun updateUserDrawing(drawingUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val auth = FirebaseAuth.getInstance()
            val userId = auth.currentUser?.uid

            userId?.let {
                try {
                    // Upload drawing to Firebase Storage
                    val storageRef = FirebaseStorage.getInstance().reference
                    val drawingRef = storageRef.child("drawings/$it/${UUID.randomUUID()}.jpg")
                    val uploadTask = drawingRef.putFile(drawingUri)
                    uploadTask.await()

                    // Get the URL of the uploaded drawing
                    val drawingUrl = drawingRef.downloadUrl.await()

                    // Update the user's drawing array in Firestore
                    updateUserField(it, "drawingArray", FieldValue.arrayUnion(drawingUrl.toString()))
                } catch (e: Exception) {
                    Log.d("ProfileScreen", "ERROR: $e")
                    // Handle exceptions
                }
            }
        }
    }

    fun updateUserField(userId: String, field: String, value: Any) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("usuarios").document(userId)

        userRef
            .set(mapOf(field to value), SetOptions.merge())
            .addOnSuccessListener {
                // Update successful
                Log.d("Firestore", "Campo actualizado correctamente: $field")
            }
            .addOnFailureListener { e ->
                // Error update
                Log.w("Firestore", "Error al actualizar el campo $field", e)
            }
    }


}
