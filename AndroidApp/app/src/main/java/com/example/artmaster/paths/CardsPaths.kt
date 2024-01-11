package com.example.artmaster.paths

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
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
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.artmaster.R
import com.example.artmaster.tutorials.TutorialsPreviewActivity

/**
 * generates cards dynamically from fetched data from Firestore
 */
@SuppressLint("MutableCollectionMutableState")
@Composable
fun CreateCards(dataViewModel: PathsViewModel = viewModel(), context: Context){
    //--------------- ID PATH ------------------------//
//    var pathID by remember {
//        mutableStateOf("")
//    }
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

        // ----------- IMAGE ---------//
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ){
           AsyncImage(
               model = ImageRequest.Builder(LocalContext.current)
                   //TODO: upload real images in FS
                   .data("https://loremflickr.com/400/400/art?lock=1")
                   .crossfade(true)
                   .build(),
               contentDescription = stringResource(id = R.string.imagen),
               contentScale = ContentScale.Crop,
               modifier = Modifier.fillMaxSize()
           )
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

        CustomLinearProgressBar(pathID = path.id)

        // ----------- BUTTON ---------//
        Button(
            onClick = {
                openTutorials(context, path.nombre, path.id)
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
fun openTutorials(context: Context, namePath : String, IDpath : String){
    val intent = Intent(context, TutorialsPreviewActivity::class.java)
    intent.putExtra("NAME_PATH" , namePath)
    intent.putExtra("ID_PATH", IDpath)
    context.startActivity(intent)
}