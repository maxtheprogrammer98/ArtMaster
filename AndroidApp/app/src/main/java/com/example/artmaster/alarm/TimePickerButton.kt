package com.example.artmaster.alarm

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.artmaster.ui.theme.ArtMasterTheme
import java.text.DateFormat
import java.util.*

class TimePickerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMasterTheme {
                SelectTimeButton()
            }
        }
    }
}

@Composable
fun SelectTimeButton() {
    var selectedTime by remember { mutableStateOf<Date?>(null) }
    val context = LocalContext.current
    val density = LocalDensity.current.density

    // Function to show the time picker dialog
    fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                selectedTime = calendar.time
            },
            currentHour,
            currentMinute,
            false
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Button to show the time picker
        Button(
            onClick = {
                showTimePicker()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Select Time")
        }

        // Display the selected time
        if (selectedTime != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Selected Time: ${DateFormat.getTimeInstance().format(selectedTime!!)}")
        }
    }
}
