package com.example.artmaster.tutorials

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.artmaster.MainActivity
import com.example.artmaster.R

class TutorialsActivity : MainActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intentInfo = intent.getStringExtra("NAME_PATH")
        Log.i("pathNombre", "is equal to: $intentInfo")

        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                TutorialsLayout(intentInfo.toString())
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun TutorialsLayout(pathName:String){
        // ------------------------ VARIABLES ----------------------//
        val scrollingState = rememberScrollState()

        // ------------------------ MAIN LAYOUT ----------------------//
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),

            topBar = {
                super.TobBarMain()
            },

            bottomBar = {
                super.BottomBar()
            }
        ){
            //--------------------- MAIN CONTAINER -------------------//
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollingState)
                    .padding(0.dp, 70.dp)
            ){
                //TODO: INSERT COMPOSABLE FILE (PRESENTATION)

                CreateSerachBar(pathName = pathName)

                GenerateCardsTutorials(pathName = pathName)
            }
        }
    }

}