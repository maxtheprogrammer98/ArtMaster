package com.example.artmaster.favorites

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.example.artmaster.tutorials.TutorialsContentActivity
import com.example.artmaster.tutorials.TutorialsModels
import com.example.artmaster.tutorials.VotosViewModel
import com.example.artmaster.ui.theme.greenDarkish
import com.example.artmaster.ui.theme.starIcon
import com.example.artmaster.user.UsersViewModel


//remember variables
/**
 * renders dynamically the tutorials cards based on the path selected
 */
@Composable
@SuppressLint("MutableCollectionMutableState")
//@Preview
fun GenerateCardsFavs(
    dataViewModel: FavViewModel = viewModel(),
    usersViewModel: UsersViewModel = viewModel(),
    context: Context
){
    // variable that stores user's profile
    val userProfile = usersViewModel.userStateProfile.value
    // variable that stores the tutotrials model
    var tutorials = dataViewModel.tutorialsModels.value
    //-------------- GENERATING CARDS ----------------//

    tutorials.forEach {
            fav -> Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
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
                text = fav.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp)

        }

        // ---------- PREVIEW IMAGE  ----------//
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ){
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(fav.imagen)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(id = R.string.imagen),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize())
        }

        // ---------- DESCRIPTION  ----------//
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
        ){
            Text(
                text = fav.descripcion,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(0.dp,10.dp))

            val estado = addDoneIcon(
                userDoneTutorials = userProfile.completados,
                tutorialName = fav.id )

            Text(
                text = "estado: $estado",
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

        AddRatingBar(tutorialID = fav.id)

        // ---------- SPACER ------------ //
        Spacer(modifier = Modifier.height(12.dp))

        // ---------- BUTTONS ------------ //
        Column {
            // ---------- BUTTON OPEN  ----------//
            Button(
                // function
                onClick = {
                    openTutorial(
                        tutorialModel = fav,
                        context = context
                    )
                },
                // styles
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp, 0.dp),
                // colors
                colors = ButtonDefaults.buttonColors(greenDarkish, Color.White)
            ){
                Text(
                    text = stringResource(id = R.string.abrir_tutorial))
            }

            // ---------- SPACER ------------ //
            Spacer(modifier = Modifier.height(12.dp))

            // ---------- BUTTON FAVS  ----------//
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Button(
                    // styling
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp, 0.dp),
                    // functions
                    onClick = {
                        // removes tutorial from DB
                        dataViewModel.removeFromFavsDB(
                            favID = fav.id,
                            userID = usersViewModel.userStateProfile.value.id,
                            context = context);
                        // removes tutorial from VM
                        dataViewModel.removeFromFavVM(tutorialID = fav.id)
                        },
                    // colors
                    colors = ButtonDefaults.buttonColors(greenDarkish, Color.White)

                ){
                    Text(text = stringResource(id = R.string.eliminar_fav))
                }
            }

            // ---------- SPACER ------------ //
            Spacer(modifier = Modifier.height(15.dp))
            }
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

@Composable
fun AddRatingBar(
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
                tint = starIcon,
                modifier = Modifier.size(40.dp))
        }
    }
}