package com.example.artmaster.favorites

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.artmaster.R
import com.example.artmaster.paths.PathsViewModel

@Composable
fun FilterOptions(
    dataViewModel: FavViewModel = viewModel(),
    pathsViewModel: PathsViewModel = viewModel(),
    context : Context
){

    // -------------- reference variables -------------------- //
    // viewmodels
    val paths = pathsViewModel.pathsModelsState.value
    val favs = dataViewModel.userFavs.value
    // toggle variable
    var flagFilterBtns by remember { mutableStateOf(false)}
    var iconType by remember { mutableStateOf(Icons.Filled.KeyboardArrowDown) }

    // -------------------- PATH FILTER ICON ------------------//
    IconButton(
        onClick = {
            flagFilterBtns = switchFlagState(flagFilterBtns);
            iconType = changeIcon(iconType)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp,12.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
    ){
        // ------------ wrapper -------------//
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            // ------------ reference text -------------//
            Text(
                text = stringResource(id = R.string.filtrar_path),
                fontWeight = FontWeight.Bold)

            // ------------ ICON -------------//
            Icon(
                imageVector = iconType,
                contentDescription = stringResource(id = R.string.filtrar_path),
                modifier = Modifier
                    .size(55.dp))
        }
    }

    // -------------------- PATHS OPTIONS BTNS ------------------//
    if (flagFilterBtns){
        if (paths.isNotEmpty()){
            // array is not empty
            paths.forEach {
                    path -> Button(
                onClick = {dataViewModel.filterPathOption(path.nombre)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .wrapContentSize(Alignment.Center)
            ){
                Text(text = path.nombre)
                }
            }
        } else {
            // displaying error
            Toast.makeText(
                context,
                "no se ha guardado ningun tutorial como favorito",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}

/**
 * changes the state of a boolean flag
 */
fun switchFlagState(flag : Boolean): Boolean{
    // changing value
    var flagState = !flag
    // returning value
    return flagState
}

/**
 * changes the icon of a dropdown option
 */
fun changeIcon(icon : ImageVector) : ImageVector{
    // reference variable
    var iconState = icon
    // changing icon
    if (iconState == Icons.Filled.KeyboardArrowDown){
        iconState = Icons.Filled.KeyboardArrowUp
    } else {
        iconState = Icons.Filled.KeyboardArrowDown
    }
    // returning value
    return iconState
}