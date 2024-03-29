package com.example.artmaster.aisection

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.artmaster.MainActivity
import com.example.artmaster.R
import com.example.artmaster.aisection.ui.theme.ArtMasterTheme
import com.google.ai.client.generativeai.GenerativeModel

class AiAssistantActivityIdeas : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // AI model data
        // adding model information
        val generativeModelTextCreative = GenerativeModel(
            modelName = "gemini-pro",
            //TODO: FIX BuildConfig problem to not expose the api key!
            apiKey = "AIzaSyACRZhR_TnmticRhpOolXD00TVILiXhh_8"
        )


        // inserting content
        setContent {
            ArtMasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GeneralLayout(generativeModelTextCreative)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun GeneralLayout(model: GenerativeModel){
        // scrolling state variable
        val scrollingState = rememberScrollState()

        // main container
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollingState)
            ){
                // fondo
                Image(
                    painter = painterResource(id = R.mipmap.fondo1),
                    contentDescription = stringResource(id = R.string.fondo),
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.FillBounds)
                
                // wrapper
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp,70.dp)
                ){
                    // inserting robot Icon
                    AddRobotIcon(painterIcon = painterResource(id = R.mipmap.robot))
                    
                    // inserting text field
                    GetDrawingIdeas(
                        model = model,
                        context = applicationContext)
                    
                    // inserting field response
                    DisplayDrawingIdeas(model = model)
                }
            }
        }
    }
}