package com.example.artmaster.user

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestoreException

class UsersViewModel : ViewModel(), GetUserID{
    // variable that stores user's ID
    val userID = ""
    // mutable variable that stores fetched data
    var userStateProfile = mutableStateOf(UserModels())

    //initializing
    init {
        getUserData()
    }

    //asynchronous request
    suspend fun fetchUserProfile(userID:String) : UserModels {
        // variable that stores fetched data
        var userProfile = UserModels()
        //instantiating firebase
        val db = Firebase.firestore
        // collection reference
        val usersCollection = db.collection("usuarios")
        // get request (applying handling error structures)
        try {
            usersCollection
                .whereEqualTo("id",userID)
                .get()
                .await()
                .map{
                    // deserializing object
                    val result = it.toObject(UserModels::class.java)
                    userProfile= result
                }

        } catch (e : FirebaseFirestoreException){
            Log.e("error_vm_user", "error trying to fetch user model", e)
        }
        // returning statement
        return userProfile
    }

    // processing previous request
    fun getUserData(){
        viewModelScope.launch {
            userStateProfile.value = fetchUserProfile(userID)
        }
    }

}