package com.example.artmaster.tutorials

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

/**
 * defines how to deserialize firebase models
 */
data class TutorialsModels(
    val nombre:String = "",
    val rutaNombre:String = "",
    val imagen:String = "",
    val video:String = "",
    val informacion:String = "",
    val descripcion:String = "",
    val calificacion:Float = 0.0f,
    @DocumentId
    val id:String = ""
) : Serializable
