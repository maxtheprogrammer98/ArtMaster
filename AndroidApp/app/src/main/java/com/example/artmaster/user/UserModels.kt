package com.example.artmaster.user

data class UserModels(
    val nombre:String = "",
    val email:String = "",
    val isAdmin:Boolean = false,
    val completados:ArrayList<String> = ArrayList(),
    val favoritos:ArrayList<String> = ArrayList()
)
