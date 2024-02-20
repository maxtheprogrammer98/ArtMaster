package com.example.artmaster.favorites

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.artmaster.R
import com.example.artmaster.tutorials.TutorialsModels
import com.example.artmaster.user.UsersViewModel
import kotlin.math.absoluteValue


/**
 * creates swipable Cards based on the viewModel content
 */
@Composable
fun SwipableCard(
    tutorialModel : TutorialsModels,
    context : Context
){
    // --------------- CARD ELEMENT ---------------- //
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            // TODO: Add funtion to open tutorial from favs
            .clickable {},
        shape = RoundedCornerShape(15.dp),
        elevation = 4.dp
    ){
        // --------------- main wrapper ---------------- //
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(15.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            // ---------- image ------------- //
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(tutorialModel.imagen)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(id = R.string.imagen),
                    modifier = Modifier
                        .size(120.dp, 120.dp)
                        .padding(16.dp, 12.dp))

            // ---------- content ------------- //
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ){
                // title
                Text(
                    text = tutorialModel.nombre,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(0.dp,8.dp),
                    fontSize = 16.sp)

                // path
                Text(
                    text = "ruta: ${tutorialModel.nombre}")

                // spacer
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun SwipeFavsCards(
    viewModelFav: FavViewModel = viewModel(),
    viewModelUser : UsersViewModel = viewModel(),
    context: Context
){
    // ------------------ STATE VARIABLES -------------------- //

    // state fav VM
    val stateViewModelFavs = viewModelFav.tutorialsModels.value
    // state users VM
    val userViewModelState = viewModelUser.userStateProfile.value

    // cards width
    val cardWidth = 200.dp

    // ------------------ BOX MAIN CONTAINER -------------------- //
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ){
        // ------------------ CARDS -------------------- //
        if (stateViewModelFavs.isNotEmpty()){
            stateViewModelFavs.forEachIndexed { index, card ->
                // stores location
                val offsetX = remember(index){ mutableStateOf(0) }
                // creating card
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        // getting particular location
                        .offset { IntOffset(offsetX.value, 8 * index) }
                        // listening to gestures
                        .pointerInput(Unit){
                            detectDragGestures { change, dragAmount ->
                                change.consumeAllChanges()
                                offsetX.value += dragAmount.x.toInt()
                                // if removed
                                if (dragAmount.x.absoluteValue > cardWidth.toPx() / 2){
                                    // removing from DB
                                    viewModelFav.removeFromFavsDB(
                                        favID = card.id,
                                        userID = userViewModelState.id,
                                        context = context
                                    )
                                    // removing from VM
                                    viewModelFav.removeFromFavVM(
                                        tutorialID = card.id,
                                        context = context)
                                }
                            }
                        },
                    shape = RoundedCornerShape(8.dp),
                    elevation = 8.dp
                ){
                    // ------------------ CARDS CONTENT -------------------- //
                    // --------------- main wrapper ---------------- //
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(15.dp)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        // ---------- image ------------- //
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(card.imagen)
                                .crossfade(true)
                                .build(),
                            contentDescription = stringResource(id = R.string.imagen),
                            modifier = Modifier
                                .size(120.dp, 120.dp)
                                .padding(16.dp, 12.dp))

                        // ---------- content ------------- //
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ){
                            // title
                            Text(
                                text = card.nombre,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(0.dp,8.dp),
                                fontSize = 16.sp)

                            // path
                            Text(
                                text = "ruta: ${card.nombre}")

                            // spacer
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}