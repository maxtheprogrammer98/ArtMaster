package com.example.artmaster.tutorials

import com.google.firebase.firestore.DocumentId

data class VotosModels(
    val tutorialID : String = "",
    val userEmail : String = "",
    val puntuacion : Int = 0,
    @DocumentId
    val id : String = ""
)
