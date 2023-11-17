package com.example.artmaster.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

interface AddingLoginHeader {
    @Composable
    fun InsertHeader(imageLogin : Painter, descriptionLogin : String){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .wrapContentSize(Alignment.BottomCenter)
        ){
            Image(
                painter = imageLogin,
                contentDescription = descriptionLogin,
                modifier = Modifier
                    .size(100.dp))
        }
    }

    @Composable
    fun InsertTitle(text : String){
        Text(
            text = text,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center))
    }
}