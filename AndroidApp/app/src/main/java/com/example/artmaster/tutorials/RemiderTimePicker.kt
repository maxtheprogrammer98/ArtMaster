package com.example.artmaster.tutorials

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material.icons.twotone.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.artmaster.R

@Composable
fun ReminderSetting(){
    // stores the selected time / date
    var selectedTime by remember { mutableStateOf("")}
    var selectedDate by remember { mutableStateOf("") }
    // flags
    var flagTime by remember { mutableStateOf(false)}
    var flagDate by remember { mutableStateOf(false)}

    // adding graphic elements
    Column(
        modifier = Modifier.padding(16.dp)
    ){
        // indicative text
        Text(
            text = stringResource(id = R.string.seleccionar_hora_reminder),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center)

        // date field
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {selectedDate = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clip(CircleShape),
            trailingIcon = {
                IconButton(
                    // clears data
                    onClick = { selectedDate = "" }
                ){
                    Icon(
                        imageVector = Icons.TwoTone.Clear,
                        contentDescription = stringResource(id = R.string.borrar))
                }
            },
            leadingIcon = {
                IconButton(
                    // turns on flag
                    onClick = { flagDate = toggleFunction(flagDate)}
                ){
                    Icon(
                        imageVector = Icons.TwoTone.Done,
                        contentDescription = stringResource(id = R.string.aceptar))
                }
            },
            placeholder = {
                Text(text = "Ingresa la fecha (DD/MM/AAAA)")
            }
        )

        // time field
        OutlinedTextField(
            value = selectedTime,
            onValueChange = {selectedTime = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clip(CircleShape),
            trailingIcon = {
                IconButton(
                    // clears data
                    onClick = { selectedTime = "" }
                ){
                    Icon(
                        imageVector = Icons.TwoTone.Clear,
                        contentDescription = stringResource(id = R.string.borrar))
                }
            },
            leadingIcon = {
                IconButton(
                    // turns on flag
                    onClick = { flagDate = toggleFunction(flagDate)}
                ){
                    Icon(
                        imageVector = Icons.TwoTone.Done,
                        contentDescription = stringResource(id = R.string.aceptar))
                }
            },
            placeholder = {
                Text(text = "Ingresa la hora especifica (HH/MM)")
            }
        )

    }
}


fun toggleFunction(flag:Boolean) : Boolean{
    return flag.not()
}