package com.example.artmaster.aisection

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.example.artmaster.aisection.ui.theme.ArtMasterTheme

class AiAssistantActivityPreview : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GeneralLayoutPreview()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun GeneralLayoutPreview(){
        // scrolling variable
        val scrollingState = rememberScrollState()

        //layout
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
            ){
                // background
                Image(
                    painter = painterResource(id = R.mipmap.fondo1),
                    contentDescription = stringResource(id = R.string.fondo),
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.FillBounds)

                // sub wrapper
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp, 70.dp)
                ){
                    
                    // ai icon
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp,20.dp,0.dp,5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Image(
                            painter = painterResource(id = R.mipmap.iconia),
                            contentDescription = "AI logo",
                            modifier = Modifier
                                .height(130.dp)
                                .width(130.dp))
                    }

                    // wrapper
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        //title
                        Text(
                            text = stringResource(id = R.string.ia_title_preview),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(20.dp))
                    }
                    
                    // option text input
                    AddOptionAI(
                        painterIcon = painterResource(id = R.mipmap.notesiconia) ,
                        description = stringResource(id = R.string.ia_text_option),
                        context = applicationContext,
                        intentTo = "textActivity")

                    // option image feedback
                    AddOptionAI(
                        painterIcon = painterResource(id = R.mipmap.artiaicon),
                        description = stringResource(id = R.string.ia_image_option),
                        context = applicationContext,
                        intentTo = "imageActivity")

                    // option drawing ideas
                    AddOptionAI(
                        painterIcon = painterResource(id = R.mipmap.eureka),
                        description = stringResource(id = R.string.ai_drawing_ideas),
                        context = applicationContext,
                        intentTo = "ideasActivity")
                }
            }
        }
    }
}
