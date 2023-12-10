package com.example.artmaster.user

import com.google.firebase.firestore.DocumentId

//TODO: fix problem that doesn't enable me to get the document ID!
data class UserModels(
    val nombre:String = "",
    val email:String = "",
    val isAdmin:Boolean = false,
    val completados:ArrayList<String> = ArrayList(),
    val favoritos:ArrayList<String> = ArrayList(),
    @DocumentId
    val id:String = ""
)
