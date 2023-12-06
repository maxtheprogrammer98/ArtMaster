package com.example.artmaster.user

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

interface GetUserID{
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
        return userID
    }
}