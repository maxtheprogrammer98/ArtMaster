package com.example.artmaster.paths


/**
 * generates models from firestore data
 */
data class PathsModels(
    val ID:String,
    val nombre:String,
    val informacion:String,
    val imagen:String,
    val dificultad:String
)
