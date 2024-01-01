package com.example.artmaster.tutorials

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.artmaster.R
import com.example.artmaster.user.UsersViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object btnDone{
    // default value
    var txtBtn = "Agregar a 'completados'"
    var flagState = false
}

/**
 * Creates front page, using Picasso to render the image fetched from DB
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun AddPortada(linkImg : String){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .wrapContentSize(Alignment.Center)
            .background(Color.LightGray)
            .padding(0.dp, 60.dp, 0.dp, 0.dp)
    ){
        AsyncImage(
            //TODO: Add real image in FS!
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://loremflickr.com/800/800/art?lock=1")
                .crossfade(1000)
                .transformations(CircleCropTransformation())
                .build(),
            contentDescription = "imagen",
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 20.dp))
    }
}

/**
 * Adds all the information regarding the tutorial
 */
@Composable
fun AddTutorialContent(
    id: String,
    nombre: String,
    informacion : String,
    calificacion: Float,
    context: Context
){
    // --------------------- CONTENEDOR GENERAL -----------------------//
    //TODO: improve styling
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 250.dp, 10.dp, 0.dp)
    ){
        // ------------ TITULO ---------------//
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .wrapContentSize(Alignment.Center)
        ){
            Text(
                text = nombre,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                fontSize = 20.sp)
        }


        // ------------ INFORMATION ---------------//
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp)
                .wrapContentSize(Alignment.Center)
        ){
            Text(
                text = stringResource(id = R.string.informacion),
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                fontSize = 20.sp,
                textAlign = TextAlign.Center)

            Text(
                text = informacion,
                modifier = Modifier
                    .padding(20.dp),
                fontSize = 14.sp
            )
        }
        // ------------ MARK AS COMPLETE BTN ---------------//
        AddDoneButton(tutorialID = id, context = context)

        // ------------ ADD TO FAVS BTN ---------------//

        // ------------ RATING BAR ---------------//

    }
}


@Composable
fun AddDoneButton(
    userViewModel: UsersViewModel = viewModel(),
    tutorialID: String,
    context: Context){
    // user's ID
    val userID = userViewModel.userStateProfile.value.id
    // validating tutorial state
    for (elem in userViewModel.userStateProfile.value.completados){
        if (elem.equals(tutorialID)){
            btnDone.flagState = true
        }
    }
    // adding btn
    Button(
        onClick = { validateTutorialState(tutorialID,userID,context) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ){
        // adding btn text
        Text(text = btnDone.txtBtn)
    }

}

/**
 * validates whether the tutorial is completed based on flag and executes the
 * according request
 */
fun validateTutorialState(
    tutorialID: String,
    userID: String,
    context: Context
){
    if(btnDone.flagState){
        // updating flags
        btnDone.flagState = false
        btnDone.txtBtn = "agregar a 'completados'"
        // executing update request
        removeTutorialDone(tutorialID, userID, context)

    } else {
        // updating flags
        btnDone.flagState = true
        btnDone.txtBtn = "eliminar de 'completados'"
        // executing update request
        addTutorialDone(tutorialID,userID,context)
    }
}

/**
 * it updates the user's document adding the done tutorial
 */
fun addTutorialDone(
    tutorialID: String,
    userID: String,
    context:Context){
    // instantiating DB
    val db = Firebase.firestore
    // document reference
    val userDocRef = db.collection("usuarios").document(userID)
    // update request
    userDocRef
        .update("completados", FieldValue.arrayUnion(tutorialID))
        .addOnCompleteListener { task ->
            if(task.isSuccessful){
                // notification
                Toast.makeText(
                    context,
                    "tutorial agregado a 'completados'",
                    Toast.LENGTH_SHORT).show()
            } else {
                //notification
                Toast.makeText(
                    context,
                    "error: ${task.exception}",
                    Toast.LENGTH_SHORT).show()
            }
        }
}

/**
 * updates the user's document removing the tutorial from 'completed' array
 */

fun removeTutorialDone(
    tutorialID: String,
    userID: String,
    context: Context
){
    // instantiating firebase DB
    val db = Firebase.firestore
    // document reference
    val userDocRef = db.collection("usuarios").document(userID)
    // update request
    userDocRef
        .update("completados", FieldValue.arrayRemove(tutorialID))
        .addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(
                    context,
                    "el tutorial se ha eliminado de 'completados'",
                    Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(
                    context,
                    "error: ${task.exception}",
                    Toast.LENGTH_SHORT).show()
            }
        }

}