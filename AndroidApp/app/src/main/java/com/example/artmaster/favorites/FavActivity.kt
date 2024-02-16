package com.example.artmaster.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.artmaster.MainActivity
import com.example.artmaster.R

class FavActivity : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                FavsLayout()
            }
        }
    }

    /**
     * contains the general layout of the section
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter",
        "UnusedMaterialScaffoldPaddingParameter"
    )
    private fun FavsLayout(
    ){
        // enables vertical scrolling
        val scrollSate = rememberScrollState()

        // main layout
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),

            bottomBar = {
                super.BottomBar()
            },

            topBar = {
                super.TobBarMain()
            }
        ){
            // main container
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollSate)
            ){

                // image background
                Image(
                    painter = painterResource(id = R.mipmap.fondo1),
                    contentDescription = stringResource(id = R.string.fondo),
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.FillBounds)

                // wrapper
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp,0.dp,0.dp,90.dp)
                ){
                    // adding header
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        Image(
                            painter = painterResource(id = R.mipmap.header),
                            contentDescription = stringResource(id = R.string.imagen) )
                    }

                    // adding search bar file
                    FavsSearchBar()

                    // adding filtering option (based on paths)
                    FilterOptions(context = applicationContext)

                    // adding cards dynamically
                    GenerateCardsFavs(context = applicationContext)
                }
            }
        }
    }
}

