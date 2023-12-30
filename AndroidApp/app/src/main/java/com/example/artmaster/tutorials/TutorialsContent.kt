package com.example.artmaster.tutorials

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.artmaster.R
import com.example.artmaster.user.UserModels
import com.example.artmaster.user.UsersViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.reflect.Field

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
    userViewModel: UsersViewModel = viewModel()
){
    // initializing user VM
    val userModel = userViewModel.userStateProfile.value
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
        Button(
            onClick = { MarkAsCompelted(id, userModel) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ){
            Text(text = stringResource(id = R.string.agregar_completado))
        }

        // ------------ ADD TO FAVS BTN ---------------//

        // ------------ RATING BAR ---------------//

    }
}

/**
 * marks tutorial as completed
 * @param userEmail
 * which is extracted through an interface method
 */
fun MarkAsCompelted(
    tutorialID: String,
    userModel: UserModels
){
    // flag variable
    var alreadyCompleted = false
    // instantiating firebase
    val db = Firebase.firestore
    // document reference
    val userDocRef = db.collection("usuarios").document(userModel.id)
    // validating whether the tutorial is already marked as completed
    for (tutorial in userModel.completados){
        if (tutorial == tutorialID){
            alreadyCompleted = true
        }
    }
    // update request
    if (!alreadyCompleted){
        // ADDING TUTORIAL
       userDocRef
           .update("completados", FieldValue.arrayUnion(tutorialID))
           .addOnCompleteListener { task ->
               if (task.isSuccessful){
                   // notification
                   Log.i("update completed", "tutorial added!" )

               } else {
                   // notification
                   Log.e("update failed", "the tutorial couldn't be added")
               }
           }
    }else{
        // REMOVING TUTORIAL
        userDocRef
            .update("completados", FieldValue.arrayRemove(tutorialID))
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    // notification
                    Log.i("update completed", "tutorial removed")
                } else {
                    // notification
                    Log.e("update failed", "the tutorial couldn't be removed!")
                }
            }
    }
}