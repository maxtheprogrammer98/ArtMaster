package com.example.artmaster.adminUsers

data class Users (
    val userID: String = "",
    val email: String = "",
    val name: String = "",
    @JvmField
    val isAdmin: Boolean = false,
    val photoUrl: String = "",
    val completados: List<String> = emptyList(),
    val favoritos: List<String> = emptyList(),
    val drawingArray: List<String> = emptyList(),
)