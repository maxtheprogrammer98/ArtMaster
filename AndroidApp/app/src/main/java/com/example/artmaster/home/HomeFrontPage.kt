package com.example.artmaster.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.artmaster.R

@Composable
fun FrontPage(){
    // scrolling variable option
    val scrolling = rememberScrollState()

    // ----------- WRAPPER -------------//
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .horizontalScroll(scrolling),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        // ----------- FRONT PAGE IMAGE -------------//
        Image(
            painter = painterResource(id = R.mipmap.fullportada),
            contentDescription = stringResource(id = R.string.imagen) )
    }
}


@Composable
fun Header(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 20.dp)
    ){
        Image(
            painter = painterResource(id = R.mipmap.header),
            contentDescription = stringResource(id = R.string.header),
            modifier = Modifier.fillMaxWidth())
    }
}