package com.example.artmaster.notes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
                    IconButton(onClick = {
                         alarmItem = AlarmItem(
                             time = LocalDateTime.now()
                                 .plusSeconds(10),
                             message = detailUiState.title
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
