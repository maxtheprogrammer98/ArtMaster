package com.example.artmaster.home

import android.content.Intent
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

data class SectionsGenerator(
    //Attributes
    val title:String,
    val text:String,
    val image:Painter
)
