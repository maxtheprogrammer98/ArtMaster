package com.example.artmaster.tutorials

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
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.artmaster.MainActivity
import com.example.artmaster.R
import com.example.artmaster.paths.CustomCiricularProgressBar

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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollingState)
                    .padding(0.dp, 60.dp)
            ){
                // adding wooden background
                Image(
                    painter = painterResource(id = R.mipmap.fondo1),
                    contentDescription = stringResource(id = R.string.fondo),
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.matchParentSize())

                // general wrapper
                Column(
                    modifier = Modifier.fillMaxSize()
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

}