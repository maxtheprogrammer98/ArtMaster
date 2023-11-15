package com.example.artmaster.profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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
                user = document.toObject(User::class.java) ?: User()
            } catch (e: FirebaseFirestoreException) {
                Log.d("error", "getUser: $e")
            }
        }

        return user
    }
}
