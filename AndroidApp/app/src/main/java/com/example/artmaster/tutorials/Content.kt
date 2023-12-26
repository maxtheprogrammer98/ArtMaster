package com.example.artmaster.tutorials

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.artmaster.R

/**
 * Creates front page, using Picasso to render the image fetched from DB
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun addPortada(linkImg : String){
    //TODO: Install picasso to render images
}

/**
 * Adds all the information regarding the tutorial
 */
@Composable
fun AddTutorialContent(
    nombre: String,
    descripcion: String,
    informacion : String,
    calificacion: Float,
){
    // --------------------- CONTENEDOR GENERAL -----------------------//
    //TODO: improve styling
    Column(
        modifier = Modifier.fillMaxSize()
    ){
        // ------------ TITULO ---------------//
        Text(text =  nombre)

        // ------------ DESCRIPTION ---------------//
        Column(
            modifier = Modifier.fillMaxWidth()
        ){
            Text(text = stringResource(id = R.string.descripcion))
            Text(text = descripcion)
        }

        // ------------ INFORMATION ---------------//
        Column(
            modifier = Modifier.fillMaxWidth()
        ){
            Text(text = stringResource(id = R.string.informacion))
            Text(text = informacion)
        }
    }
}
