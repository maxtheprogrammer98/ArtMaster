package com.example.artmaster.tutorials

interface AlarmScheudaler{
    fun scheudale(item: AlarmTime)
    fun cancel(item: AlarmTime)
}