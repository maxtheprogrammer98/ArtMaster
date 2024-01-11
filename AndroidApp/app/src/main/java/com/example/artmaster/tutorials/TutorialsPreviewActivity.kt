package com.example.artmaster.tutorials

import android.annotation.SuppressLint
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
import com.example.artmaster.paths.CustomCiricularProgressBar
import com.example.artmaster.paths.CustomLinearProgressBar

class TutorialsPreviewActivity : MainActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // receiving data from intent
        val namePath = intent.getStringExtra("NAME_PATH")
        val idPath = intent.getStringExtra("ID_PATH")

        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                TutorialsLayout(namePath.toString(), idPath.toString())
            }
        }
    }

    /**
     * creates the entire layout for this section
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun TutorialsLayout(pathName:String, pathID:String){
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
                // widget that shows progress
                CustomCiricularProgressBar(IDpath = pathID)

                // creates search bar that interacts with the view model
                CreateSerachBar(pathName = pathName)

                // generates cards dynamically
                GenerateCardsTutorials(
                    pathName = pathName,
                    context = applicationContext)
            }
        }
    }

}