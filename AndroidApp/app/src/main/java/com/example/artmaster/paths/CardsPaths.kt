package com.example.artmaster.paths

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.artmaster.R
import com.example.artmaster.ui.theme.greenDarkish
import com.example.artmaster.ui.theme.greenMain

/**
 * generates cards dynamically from fetched data from Firestore
 */
@SuppressLint("MutableCollectionMutableState")
@Composable
fun CardsPaths(
    pathModel : PathsModels,
    navigateTo: () -> Unit){

    // ----------- MAIN CARD CONTAINER ---------//
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ){
        // ----------- IMAGE ---------//
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ){
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pathModel.imagen)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(id = R.string.imagen),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                error = painterResource(id = R.mipmap.artiaicon)
            )
        }

        // ----------- TITLE ---------//
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(greenMain)
                .wrapContentSize(Alignment.Center)
        ){
            Text(
                text = pathModel.nombre,
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
                text = pathModel.informacion,
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
                text = "Dificultad: ${pathModel.dificultad}",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold)
        }
        // ------ PROGRESS BAR -----------//

        CustomLinearProgressBar(pathID = pathModel.id)

        // ----------- BUTTON ---------//
        Button(
            onClick = {
                // starts intent that shows tutorials list
                navigateTo.invoke()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(greenDarkish, Color.White)){
            Text(
                text = stringResource(id = R.string.abrir_tutorial),
                fontSize = 16.sp)
        }
    }
}