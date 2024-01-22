package com.example.artmaster.favorites

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.artmaster.R
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavsSearchBar(
    viewModelFavs: FavViewModel = viewModel()
){
    // stores the input text from the search bar
    var searchBarInput by remember {
        mutableStateOf("ingresa el nombre del tutorial")
    }
    
    // adding search bar
    OutlinedTextField(
        value = searchBarInput,
        onValueChange = {searchBarInput = it},
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp,25.dp),
        placeholder = {
            Text(text = stringResource(id = R.string.barra_busqueda_fav))
        },
        leadingIcon = {
            IconButton(
                // calls the filter function
                onClick = {viewModelFavs.filterSearchBar(searchBarInput)}
            ){
                Icon(
                    imageVector = Icons.Filled.Search ,
                    contentDescription = stringResource(id = R.string.buscar_tutorial))
            }
        },
        trailingIcon = {
            // calls the filter function
            IconButton(
                onClick = {
                    // changing variable value
                    searchBarInput = ""
                    viewModelFavs.getFavsUser()
                }
            ){
                Icon(
                    imageVector = Icons.Filled.Clear ,
                    contentDescription = stringResource(id = R.string.eliminar_fav))
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        shape = CircleShape
    )
}