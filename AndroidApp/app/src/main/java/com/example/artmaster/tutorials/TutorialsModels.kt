package com.example.artmaster.tutorials

import com.google.firebase.firestore.DocumentId

/**
 * defines how to deserialize firebase models
 */
data class TutorialsModels(
    @DocumentId
    val documentId:String = "",
    val nombre:String = "",
    val rutaNombre:String = "",
    val imagen:String = "",
    val video:String = "",
    val informacion:String = "",
    val descripcion:String = "",
    val calificacion:Float = 0.0f
)
