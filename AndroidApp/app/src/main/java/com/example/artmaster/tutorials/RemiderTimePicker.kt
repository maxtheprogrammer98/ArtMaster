package com.example.artmaster.tutorials

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material.icons.twotone.KeyboardArrowDown
import androidx.compose.material.icons.twotone.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.artmaster.R


@Composable
fun ReminderSetting(){
    // main button (which triggers the function to show the settings)
    var showSettings by remember { mutableStateOf(false)}
    var iconMenuSettings by remember { mutableStateOf(Icons.TwoTone.KeyboardArrowDown)}
    var textSettings by remember { mutableStateOf("Click para agendar recordatorio!") }
    // stores the selected time / date
    var selectedTime by remember { mutableStateOf("")}
    var selectedDate by remember { mutableStateOf("")}
    // flag (it's true when the date and time field are both filled up)
    var flagDataTime by remember { mutableStateOf(false) }

    // show settings
    TextButton(
        onClick = {
            // changes flag's state
            showSettings = switchFlagState(showSettings)
            // changing text / icon
            if (showSettings) {
                textSettings = "click para cerrar ajustes"
                iconMenuSettings = Icons.TwoTone.KeyboardArrowUp
            } else {
                textSettings = "Click para agendar recordatorio!"
                iconMenuSettings = Icons.TwoTone.KeyboardArrowDown
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .wrapContentSize(Alignment.Center)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Text(
                text = textSettings,
                textAlign = TextAlign.Center)

            Icon(
                imageVector = iconMenuSettings,
                contentDescription = "icon menu")
        }
    }

    // time fields
    if (showSettings){
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
                    .padding(12.dp),
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
                placeholder = {
                    Text(text = "Ingresa la fecha (DD/MM/AAAA)")
                },
                shape = CircleShape
            )

            // time field
            OutlinedTextField(
                value = selectedTime,
                onValueChange = {selectedTime = it},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
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
                placeholder = {
                    Text(text = "Ingresa la hora especifica (HH/MM)")
                },
                shape = CircleShape
            )

            // ---------------------- DONE BTN -----------------------//
            Button(
                onClick = {
                    flagDataTime = validateFields(selectedDate,selectedTime)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ){
                Text(
                    text = "agregar recordatorio",
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}

private fun validateFields (fieldDate : String, fieldTime : String) : Boolean{
    // reference variable
    var isCompleted = false
    // validation
    if(fieldDate.isNotEmpty() && fieldTime.isNotEmpty()){
        //TODO: Implement some kind of regex to refine the validation
        isCompleted = true
    }
    //testing
    Log.i("reminder", "validation result: $isCompleted")
    // return statement
    return isCompleted
}

private fun switchFlagState(flag: Boolean) : Boolean{
    // reference variable
    var flagState = flag
    // changing value
    flagState != flag
    // return statement
    return flagState
}