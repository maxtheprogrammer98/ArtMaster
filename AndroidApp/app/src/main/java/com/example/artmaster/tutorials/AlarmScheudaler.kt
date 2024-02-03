package com.example.artmaster.tutorials

interface AlarmScheudaler {
    fun scheudale(item : AlarmItem)
    fun cancel(item : AlarmItem)
}