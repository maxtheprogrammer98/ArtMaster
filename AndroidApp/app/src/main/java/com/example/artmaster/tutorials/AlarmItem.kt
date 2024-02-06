package com.example.artmaster.tutorials

import java.time.LocalTime

data class AlarmItem(
    val time : LocalTime,
    val date : java.util.Date,
    val message : String
)
