package com.example.artmaster.home

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.artmaster.MainActivity
import com.example.artmaster.ui.theme.ArtMasterTheme
import com.example.artmaster.R
import com.example.artmaster.home.InsertSlideshow
import com.example.artmaster.graphicElements.headerMain


class Home : MainActivity(){
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

    //images list
    val imagesList = listOf<Int>(
        R.mipmap.portada1,
        R.mipmap.portada2,
        R.mipmap.portada3
    )

    val sectionsRefe = listOf<SectionsGenerator>()

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
            Column(
                modifier = Modifier
                    .verticalScroll(scrollableSateVertical)
                    .padding(0.dp, 60.dp, 0.dp, 0.dp)
            ){
                //adding header
                headerMain()

                //inserting slideshow
                InsertSlideshow(
                    imagesList = imagesList,
                    context = applicationContext)
            }
        }
    }
}