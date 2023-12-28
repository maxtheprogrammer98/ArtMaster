package com.example.artmaster.profile

import androidx.compose.ui.graphics.ImageBitmap

enum class MultiFloatingState{
    Expanded,
    Collapse
}

enum class Identifier{
    Photo,
    Drawing,
    Password
}

class MinFabItem(
    val icon: ImageBitmap,
    val label: String,
    val identifier: String
)