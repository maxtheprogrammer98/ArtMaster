package com.example.artmaster.alarm

//import java.time.LocalDateTime
import java.util.Date

data class AlarmItem(
    var time: Date?,
    val title: String,
    val content: String
)
