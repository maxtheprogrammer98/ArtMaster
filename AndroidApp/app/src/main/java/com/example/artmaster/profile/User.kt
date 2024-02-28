package com.example.artmaster.profile

data class User(
    val name: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    @JvmField
    val isAdmin: Boolean = false,
    val drawingArray: List<String> = emptyList()
)
