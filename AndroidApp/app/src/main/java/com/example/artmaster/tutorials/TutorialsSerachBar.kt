package com.example.artmaster.tutorials

import android.annotation.SuppressLint
import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.artmaster.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("MutableCollectionMutableState")
fun CreateSerachBar(dataViewModel: TutorialsViewModel = viewModel()){
    // -------------- BASE VARIABLES -----------------//
    // data from fetched models
    var tutorialModels = dataViewModel.stateTutorials.value
    // input entered by user
    var searchQuery by remember {
        mutableStateOf("")
    }
    // ---------------- WRAPPER -----------------//
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .wrapContentSize(Alignment.Center)
    ){
        // ---------------- SEARCHBAR -----------------//
        TextField(
            value = searchQuery ,
            onValueChange = {searchQuery = it},
            placeholder = {
                Text(text = stringResource(id = R.string.buscar_tutorial))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp)
                ),
            leadingIcon = {
                IconButton(
                    onClick = {
                        // filters the tutorials whose name match the input
                        tutorialModels.filter {
                            it.nombre.contains(searchQuery)
                        }
                    }
                ){
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.buscar_tutorial))
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        // deletes input
                        searchQuery = ""
                    }
                ){
                    Icon(
                        imageVector = Icons.Filled.Clear ,
                        contentDescription = stringResource(id = R.string.borrar_busqueda))
                }
            })
    }
}