package com.example.artmaster.tutorials

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

object ButtonsFlags{
    // default values
    var btnDone = false
    var btnFavs = false
    var btnRate = false
}

/**
 * Creates front page, using Picasso to render the image fetched from DB
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun AddPortada(linkImg : String){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(0.dp, 55.dp, 0.dp, 0.dp),
    ){

        // adding background
        Box(modifier = Modifier.fillMaxSize()){
                Image(
                    painter = painterResource(id = R.mipmap.header2),
                    contentDescription = stringResource(id = R.string.fondo),
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.matchParentSize())
        }

        // adding tutorial image
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
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
}

/**
 * Adds all the information regarding the tutorial
 */
@Composable
fun AddTutorialContent(
    id: String,
    nombre: String,
    informacion : String,
    context: Context,
    userEmail: String
){
    // --------------------- CONTENEDOR GENERAL -----------------------//
    //TODO: improve styling
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 0.dp, 10.dp, 0.dp)
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
        AddBtnFavs(tutorialID = id, context = context)

        // ------------ RATING BAR ---------------//
        // description
        Text(
            text = stringResource(id = R.string.calificacion),
            modifier = Modifier
                .padding(10.dp),
            textAlign = TextAlign.Center)
        // rating bar
        RatingBar(onRatingChange = {task ->
            // notification
            Toast.makeText(
                context,
                "puntuacion seleccionada: ${task}",
                Toast.LENGTH_SHORT).show()
            // uploading result to DB
            getScore(
                userEmail = userEmail,
                tutorialID = id,
                puntuacion = task
            )
        })
        // ------------ REMINDER ---------------//
        setReminder(context, nombre)

    }
}


@Composable
fun AddDoneButton(
    userViewModel: UsersViewModel = viewModel(),
    tutorialID: String,
    context: Context){
    // text remember variable
    var btnText by remember {
        mutableStateOf("Agregar a 'completados' ✅")
    }
    // user's ID
    val userID = userViewModel.userStateProfile.value.id
    // validating tutorial state
    for (elem in userViewModel.userStateProfile.value.completados){
        if (elem.equals(tutorialID)){
            ButtonsFlags.btnDone = true
            btnText = "Eliminar de completados ❎"
        }
    }
    // adding btn
    Button(
        onClick = {
            // executing update request
            validateTutorialState(tutorialID,userID,context);
            // updating text btn
            btnText = setBtnDoneText(ButtonsFlags.btnDone)},
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ){
        // adding btn text
        Text(text = btnText)
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
    if(ButtonsFlags.btnDone){
        // updating flags
        ButtonsFlags.btnDone = false
        // executing update request
        removeTutorialDone(tutorialID, userID, context)

    } else {
        // updating flags
        ButtonsFlags.btnDone = true
        // executing update request
        addTutorialDone(tutorialID,userID,context)
    }
}


fun setBtnDoneText(flagState: Boolean) : String{
    // base variable
    var btnText = ""
    // validation
    if(flagState){
        btnText = "Eliminar de 'completados' ❎"
    }else{
        btnText = "Agregar a 'completados' ✅"
    }
    // return statement
    return btnText
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

/**
 * enables the user to add and remove the tutorial from 'favs'
 */
@Composable
fun AddBtnFavs(
    tutorialID: String,
    context: Context,
    userViewModel: UsersViewModel = viewModel()
){
    // extracting user's id
    val userID = userViewModel.userStateProfile.value.id
    // remember text variable
    var favBtnText by remember {
        mutableStateOf("Agregar a 'favoritos'")
    }
    // checking whether is already stored as 'fav'
    for (fav in userViewModel.userStateProfile.value.favoritos){
        if(fav.equals(tutorialID)){
            ButtonsFlags.btnFavs = true
            favBtnText = "Eliminar de 'favoritos'"
        }
    }
    // adding button
    Button(
        onClick = {
            // executes update request
            validateFavState(tutorialID,userID,context);
            // updates text
            favBtnText = setFavBtnText(ButtonsFlags.btnFavs)},
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 0.dp)
    ){
        // adding text
        Text(text = favBtnText)
    }
}

/**
 * sets the text of the btn depending on the flag state
 */
fun setFavBtnText(flagState: Boolean):String{
    //base value
    var btnText = ""
    // validation
    if (flagState){
        btnText = "Eliminar de 'favoritos'"
    } else {
        btnText = "Agregar a 'favoritos'"
    }
    // return statement
    return btnText
}


/**
 * checks whether the tutorial is stored as fav in order
 * to execute the update request accordingly
 */
fun validateFavState(
    tutorialID: String,
    userID: String,
    context: Context
){
    if(ButtonsFlags.btnFavs){
        // updating flag
        ButtonsFlags.btnFavs = false
        // executing request
        removeFavTutorial(tutorialID,userID,context)
    }else{
        // updating flag
        ButtonsFlags.btnFavs = true
        // executing request
        addFavTutorial(userID,tutorialID,context)
    }
}

/**
 * adds the tutorial to favs (which is an array property in the user document)
 */
fun addFavTutorial(
    userID: String,
    tutorialID: String,
    context: Context
){
    //instantiating firebase
    val db = Firebase.firestore
    // document reference
    val userDocRef = db.collection("usuarios").document(userID)
    // update request
    userDocRef
        // executing request
        .update("favoritos", FieldValue.arrayUnion(tutorialID))
        // handling results
        .addOnCompleteListener { task ->
            if (task.isSuccessful){
                //notification success
                Toast.makeText(
                    context,
                    "el tutorial se ha agregado a 'favoritos'",
                    Toast.LENGTH_SHORT).show()
            } else {
                //notification error
                Toast.makeText(
                    context,
                    "error ${task.exception}",
                    Toast.LENGTH_SHORT).show()
            }
        }
}

/**
 * removes the tutorial to favs (which is an array property in the user document)
 */
fun removeFavTutorial(
    tutorialID: String,
    userID: String,
    context: Context
){
    // instantiating firestore
    val db = Firebase.firestore
    // document reference
    val userDocRef = db.collection("usuarios").document(userID)
    // update request
    userDocRef
        // executing request
        .update("favoritos", FieldValue.arrayRemove(tutorialID))
        // handling results
        .addOnCompleteListener { task ->
            if (task.isSuccessful){
                // notification success
                Toast.makeText(
                    context,
                    "El tutorial ha sido eliminado de 'favoritos'",
                    Toast.LENGTH_SHORT).show()
            } else {
                // notification error
                Toast.makeText(
                    context,
                    "error ${task.exception}",
                    Toast.LENGTH_SHORT).show()
            }
        }
}

/**
 * creates a rating bar based on the specified scale assigned in the parameters
 */
@Composable
fun RatingBar(
    maxRatingBar: Int = 5,
    initialRatingBar: Int = 1,
    onRatingChange: (Int) -> Unit
){
    // remember variable
    var rating by remember {
        mutableStateOf(initialRatingBar)
    }

    // adding container and its parameters
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        // generating items dynamically
        for(i in 1..maxRatingBar){
            RatingBarItem(
                rating = i ,
                isSelected = i <= rating,
                onRatingSelected = {
                    rating = i
                    onRatingChange(i)
                })
        }
    }
}

