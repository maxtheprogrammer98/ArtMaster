package com.example.artmaster.graphicElements

import androidx.compose.ui.graphics.vector.ImageVector

data class ItemsGenerator(
    val name:String,
    val contentDescription:String,
    val icon:ImageVector,
    val onlyAdmin:Boolean,
    val visitorCanAccess:Boolean
)
