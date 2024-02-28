package com.example.artmaster.notes

import com.google.firebase.Timestamp

data class Notes(
    val userId: String = "",
    val title: String = "",
    val content: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val documentId: String = ""
)
