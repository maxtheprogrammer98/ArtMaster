package com.example.artmaster.user

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.util.Log

class UsersViewModel : ViewModel(), GetUserID{
    // variable that stores user's ID
    val userID = getCurrentUserID()
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
        // get request
        usersCollection
            .whereEqualTo("id", userID)
            .get()
            .await()
            .map {
                Log.i("fetch request", userID)
                val result = it.toObject(UserModels::class.java).copy(id = userID)
                userProfile = result
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