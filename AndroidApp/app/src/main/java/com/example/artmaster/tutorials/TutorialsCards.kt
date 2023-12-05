package com.example.artmaster.tutorials

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.artmaster.R

//TODO: ADD path info as parameter? (possible solution)
/**
 * renders dynamically the tutorials cards based on the path selected
 */
@Composable
@SuppressLint("MutableCollectionMutableState")
//@Preview
fun GenerateCardsTutorials(dataViewModel: TutorialsViewModel = viewModel(), pathName:String){
    // variable that stores fetched documents
    val tutorialsData = dataViewModel.stateTutorials.value
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

                //TODO: add done / undone icon here!
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
                    onClick = { /*TODO: add function btn visit*/},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp,10.dp)
                ){
                    Text(
                        text = stringResource(id = R.string.abrir_tutorial))
                }

                // ---------- BUTTON FAVS  ----------//
                Button(
                    onClick = { /*TODO: add function btn visit*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp,10.dp)
                ){
                    Text(
                        text = stringResource(id = R.string.favoritos))
                }
            }
        }
    }
}