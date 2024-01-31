package com.example.artmaster.tutorials

import android.widget.DatePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.sql.Time
import java.util.Date
import com.example.artmaster.R

@Composable
fun ReminderScreen(){
    // stores the selected time / date
    var selectedTime by remember { mutableStateOf<Date?>(null)}
    var selectedDate by remember { mutableStateOf<Time?>(null) }

    // adding graphic elements
    Column(
        modifier = Modifier.padding(16.dp)
    ){
        // indicative text
        Text(
            text = stringResource(id = R.string.seleccionar_hora_reminder),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center)

        // data picker

    }
}