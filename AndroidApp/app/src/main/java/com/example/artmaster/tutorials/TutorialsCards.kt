package com.example.artmaster.tutorials

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.artmaster.R
import com.example.artmaster.user.UserModels
import com.example.artmaster.user.UsersViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * renders dynamically the tutorials cards based on the path selected
 */
@Composable
@SuppressLint("MutableCollectionMutableState")
//@Preview
fun GenerateCardsTutorials(
    dataViewModel: TutorialsViewModel = viewModel(),
    pathName:String ,
    usersViewModel: UsersViewModel = viewModel(),
    context: Context
){
    // variable that stores fetched documents
    val tutorialsData = dataViewModel.stateTutorials.value
    // variable that stores user's profile
    val userProfile = usersViewModel.userStateProfile.value
    //-------------- GENERATING CARDS ----------------//
    tutorialsData.filter{ it.rutaNombre.equals(pathName)}.forEach {
        tutorial -> Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .heightIn(400.dp, 800.dp)
                .clip(MaterialTheme.shapes.medium),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
        ){
            // ---------- TITLE ----------//
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .wrapContentSize(Alignment.Center)
            ){
                Text(
                    text = tutorial.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp)

                AddDoneIcon(
                    userDoneTutorials = userProfile.completados,
                    tutorialName = tutorial.nombre)

            }

            // ---------- PREVIEW  ----------//
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .wrapContentSize(Alignment.Center)
            ){
                //TODO: implement Picasso or similar library to render the image
                Image(
                    painter = painterResource(id = R.mipmap.articon),
                    contentDescription = "preview ${tutorial.nombre}",
                    modifier = Modifier
                        .size(100.dp),
                        //.clip(MaterialTheme.shapes.extraLarge)
                    alignment = Alignment.Center)
            }

            // ---------- DESCRIPTION  ----------//
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            ){
                Text(
                    text = tutorial.descripcion,
                    textAlign = TextAlign.Center)
            }

            // ---------- DESCRIPTION  ----------//
            //TODO: Add rating bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            ){
                Text(
                    text = "calificacion: ${tutorial.calificacion}",
                    textAlign = TextAlign.Center)
            }

            // ---------- BUTTONS ------------ //
            Column {
                // ---------- BUTTON OPEN  ----------//
                Button(
                    onClick = {
                              openTutorial(
                                  tutorialModel = tutorial,
                                  context = context
                              )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp, 10.dp)
                ){
                    Text(
                        text = stringResource(id = R.string.abrir_tutorial))
                }

                // ---------- BUTTON FAVS  ----------//
                AddFavButton(
                    userProfile = userProfile,
                    tutorialsModel = tutorial)
            }
        }
    }
}

/**
 * determines whether the tutorial is completed or not based on the provided arguments
 */
@Composable
fun AddDoneIcon(userDoneTutorials:ArrayList<String>, tutorialName: String){
    // flag reference value
    var done = false
    // validating process
    for(elem in userDoneTutorials){
        if (elem.equals(tutorialName)){
            done = true
        }
    }
    // creating icon based on flag state
    if(done){
        Icon(
            imageVector = Icons.Filled.Done,
            contentDescription = stringResource(id = R.string.tutorial_completado))
    }else{
        Icon(
            imageVector = Icons.Filled.Clear,
            contentDescription = stringResource(id = R.string.tutorial_no_completado))
    }
}


/**
 * determines whether the tutorial is already stored as "fav"
 * based on the given arguments, and displays the btn accordingly
 */
@Composable
fun AddFavButton(userProfile:UserModels, tutorialsModel: TutorialsModels){
    Log.i("tutorials", "first test, user ID: ${userProfile.id}")
    // boolean flag
    var favFlag = false
    // checking if the tutorial is saved as fav
    for (elem in userProfile.favoritos){
        if (elem.equals(tutorialsModel.id)){
            favFlag = true
        }
    }
    //creating button depending on flag state
    if (favFlag){
        // if the tutorial is already stored as fav can be deleted
        Button(
            onClick = {
                removeFavTutorial(tutorialsModel.id, userProfile)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp, 10.dp)
        ){
            Text(
                text = stringResource(id = R.string.eliminar_fav))
        }
    } else{
        // if not, the tutorial can be added to favs
        Button(
            onClick = {
                addFavTutorial(tutorialsModel.id, userProfile)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp, 10.dp)
        ){
            Text(
                text = stringResource(id = R.string.favoritos))
        }
    }

}

/**
 * updates the user profile with the new fav tutorial
 */
fun addFavTutorial(IDtutorial:String, userModel: UserModels){
    // instantiating FB
    val db = Firebase.firestore
    val documentID = userModel.id
    Log.i("tutorials", "userID: $documentID")
    // document reference
    val userDocument = db.collection("usuarios").document(documentID)
    // update request on document
    userDocument
        .update("favoritos", FieldValue.arrayUnion(IDtutorial))
        .addOnCompleteListener { documentTask ->
            if(documentTask.isSuccessful){
               Log.i("tutorials", "tutorial added!")
            }
        }
        .addOnFailureListener { exception ->
            Log.e("tutorials", "error trying to update tutorial", exception)
        }
}


/**
 * removes the tutorial from the user's profile
 */
fun removeFavTutorial(IDtutorial:String, userModel: UserModels){
    // instantiating FB
    val db = Firebase.firestore
    val documentID = userModel.id
    // document reference
    val userDocument = db.collection("usuarios").document(documentID)
    // update request on document
    userDocument
        .update("favoritos", FieldValue.arrayRemove(IDtutorial))
        .addOnCompleteListener { documentTask ->
            if (documentTask.isSuccessful){
                Log.i("tutorials", "tutorial removed!")
            }
        }
        .addOnFailureListener { exception ->
            Log.e("tutorials", "error while removing tutorial", exception)
        }
}

/**
 * creates intents that redirects user to a new activity where the content is displayed
 */
fun openTutorial(tutorialModel: TutorialsModels, context: Context){
    // defining intent
    val intentTutorial = Intent(context, TutorialsContentActivity::class.java)
    // adding serializable object
    intentTutorial.putExtra("TUTORIAL_DATA", tutorialModel)
    // initializing intent
    context.startActivity(intentTutorial)
}



