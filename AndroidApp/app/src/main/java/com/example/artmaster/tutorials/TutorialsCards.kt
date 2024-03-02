package com.example.artmaster.tutorials

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.artmaster.R
import com.example.artmaster.ui.theme.greenDarkish
import com.example.artmaster.ui.theme.starIcon
import com.example.artmaster.user.UserModels
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * renders dynamically the tutorials cards based on the path selected
 */
@Composable
@SuppressLint("MutableCollectionMutableState")
fun CardsTutorials(
    tutorialModel: TutorialsModels,
    userModel: UserModels,
    navigateTo : () -> Unit,
){
    //-------------- GENERATING CARDS ----------------//
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clip(MaterialTheme.shapes.medium),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ){
        // ---------- TITLE ----------//
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .wrapContentSize(Alignment.Center)
        ){
            Text(
                text = tutorialModel.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp)

            }

            // ---------- PREVIEW IMAGE  ----------//
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(tutorialModel.imagen)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(id = R.string.imagen),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    error = painterResource(id = R.mipmap.artiaicon))
            }

            // ---------- DESCRIPTION  ----------//
            Column(
                modifier = Modifier
                    .padding(0.dp, 20.dp, 0.dp, 5.dp)
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            ){
                Text(
                    text = tutorialModel.descripcion,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(0.dp,10.dp))

                val estado = addDoneIcon(
                    userDoneTutorials = userModel.completados,
                    tutorialName = tutorialModel.id )

                Text(
                    text = "Estado: $estado",
                    textAlign = TextAlign.Center)

            }

            // ---------- RATING  ----------//
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            ){
                Text(
                    text = stringResource(id = R.string.calificacion_titulo),
                    textAlign = TextAlign.Center)
            }

            AddRatingIcons(tutorialID = tutorialModel.id)

            // ---------- BUTTONS ------------ //
            Column {
                // ---------- BUTTON OPEN  ----------//
                Button(
                    onClick = { navigateTo.invoke()},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp, 5.dp),

                    colors = ButtonDefaults.buttonColors(greenDarkish, Color.White)
                ){
                    Text(
                        text = stringResource(id = R.string.abrir_tutorial))
                }

                // ---------- BUTTON FAVS  ----------//
                AddFavButton(
                    userProfile = userModel,
                    tutorialsModel = tutorialModel,
                    context = LocalContext.current)

                // ---------- SPACER ----------//
                Spacer(modifier = Modifier.height(15.dp))
            }
    }
}

/**
 * determines whether the tutorial is completed or not based on the provided arguments
 */
@Composable
fun addDoneIcon(userDoneTutorials:ArrayList<String>, tutorialName: String):String{
    // flag reference value
    var done = false
    // base value
    var status = ""
    // validating process
    for(elem in userDoneTutorials){
        if (elem.equals(tutorialName)){
            done = true
        }
    }
    // creating icon based on flag state
    if(done){
        status = stringResource(id = R.string.completado) + "\u2713"
    }else{
       status = stringResource(id = R.string.incompleto)
    }

    //returning result
    return status
}


/**
 * determines whether the tutorial is already stored as "fav"
 * based on the given arguments, and displays the btn accordingly
 */
@Composable
fun AddFavButton(userProfile:UserModels, tutorialsModel: TutorialsModels, context: Context){
    Log.i("tutorials", "first test, user ID: ${userProfile.id}")
    // boolean flag
    var favFlag by remember {
        mutableStateOf(false)
    }

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
                removeFavTutorial(tutorialsModel.id, userProfile, context)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp, 5.dp)
        ){
            Icon(
                painter = painterResource(id = R.drawable.ic_delete) ,
                contentDescription = stringResource(id = R.string.favoritos),
                modifier = Modifier.padding(5.dp,0.dp))

            Text(
                text = stringResource(id = R.string.eliminar_fav))
        }
    } else{
        // if not, the tutorial can be added to favs
        Button(
            onClick = {
                addFavTutorial(tutorialsModel.id, userProfile, context)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp, 5.dp),

            colors = ButtonDefaults.buttonColors(greenDarkish, Color.White)
        ){
            Icon(
                painter = painterResource(id = R.drawable.ic_star_yellow) ,
                contentDescription = stringResource(id = R.string.favoritos),
                modifier = Modifier.padding(5.dp,0.dp))

            Text(
                text = stringResource(id = R.string.favoritos))
        }
    }

}

/**
 * updates the user profile with the new fav tutorial
 */
fun addFavTutorial(IDtutorial:String, userModel: UserModels, context: Context){
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
                // displaying notification
               Log.i("tutorials", "tutorial added!")
                Toast.makeText(
                    context,
                    "tutorial añadido a favs",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        .addOnFailureListener { exception ->
            Log.e("tutorials", "error trying to update tutorial", exception)
        }
}


/**
 * removes the tutorial from the user's profile
 */
fun removeFavTutorial(IDtutorial:String, userModel: UserModels,context: Context){
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
                //notifications
                Log.i("tutorials", "tutorial removed!")
                Toast.makeText(
                    context,
                    "el tutorial ha sido eliminado de favs, " +
                            "refresca la seccion para volver a añadirlo",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        .addOnFailureListener { exception ->
            Log.e("tutorials", "error while removing tutorial", exception)
        }
}

@Composable
fun AddRatingIcons(
    votosViewModel: VotosViewModel = viewModel(),
    tutorialID: String
){
    // seting filter value in VM
    val VM = votosViewModel.setTutorialID(tutorialID)
    // getting average score
    val averageScore = votosViewModel.calculateAverage()

    // creating wrapper element
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(5.dp)
            .wrapContentSize(Alignment.Center)
    ){
        // adding starts (based on the function result)
        for(elem in 0 until averageScore){
            Icon(
                painter = painterResource(id = R.drawable.ic_star_yellow),
                contentDescription = stringResource(id = R.string.estrella_icono),
                modifier = Modifier.size(40.dp),
                tint = starIcon)
        }
    }
}

