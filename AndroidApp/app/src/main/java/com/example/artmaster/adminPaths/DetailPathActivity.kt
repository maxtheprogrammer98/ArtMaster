package com.example.artmaster.adminPaths

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import com.example.artmaster.MainActivity
import com.example.artmaster.ui.theme.ArtMasterTheme

class DetailPathActivity: MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMasterTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                }
            }
        }
    }




}