package com.example.artmaster.home

import android.annotation.SuppressLint
import android.content.Intent
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
import com.example.artmaster.aisection.AiAssistantActivityPreview
import com.example.artmaster.paths.PathsActivity
import com.example.artmaster.ui.theme.ArtMasterTheme

class HomeActivity : MainActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                    CreateHome()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun CreateHome(){
        // remember variables
        val scrollableSateVertical = rememberScrollState()

        // ------------------------ SCAFFOLD MAIN LAYOUT ---------------------------//
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
            // ------------------------ BOX MAIN CONTAINER ---------------------------//
            Box(
                modifier = Modifier
                    .verticalScroll(scrollableSateVertical)
                    .padding(0.dp, 60.dp)
            ){
                // adding background
                Image(
                    painter = painterResource(id = R.mipmap.fondo6),
                    contentDescription = stringResource(id = R.string.fondo),
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.FillBounds)

                // general wrapper
                Column(
                    modifier = Modifier.fillMaxSize()
                ){
                    // adding header
                    Header()

                    // adding front page
                    FrontPage()

                    // adding welcome message
                    WelcomeMessage(message = "Bienvenido a ArtMaster")

                    // adding IA section card
                    CardsSections(
                        title = stringResource(id = R.string.asistente_ia) ,
                        icon = painterResource(id = R.mipmap.iconiaalt),
                        description = stringResource(id = R.string.card_ai),
                        navigateTo = {
                            Intent(applicationContext,AiAssistantActivityPreview::class.java).also {
                                startActivity(it)
                            }
                        })

                    // adding paths section card
                    CardsSections(
                        title = stringResource(id = R.string.rutas),
                        icon = painterResource(id = R.mipmap.artiaicon),
                        description = stringResource(id = R.string.card_paths),
                        navigateTo = {
                            Intent(applicationContext,PathsActivity::class.java).also {
                                startActivity(it)
                            }
                        })

                    // adding notes section card
                    CardsSections(
                        title = stringResource(id = R.string.notas),
                        icon = painterResource(id = R.mipmap.notesiconia),
                        description = stringResource(id = R.string.card_notes),
                        navigateTo = {
                            Intent(applicationContext,PathsActivity::class.java).also {
                                startActivity(it)
                            }
                        })
                    
                    // adding developers contact
                    CardDevelopers(context = applicationContext)
                }
            }
        }
    }
}