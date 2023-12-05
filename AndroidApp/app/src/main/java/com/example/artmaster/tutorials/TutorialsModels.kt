package com.example.artmaster.tutorials

/**
 * defines how to deserialize firebase models
 */
data class TutorialsModels(
    val id:String = "",
    val nombre:String = "",
    val rutaNombre:String = "",
    val imagen:String = "",
    val video:String = "",
    val informacion:String = "",
    val descripcion:String = "",
    val calificacion:Float = 0.0f
)
