package com.example.artmaster.paths

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.DocumentId

/**
 * generates models from firestore data
 */
data class PathsModels(
    // constructor
    val nombre:String = "",
    val informacion:String = "",
    val imagen:String = "",
    val dificultad:String = "",
    val tutorialesID: ArrayList<String> = ArrayList<String>(), //includes the tutorials IDs that belong to this path
    @DocumentId
    val id:String = ""
)
