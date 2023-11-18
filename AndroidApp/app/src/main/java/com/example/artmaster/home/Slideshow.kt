package com.example.artmaster.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.artmaster.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

/**
 * creates a slideshow based on the resource list provided
 */
@Composable
fun InsertSlideshow(imagesList : List<Int>, context : Context){
    //remember variable: stores the current image
    var currentImage by remember {
        mutableIntStateOf(0)
    }

    // ------------------------ SLIDESHOW CONTAINER ----------------------------//
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(0.dp, 60.dp, 0.dp, 10.dp)
            .wrapContentSize(Alignment.Center)
            .background(Color.Gray)
    ){
        // ------------------------ SLIDESHOW IMAGE ----------------------------//
        Image(
            painter = painterResource(id = imagesList[currentImage]),
            contentDescription = stringResource(id = R.string.slideshow),
            modifier = Modifier
                .height(300.dp))

        // ------------------------ PREVIOUS ICON ----------------------------//
        Icon(
            imageVector = Icons.Filled.KeyboardArrowLeft ,
            contentDescription = stringResource(id = R.string.flecha_anterior),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(30.dp)
                .clickable {
                    // switchs to previus image
                    if (currentImage>0){
                        currentImage = currentImage-1
                    } else {
                        Toast.makeText(
                            context,
                            "no hay imagenes previas para mostrar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })

        // ------------------------ NEXT  ICON ----------------------------//
        Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = stringResource(id = R.string.flecha_siguiente),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(30.dp)
                .clickable {
                    // switchs to next image
                    if (currentImage < imagesList.size-1){
                        currentImage += 1
                    } else {
                        Toast.makeText(
                            context,
                            "no hay imagenes siguientes para mostrar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
    }

}