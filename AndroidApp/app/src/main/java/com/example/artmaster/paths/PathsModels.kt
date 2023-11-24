package com.example.artmaster.paths

import com.google.firebase.firestore.PropertyName


/**
 * generates models from firestore data
 */
data class PathsModels(
    // constructor
    val id:String = "",
    val nombre:String = "",
    val informacion:String = "",
    val imagen:String = "",
    val dificultad:String = "",
    val tutoriales: ArrayList<String> = ArrayList<String>()
)
