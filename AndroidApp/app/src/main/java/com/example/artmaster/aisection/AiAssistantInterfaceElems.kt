package com.example.artmaster.aisection

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material.icons.twotone.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.artmaster.R
import com.google.ai.client.generativeai.GenerativeModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputFieldAIhelp(
    viewModelAIsection: AiAssistantViewModel = viewModel(),
    model : GenerativeModel){
    // variable that stores the input
    var inputUser by remember {
        mutableStateOf("")
    }

    //TextField
    OutlinedTextField(
        value = inputUser,
        onValueChange = {inputUser = it},
        modifier = Modifier.padding(20.dp),
        placeholder = {
            Text(text = stringResource(id = R.string.ai_help))
        },
        shape = CircleShape,
        trailingIcon = {
            IconButton(
                // removes text
                onClick = {inputUser = ""}
            ){
                Icon(
                    imageVector = Icons.TwoTone.Clear,
                    contentDescription = stringResource(id = R.string.borrar))
            }
        },
        leadingIcon = {
            IconButton(onClick = {
                // calling the function that updates the viewModel
                viewModelAIsection.getHelpClick(
                    model = model,
                    input = inputUser
                )
            }){
                Icon(
                    imageVector = Icons.TwoTone.Done,
                    contentDescription = stringResource(id = R.string.aceptar))
            }
        })
}


@Composable
fun DisplayAIresponse(viewModelAIsection: AiAssistantViewModel = viewModel()){
    // reference variable
    val aiResponse = viewModelAIsection.stateContentResponse.value

    //text
    Text(text = aiResponse)
}