package com.example.artmaster.favorites

import android.content.Context
import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.artmaster.R

@Composable
fun CardsFavs(
    favsViewModel: FavViewModel = viewModel(),
    context: Context
){
    // cards models (extracted from view model)
    val favsModel = favsViewModel.tutorialsModels.value

    // generating cards dynamically from models
    favsModel.forEach {
        // main layout
        tutorialModel -> Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(100.dp, 200.dp)
                .padding(10.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = CardDefaults.outlinedShape,
        ){
            // contains tutorial image
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(30.dp, 100.dp)
            ){
                // processing image with Coil library
                AsyncImage(
                    //TODO: Add real images in FS
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://loremflickr.com/600/600/art?lock=1")
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(id = R.string.imagen),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize())
            }

            // contains information
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .heightIn(100.dp, 200.dp)
            ){
                // title
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .wrapContentSize(Alignment.Center)
                ){
                    Text(
                        text = tutorialModel.nombre,
                        fontWeight = FontWeight.Bold)
                }
            }
        }

    }
}