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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.artmaster.MainActivity
import com.example.artmaster.R
import com.example.artmaster.ui.theme.ArtMasterTheme
import com.google.ai.client.generativeai.GenerativeModel

class AiAssistantActivityImg : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // adding model information
        val generativeModelImg = GenerativeModel(
            modelName = "gemini-pro-vision",
            //TODO: FIX BuildConfig problem to not expose the api key!
            apiKey = "AIzaSyACRZhR_TnmticRhpOolXD00TVILiXhh_8"
        )

        setContent {
            ArtMasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                    GeneralLayout(model = generativeModelImg)
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
            // wrapper
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollingState)
                    .padding(0.dp, 60.dp)
            ){
                // background
                Image(
                    painter = painterResource(id = R.mipmap.fondo1),
                    contentDescription = stringResource(id = R.string.fondo),
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.matchParentSize())

                // sub wrapper
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ){

                    // adding robot icon
                    AddRobotIcon(painterIcon = painterResource(id = R.mipmap.robot))

                    // title
                    Text(
                        text = stringResource(id = R.string.ai_btn_img),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.White)

                    // inserting btn to pick image
                    BtnUploadImg(model = model, context = applicationContext)

                    // inserting AI response
                    DisplayFeedback()
                }
            }
        }
    }
}