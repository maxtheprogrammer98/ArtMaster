package com.example.artmaster.graphicElements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.artmaster.R

/**
 * Main application header
 */
@Composable
fun HeaderMain(){
    Image(
        painter = painterResource(id = R.mipmap.header),
        contentDescription = stringResource(id = R.string.header),
        modifier = Modifier
            .fillMaxWidth())
}