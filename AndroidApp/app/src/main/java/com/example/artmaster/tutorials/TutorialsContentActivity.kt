package com.example.artmaster.tutorials

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.artmaster.MainActivity
import com.example.artmaster.R

/**
 * this activity contains the tutorials content, from the main activity
 * the menu bar is inhereted
 */
class TutorialsContentActivity : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ){
                TutorialsLayoutContent()
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    fun TutorialsLayoutContent(){
        /* ------------------ MAIN LAYOUT ----------------------- */
        Scaffold(
            topBar = {
                super.TobBarMain()
            },
            bottomBar = {
                super.BottomBar()
            },
            modifier = Modifier
                .fillMaxSize()
        ){
            /* ------------------ CONTENT ----------------------- */
        }
    }

}