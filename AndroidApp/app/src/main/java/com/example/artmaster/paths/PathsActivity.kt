package com.example.artmaster.paths

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.artmaster.MainActivity
import com.example.artmaster.R
import com.example.artmaster.graphicElements.HeaderMain
import com.example.artmaster.graphicElements.LoadingScreen
import com.example.artmaster.ui.theme.ArtMasterTheme


class PathsActivity : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                    CreateRutas()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun CreateRutas(pathsViewModel: PathsViewModel = viewModel()){
        // scrolling variable
        val scrollingState = rememberScrollState()
        // checking whether the data has been fetched
        if (pathsViewModel.isLoading.value){
            // inserting loading screen
            LoadingScreen()
        } else {
            // regular content is displayed

            // ----------------------- MAIN LAYOUT / SCAFFOLD --------------------------//
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
                // ----------------------- MAIN CONTAINER --------------------------//
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollingState)

                ){
                    // ------------ BACKGROUND ---------//
                    Image(
                        painter = painterResource(id = R.mipmap.fondo6),
                        contentDescription = stringResource(id = R.string.fondo),
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.FillBounds)

                    // ------------ WRAPPER ---------//
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(0.dp,60.dp,0.dp,80.dp)
                    ){
                        // ------------ INSERTING HEADER ---------//
                        HeaderMain()
                        // ----------------- CARDS -----------------//
                        CreateCards(context = applicationContext)
                    }
                }
            }
        }
    }
}