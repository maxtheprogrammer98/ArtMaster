package com.example.artmaster.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.artmaster.paths.PathsViewModel
import com.example.artmaster.profile.UserViewModel
import com.example.artmaster.user.UserModels
import com.example.artmaster.R

@Composable
fun FilterOptions(
    dataViewModel: FavViewModel = viewModel(),
    pathsViewModel: PathsViewModel = viewModel()
){

    // -------------- reference variables -------------------- //
    val paths = pathsViewModel.pathsModelsState.value
    val favs = dataViewModel.userFavs.value

    // ------------ reference text -------------//
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
            .padding(10.dp)
    ){
        Text(
            text = stringResource(id = R.string.filtrar_path),
            fontWeight = FontWeight.Bold)
    }

    // -------------------- BUTTONS ------------------//
    paths.forEach {
        path -> Button(
            onClick = {dataViewModel.filterPathOption(path.nombre)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .wrapContentSize(Alignment.Center)
        ) {
            Text(text = path.nombre)
        }
    }
}