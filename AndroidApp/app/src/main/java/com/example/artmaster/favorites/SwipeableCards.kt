package com.example.artmaster.favorites

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.artmaster.R
import com.example.artmaster.tutorials.TutorialsModels
import kotlinx.coroutines.delay

/**
 * displays a card which the user can swipe to delete
 */
@ExperimentalMaterialApi
@Composable
fun DeleteBackground(
    swipeDismissState: DismissState
){
    // changing color depending on flag state
    val color = if (swipeDismissState.dismissDirection == DismissDirection.EndToStart){
        Color.Red
    } else Color.Transparent

    // container
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ){
        // icon
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "delete",
            tint = Color.White)
    }
}

/**
 * container for swipeable cards
 */
@ExperimentalMaterialApi
@Composable
fun SwipeToDeleteContainer(
    cardId : String,
    card: @Composable () -> Unit,
    onDelete: () -> Unit,
    animationDuration: Int = 500,
    //viewModel: FavViewModel = viewModel()
) {
    // Flag to track card removal
    var isRemoved by remember { mutableStateOf(false) }

    // State for swipe dismissal
    val state = rememberDismissState(
        confirmStateChange = { value ->
            if (value == DismissValue.DismissedToEnd) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )

    // Swipe-to-dismiss container
    SwipeToDismiss(
        state = state,
        background = { DeleteBackground(swipeDismissState = state) },
        dismissContent = { card },
    )

    // Trigger deletion after animation
    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong())
            onDelete()
        }
    }

    // Animated visibility
    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismiss(
            state = state,
            background = { DeleteBackground(swipeDismissState = state) },
            dismissContent = { card() }
        )
    }
}


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