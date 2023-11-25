package com.example.artmaster.paths

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UsersViewModelPath : ViewModel(){
    //variables that stores the user's fetched data
    var userState = mutableStateOf(
        UsersModels()
    )

    // initializing class
    init {
        getUserData()
    }

    /**
     * function that gets the current user
     */
    fun getCurrentUser():String{
        // instantiating auth
        val user = Firebase.auth.currentUser
        var userID = ""
        //validating user:
        if(user != null){
            userID = user.uid
        }
        // returning info
        return userID
    }

    /**
     * it executes a fetch request based on the previously obtained ID
     */
    private suspend fun fetchUserData():UsersModels{
        // reference values
        val userID = getCurrentUser()
        var userModel = UsersModels()
        // instantiating firestore
        val db = Firebase.firestore
        // fetching document
        userID.let{
            try {
                val userDocument = db.collection("usuarios").document(it).get().await()
                // deserializing document
                userModel = userDocument.toObject(UsersModels::class.java)?:UsersModels()
            } catch (e:FirebaseFirestoreException){
                Log.e("error", "error while retrieving document", e)
            }
        }
        // returning deserialized object
        Log.i("userModel", "user's done tutos: ${userModel.completados.size}")
        return userModel
    }

    private fun getUserData(){
        viewModelScope.launch {
            userState.value = fetchUserData()
        }
    }


}