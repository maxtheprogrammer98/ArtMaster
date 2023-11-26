package com.example.artmaster.tutorials

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.artmaster.MainActivity
import com.example.artmaster.ui.theme.ArtMasterTheme

class TutorialsActivity : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                }
            }
        }
    }
    // represents the selected path
    val rutaID = intent.getStringExtra("IDPATH").toString()

    @Composable
    fun DisplayTutorials(){

    }


}