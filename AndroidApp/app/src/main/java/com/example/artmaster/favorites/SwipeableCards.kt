package com.example.artmaster.favorites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
 * @param content
 * DeleteBackground
 */
@ExperimentalMaterialApi
@Composable
fun <T> SwipeToDeleteContainer(
    item : T,
    onDelete : (T) -> Unit,
    animationDuration : Int = 500,
    content: @Composable (T) -> Unit
){
    // flag
    var isRemoved by remember { mutableStateOf(false)}
    // validating state and updating flag
    val state = rememberDismissState(
        confirmStateChange = { value ->
            if (value == DismissValue.DismissedToEnd){
                isRemoved = true
                true
            } else {
                false
            }

        }
    )

    // triggers swipe action
    SwipeToDismiss(
        state = state,
        background = {
            DeleteBackground(swipeDismissState = state)
        },
        dismissContent = {content(item)}
    )

    // finishing deleting process and updating viewModel / DB
    LaunchedEffect(key1 = isRemoved){
        if(isRemoved){
            delay(animationDuration.toLong())
            // TODO: configure it properly so it's deleted from the viewModel and the BD
            onDelete(item)
        }
    }

    // animaiton
    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ){
        SwipeToDismiss(
            state = state,
            background = {
                DeleteBackground(swipeDismissState = state)
            },
            dismissContent = {
                content(item)
            })
    }

}