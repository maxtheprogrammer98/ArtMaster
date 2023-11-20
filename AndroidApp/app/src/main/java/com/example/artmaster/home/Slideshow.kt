package com.example.artmaster.home

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.swipeable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.artmaster.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

/**
 * creates a slideshow based on the resource list provided
 */
@Composable
fun InsertSlideshow(imagesList : List<Int>, context : Context){
    // remember variable: stores the current image
    var currentImage by remember {
        mutableIntStateOf(0)
    }

    // toast messages
    val errorNextImage = Toast.makeText(
        context,
        "no hay mas imagenes para mostrar",
        Toast.LENGTH_SHORT
    )

    val errorPrevImage = Toast.makeText(
        context,
        "no hay imagenes previas",
        Toast.LENGTH_SHORT
    )

    // remember variable: stores animation state
    var blurstate by remember {
        mutableStateOf(0)
    }

    // ----------------- MAIN CONTAINER --------------------- //
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(Color.LightGray)
    ){
        // --------------- ARROW NEXT ---------------//
        Icon(
            imageVector = Icons.Filled.KeyboardArrowRight ,
            contentDescription = stringResource(id = R.string.flecha_siguiente),
            modifier = Modifier
                .zIndex(10f)
                .size(30.dp)
                .align(Alignment.CenterEnd)
                .clickable {
                    if (currentImage < imagesList.size - 1) {
                        currentImage++
                    } else {
                        errorNextImage.show()
                    }
                })

        // --------------- ARROW PREVIOUS ---------------//
        Icon(
            imageVector = Icons.Filled.KeyboardArrowLeft ,
            contentDescription = stringResource(id = R.string.flecha_siguiente),
            modifier = Modifier
                .zIndex(10f)
                .size(30.dp)
                .align(Alignment.CenterStart)
                .clickable {
                    if (currentImage > 0) {
                        currentImage--
                    } else {
                        errorPrevImage.show()
                    }
                })

        // --------------- SLIDESHOW IMAGE ---------------//

        Image(
            painter = painterResource(id = imagesList[currentImage]),
            contentDescription = stringResource(id = R.string.slideshow),
            modifier = Modifier
                .align(Alignment.Center)
                .height(500.dp)
                .blur(blurstate.dp))
    }
}