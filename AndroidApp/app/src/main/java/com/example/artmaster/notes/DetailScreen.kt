package com.example.artmaster.notes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.artmaster.MainActivity
import com.example.artmaster.alarm.AlarmItem
import com.example.artmaster.alarm.AndroidAlarmScheluder
import com.example.artmaster.ui.theme.ArtMasterTheme
import kotlinx.coroutines.launch
import java.time.LocalDateTime

/**
 * Activity for handling the details screen.
 * Displays the UI for adding, editing, and updating notes.
 */
class DetailActivity: MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val noteId = intent.getStringExtra("noteId")
        val schedule = AndroidAlarmScheluder(this)
        setContent {
            ArtMasterTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    DetailScreen(detailViewModel = DetailViewModel(), noteId = noteId ?: "") {
                        Intent(applicationContext, NoteActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    @Composable
    fun DetailScreen(
        detailViewModel: DetailViewModel?,
        noteId: String,
        onNavigate:() -> Unit
    ) {
        val context = LocalContext.current
        // Retrieve the UI state from the ViewModel or use a default state
        val detailUiState = detailViewModel?.detailUiState ?: DetailUiState()
        // Determine if the forms are not blank
        val isFormsNotBlank = detailUiState.content.isNotBlank() &&
                detailUiState.title.isNotBlank()
        // Determine if the note ID is not blank
        val isNoteIdNotBlank = noteId.isNotBlank()
        // Determine the appropriate icon based on the state
        val icon = if (isNoteIdNotBlank) Icons.Default.Refresh else Icons.Default.Check

        val scheduler = AndroidAlarmScheluder(context)
        var alarmItem: AlarmItem? = null

        var selectedHour by remember { mutableStateOf(0) }
        var selectedMinute by remember { mutableStateOf(0) }
        var showTimePickerDialog by remember { mutableStateOf(false) }


        // Perform actions when the composition is launched
        LaunchedEffect(key1 = Unit) {
            // If forms are not blank, get the note; otherwise, reset the state
            if (noteId.isNotBlank()) {
                detailViewModel?.getNote(noteId)
            }else {
                detailViewModel?.resetState()
            }
        }

        // Coroutine scope for managing coroutines
        val scope = rememberCoroutineScope()

        // Scaffold state for managing the scaffold (app bar, snackbar, etc.)
        val scaffoldState = rememberScaffoldState()

        var selectedTimeInSeconds = 0

        // Main UI composition using Jetpack Compose
        Scaffold(
            scaffoldState = scaffoldState,
            floatingActionButton = {
                AnimatedVisibility(visible = isFormsNotBlank) {
                    // Show the FloatingActionButton when forms are not blank
                    FloatingActionButton(
                        onClick = {
                            // If the note ID is not blank, update the note; otherwise, add a new note
                            if (isNoteIdNotBlank) {
                                detailViewModel?.updateNote(noteId)
                            }else {
                                detailViewModel?.addNote()
                            }
                        },
                        backgroundColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(imageVector = icon, contentDescription = null)
                    }
                }
            }
        ) { padding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(color = MaterialTheme.colorScheme.onPrimary)
            ) {
                // Show a snackbar for a successfully added note
                if (detailUiState.noteAddedStatus) {
                    scope.launch {
                        scaffoldState.snackbarHostState
                            .showSnackbar("Nota agregada exitosamente")
                        detailViewModel?.resetNoteAddedStatus()
                        onNavigate.invoke()
                    }
                }

                // Show a snackbar for a successfully updated note
                if (detailUiState.updateNoteStatus) {
                    scope.launch {
                        scaffoldState.snackbarHostState
                            .showSnackbar("Nota editada exitosamente")
                        detailViewModel?.resetNoteAddedStatus()
                        onNavigate.invoke()
                    }
                }

                // Input field for the note title
                CustomOutlinedTextField(
                    value = detailUiState.title,
                    onValueChange = { detailViewModel?.onTitleChange(it) },
                    label = { Text(text = "Titulo") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                // Input field for the note content
                CustomOutlinedTextField(
                    value = detailUiState.content,
                    onValueChange = { detailViewModel?.onContentChange(it) },
                    label = { Text(text = "Contenido") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .weight(1f)
                )

                if (isNoteIdNotBlank) {
                    // Time picker dialog
                    if (showTimePickerDialog) {
                        TimePickerDialog(
                            onDismissRequest = {
                                showTimePickerDialog = false
                            },
                            onTimeSelected = { hour, minute ->
                                // Handle the selected time
                                selectedHour = hour
                                selectedMinute = minute
                                // Do something with the selected time
                                // For example, convert it to seconds
                                selectedTimeInSeconds = (hour * 3600) + (minute * 60)
                                Log.d("selectedTimeInSeconds", "DetailScreen: selectedTimeInSeconds: $selectedTimeInSeconds")
                                // ... rest of your code
                                showTimePickerDialog = false
                            }
                        )
                    }

                    // Button to show the time picker dialog
                    Button(
                        onClick = {
                            showTimePickerDialog = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Select Time")
                    }

                    IconButton(onClick = {
                         alarmItem = AlarmItem(
                             time = LocalDateTime.now()
                                 .plusSeconds(10),
                             title = detailUiState.title,
                             content = detailUiState.content
                         )
                        alarmItem?.let(scheduler::schedule)

                        scope.launch {
                            scaffoldState.snackbarHostState
                                .showSnackbar("Se agrego recordatorio")
                        }
                    }, modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Agregar Recordatorio",
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    IconButton(onClick = {
                        alarmItem?.let(scheduler::cancel)

                        scope.launch {
                            scaffoldState.snackbarHostState
                                .showSnackbar("Se cancelo recordatorio")
                        }
                    }, modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.Red
                            )
                            Text(
                                text = "Cancelar Recordatorio",
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                }
            }

        }



    }
}


@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedLabelColor = MaterialTheme.colorScheme.onBackground,
            unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
            cursorColor = MaterialTheme.colorScheme.onBackground,
            textColor = MaterialTheme.colorScheme.onBackground,
            backgroundColor = MaterialTheme.colorScheme.onPrimary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(25.dp)
    )
}


@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onTimeSelected: (Int, Int) -> Unit
) {
    var selectedHour by remember { mutableIntStateOf(0) }
    var selectedMinute by remember { mutableIntStateOf(0) }

    AlertDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        title = {
            Text("Select Time")
        },
        text = {
            // Time picker
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TimePicker(
                    modifier = Modifier.fillMaxWidth(),
                    hour = selectedHour,
                    minute = selectedMinute,
                    onTimeChanged = { hour, minute ->
                        selectedHour = hour
                        selectedMinute = minute
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onTimeSelected(selectedHour, selectedMinute)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    hour: Int,
    minute: Int,
    onTimeChanged: (Int, Int) -> Unit
) {
    Row(
        modifier = modifier
            .padding(16.dp)
    ) {
        // Hour picker
        NumberPicker(
            value = hour,
            onValueChange = {
                onTimeChanged(it, minute)
            },
            range = 0..23,
            label = "Hour"
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Minute picker
        NumberPicker(
            value = minute,
            onValueChange = {
                onTimeChanged(hour, it)
            },
            range = 0..59,
            label = "Minute"
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label)
        var textValue by remember { mutableStateOf(value.toString()) }
        var isEditing by remember { mutableStateOf(false) }
        var keyboardController by remember { mutableStateOf<SoftwareKeyboardController?>(null) }

        OutlinedTextField(
            value = textValue,
            onValueChange = {
                textValue = it
                isEditing = true
            },
            modifier = Modifier
                .width(60.dp)
                .clip(MaterialTheme.shapes.medium)
                .onFocusChanged {
                    if (it.isFocused) {
                        isEditing = true
                        keyboardController?.show()
                    } else {
                        isEditing = false
                    }
                },
            textStyle = MaterialTheme.typography.bodySmall,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            singleLine = true
        )

        DisposableEffect(isEditing) {
            if (!isEditing) {
                // Parse the textValue to an integer and ensure it is within the specified range
                val parsedValue = textValue.toIntOrNull() ?: 0
                val clampedValue = parsedValue.coerceIn(range)
                onValueChange(clampedValue)
            }
            onDispose { }
        }
    }
}
