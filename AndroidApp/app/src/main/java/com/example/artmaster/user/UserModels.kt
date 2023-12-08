package com.example.artmaster.user

import com.google.firebase.firestore.DocumentId

data class UserModels(
    @DocumentId
    val documentId:String,
    val nombre:String = "",
    val email:String = "",
    val isAdmin:Boolean = false,
    val completados:ArrayList<String> = ArrayList(),
    val favoritos:ArrayList<String> = ArrayList()
)
