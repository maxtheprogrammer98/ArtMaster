package com.example.artmaster.tutorials

import android.content.Context
import android.provider.Telephony.Mms.Rate
import android.widget.RatingBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.artmaster.R
import com.google.android.material.color.utilities.Score

/**
 * create cards that display the different tutorials that belong to the selected path
 * @param dataViewModel
 * it referes to the viewModel "TutorialsViewModel" that fetches data from firestore
 * @param context
 * from where the function will be executed
 */
@Composable
fun CreateCards(dataViewModel: TutorialsViewModel = viewModel(), context:Context, pathID:String){
    // --------------- BASE ARRAY -----------------------//
    val tutorialsPath = dataViewModel.tutorialsState.value
    // --------------- GENERATING CARDS  -----------------//
    tutorialsPath.filter{it.rutaID.equals(pathID)}.forEach{
        tutorial -> Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clip(MaterialTheme.shapes.medium)
                .shadow(3.dp, CircleShape, true, Color.DarkGray, Color.DarkGray)
        ){
            // ---------------- IMAGE -----------------//
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(80.dp, 120.dp)
                    .wrapContentSize(Alignment.Center)
            ){
                Image(
                    painter = painterResource(id = R.mipmap.articon),
                    contentDescription = "imagen de $tutorial.nombre")
            }

            // ---------------- MAIN CONTAINER -----------------//
            Row(
                modifier = Modifier
                    .fillMaxSize()
            ){
                // ---------------- TITLE -----------------//
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .wrapContentSize(Alignment.Center)
                ){
                    Text(
                        text = tutorial.nombre,
                        color = Color.White,
                        fontWeight = FontWeight.Bold)
                }

                // ---------------- SCORE -----------------//
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .wrapContentSize()
                ){
                    //TODO: add star rating later on
                    Text(
                        text = "puntaje: 0",
                        fontWeight = FontWeight.Bold)
                }
                // ---------------- BTNS (open and fav) -----------------//
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Button(
                        onClick = { /*TODO: redirect to tutorial*/ }
                    ){
                        Text(text = stringResource(id = R.string.abrir_tutorial))
                    }

                    Button(onClick = { /*TODO: add to favs*/ }
                    ){
                        Text(text = stringResource(id = R.string.favoritos))
                    }
                }
            }
        }
    }
}