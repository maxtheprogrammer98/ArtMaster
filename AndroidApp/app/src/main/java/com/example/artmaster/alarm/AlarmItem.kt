package com.example.artmaster.alarm

import java.time.LocalDateTime

data class AlarmItem(
    val time: LocalDateTime,
    val title: String,
    val content: String
)
