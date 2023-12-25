package com.example.artmaster.user

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

interface GetUserInfoAuth{
    fun getCurrentUserID() : String{
        // initializing auth service
        val auth = Firebase.auth
        // variable that stores user model
        val user = auth.currentUser
        // variable that stores user's ID
        var userID = ""
        // validating whether user exists
        if (user != null){
            userID = user.uid
        }
        // returning statement
        Log.i("user auth" , "userID: $userID")
        return userID
    }

    fun getCurrentUserEmail() : String{
        // initializing auth service
        val auth = Firebase.auth
        // variable that stores the current user
        val user = auth.currentUser
        // variable that stores the user's email
        var userEmail = ""
        // extracting user's email
        if (user != null){
            userEmail = user.email.toString()
        }
        // return statement
        Log.i("user auth", "user email: $userEmail")
        return userEmail
    }
}