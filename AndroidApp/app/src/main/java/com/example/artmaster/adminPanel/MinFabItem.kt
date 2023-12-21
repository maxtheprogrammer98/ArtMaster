package com.example.artmaster.adminPanel

import androidx.compose.ui.graphics.ImageBitmap

enum class MultiFloatingState{
    Expanded,
    Collapse
}

enum class Identifier{
    Users,
    Tutorials,
    Paths
}

class MinFabItem(
    val icon: ImageBitmap,
    val label: String,
    val identifier: String
)