package com.example.artmaster.favorites

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.Modifier
import com.example.artmaster.MainActivity
import com.example.artmaster.R

class FavActivity : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                FavsLayout()
            }
        }
    }

    /**
     * contains the general layout of the section
     */
    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    private fun FavsLayout(){

        // enables vertical scrolling
        val scrollSate = rememberScrollState()

        // main layout
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollSate),

            bottomBar = {
                super.BottomBar()
            },

            topBar = {
                super.TobBarMain()
            }
        ){
            // inserting cards
            CardsFavs(context = applicationContext)
        }
    }
}

