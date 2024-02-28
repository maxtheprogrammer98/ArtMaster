package com.example.artmaster.adminPaths

data class Paths(
    val pathsID: String = "",
    val dificultad: String = "",
    val imagen: String = "",
    val informacion: String = "",
    val nombre: String = "",
    val tutorialesID: List<String> = emptyList()
)