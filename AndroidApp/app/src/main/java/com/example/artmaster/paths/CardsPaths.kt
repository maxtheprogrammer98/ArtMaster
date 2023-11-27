package com.example.artmaster.paths

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.artmaster.R
import com.example.artmaster.tutorials.TutorialsActivity

/**
 * generates cards dynamically from fetched data from Firestore
 */
@SuppressLint("MutableCollectionMutableState")
@Composable
fun CreateCards(dataViewModel: PathsViewModel = viewModel(), context: Context){
    //--------------- BASE ARRAY ------------------------//
    val learningPaths = dataViewModel.state.value
    // ----------- PATH CARDS---------//
    learningPaths.forEach {
            path -> Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .shadow(5.dp, RectangleShape),
                elevation = CardDefaults.cardElevation(),
    ){
        //TODO: implement picasso to render images

        // ----------- IMAGE ---------//
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .wrapContentSize(Alignment.Center)
                .padding(15.dp)
        ){
            Image(
                painter = painterResource(id = R.mipmap.articon),
                contentDescription = "art icon",
                alignment = Alignment.Center)
        }

        // ----------- TITLE ---------//
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.DarkGray)
                .wrapContentSize(Alignment.Center)
        ){
            Text(
                text = path.nombre,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(15.dp),
                color = Color.White)
        }

        // ----------- INFORMATION  ---------//
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ){
            Text(
                text = path.informacion,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(10.dp, 0.dp)
                    .fillMaxWidth())
        }

        // ----------- DIFFICULTY ---------//
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
                .padding(15.dp)
        ){
            Text(
                text = "Dificultad: ${path.dificultad}",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold)
        }
        // ------ PROGRESS BAR -----------//

        //TODO: FIX PROBLEM WITH PROGRESS BAR
        //CreateProgressBar(tutorialsPath = path.tutoriales)

        // ----------- BUTTON ---------//
        Button(
            onClick = {
                openTutorials(
                    pathID = path.id,
                    context = context
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .padding(16.dp)){
            Text(
                text = stringResource(id = R.string.abrir_tutorial),
                fontSize = 16.sp)
            }
        }
    }
}

/**
 * Starts a new activity where the tutorials will be displayed
 * based on the ID path selected
 */
fun openTutorials(pathID:String, context: Context){
    val intent = Intent(context, TutorialsActivity::class.java)
    intent.putExtra("IDPATH", pathID)
    context.startActivity(intent)
}