/**
 * generates the rating bar items
 */
@Composable
fun RatingBarItem(
    rating: Int,
    isSelected: Boolean,
    onRatingSelected: () -> Unit
){
    // changing styles based on boolean flag
    val icon = if(isSelected) Icons.Default.Star else Icons.TwoTone.Star
    val color = if(isSelected) Color.Yellow else Color.Gray

    // generating icon
    Icon(
        imageVector = icon,
        contentDescription = stringResource(id = R.string.estrella_icono),
        tint = color,
        modifier = Modifier
            .size(40.dp)
            .clickable { onRatingSelected() })
}

/**
 * it adds a new document into the "votos" collection which stores
 * all votes based on the following properties (tutorialID, userEmail, puntuacion)
 */
fun getScore(
    userEmail: String,
    tutorialID: String,
    puntuacion: Int
){
    // instantiating firebase DB:
    val db = Firebase.firestore
    // collection reference
    val collectionRef = db.collection("votos")
    // updating document (if user already voted)
    collectionRef
        // filtering results
        .whereEqualTo("tutorialID", tutorialID)
        .whereEqualTo("userEmail", userEmail)
        .get()
        // handling results
        .addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty){
                // a new document is created with the chosen score
                createScore(
                    tutorialID = tutorialID,
                    userEmail = userEmail,
                    puntuacion = puntuacion
                )
            } else{
                // otherwise the ID is extracted in order to update the document
                for (document in querySnapshot){
                    // extracting document ID
                    val voteID = document.id
                    // executing updating function
                    updateScore(
                        documentID = voteID,
                        puntuacion = puntuacion
                    )
                }
            }
        }
}

/**
 * updates the document according to the arguments passed
 */
fun updateScore(
    documentID : String,
    puntuacion: Int
){
    // testing arguments
    Log.i("score ID", "id: $documentID" )
    // instantiating firestore
    val db = Firebase.firestore
    // document reference
    val docRef = db.collection("votos").document(documentID)
    // update request
    docRef
        // executing request
        .update("puntuacion", puntuacion)
        // handling results
        .addOnCompleteListener { task ->
            if (task.isSuccessful){
                // notification
                Log.i("score", "score has been updated")
            } else {
                // notification
                Log.e("score", "error: ${task.exception}")
            }
        }
}

/**
 * if the user hasn't voted already a new document is created with the chosen score
 */
fun createScore(
    tutorialID: String,
    userEmail: String,
    puntuacion: Int
){
    // creating map from passed arguments
    // enterely based on string values
    val newScore = mapOf<String,Any>(
        "userEmail" to userEmail,
        "tutorialID" to tutorialID,
        "puntuacion" to puntuacion
    )
    // instantiating database
    val db = Firebase.firestore
    // collection reference
    val collectionRef = db.collection("votos")
    // create request
    collectionRef
        // executing request
        .add(newScore)
        // handling results
        .addOnCompleteListener { task ->
            if(task.isSuccessful){
                // notification
                Log.i("score", "a new score has been created")
            } else{
                Log.e("score", "error has ocurred ${task.exception}")
            }
        }
}


/**
 * triggers a request to set a reminder on a calendar
 */
@Composable
fun BtnSetReminder(){
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ){
        Text(
            text = stringResource(id = R.string.agregar_reminder),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center)
    }
}
