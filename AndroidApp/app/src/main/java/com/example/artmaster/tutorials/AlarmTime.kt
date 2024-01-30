package com.example.artmaster.tutorials

import java.time.LocalDateTime

/**
 * info that will be displayed in the reminder
 */
data class AlarmTime(
    val time: LocalDateTime,
    val message: String
)
