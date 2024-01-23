package com.example.artmaster.favorites

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.artmaster.MainActivity
import com.example.artmaster.R
import com.google.android.material.search.SearchBar

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
            // general wrapper
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollSate)
                    .padding(0.dp, 0.dp, 0.dp, 80.dp)
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
                FilterOptions()

                // adding cards dynamically
                GenerateCardsFavs(context = applicationContext)
            }
        }
    }
}

