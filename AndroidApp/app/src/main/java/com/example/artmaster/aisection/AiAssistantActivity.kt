package com.example.artmaster.aisection

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.artmaster.MainActivity
import com.example.artmaster.ui.theme.ArtMasterTheme
import com.google.ai.client.generativeai.GenerativeModel

class AiAssistantActivity : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // adding model information
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro",
            //TODO: FIX BuildConfig problem to not expose the api key!
            apiKey = "AIzaSyACRZhR_TnmticRhpOolXD00TVILiXhh_8"
        )

        setContent {
            ArtMasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                    GeneralLayout(generativeModel)
                }
            }
        }


    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    private fun GeneralLayout(model: GenerativeModel){
        // scroll variable
        val scrollVarable = rememberScrollState()

        // setting main layout
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp, 50.dp)
                    .verticalScroll(scrollVarable)
            ){
                // inserting input field
                InputFieldAIhelp(model = model)

                // response text
                DisplayAIresponse()

            }
        }
    }

}