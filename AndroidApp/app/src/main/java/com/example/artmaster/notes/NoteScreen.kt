package com.example.artmaster.notes

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.artmaster.MainActivity
import com.example.artmaster.ui.theme.ArtMasterTheme
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

class NoteActivity: MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMasterTheme {
                Surface(color = androidx.compose.material3.MaterialTheme.colorScheme.background) {
                    NoteScreen(
                        noteViewModel = null,
                        onNoteClick = {},
                        navToDetailScreen = { /*TODO*/ }
                    )
                }
            }
        }
    }

    @OptIn(androidx.compose.animation.ExperimentalAnimationApi::class)
    @Composable
    fun NoteScreen(
        noteViewModel: NoteViewModel?,
        onNoteClick: (id: String) -> Unit,
        navToDetailScreen: () -> Unit,
    ) {
        val noteUiState = noteViewModel?.noteUiState ?: NoteUiState()

        var openDialog by remember {
            mutableStateOf(false)
        }
        var selectedNote: Notes? by remember {
            mutableStateOf(null)
        }
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()

        LaunchedEffect(key1 = Unit) {
            noteViewModel?.loadNotes()
        }

        Scaffold(
            scaffoldState = scaffoldState,
            floatingActionButton = {
                FloatingActionButton(onClick = { navToDetailScreen.invoke() }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                    )
                }
            },
            topBar = {
                super.TobBarMain()
            },
            bottomBar = {
                super.BottomBar()
            }


        ) {padding ->
            Column(modifier = Modifier.padding(padding)) {
                when(noteUiState.notesList) {
                    is Resources.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(align = Alignment.Center)
                        )
                    }

                    is Resources.Success -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(1),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(
                                noteUiState.notesList.data ?: emptyList()
                            ) {note ->
                                NoteItem(notes = note, onLongClick = {
                                    openDialog = true
                                    selectedNote = note
                                },
                                ) {
                                    onNoteClick.invoke(note.documentId)
                                }

                            }
                        }
                        AnimatedVisibility(visible = openDialog) {
                            AlertDialog(
                                onDismissRequest = {
                                    openDialog = false
                                },
                                title = { Text(text = "Delete Note?") },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            selectedNote?.documentId?.let {
                                                noteViewModel?.deleteNote(it)
                                            }
                                            openDialog = false
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color.Red
                                        ),
                                    ) {
                                        Text(text = "Delete")
                                    }
                                },
                                dismissButton = {
                                    Button(onClick = { openDialog = false }) {
                                        Text(text = "Cancel")
                                    }
                                }
                            )


                        }

                    }

                    else -> {
                        Text(
                            text = noteUiState
                                .notesList.throwable?.localizedMessage ?: "Error desconocido",
                            color = Color.Red
                        )
                    }

                }
            }

        }

        LaunchedEffect(key1 = noteViewModel?.hasUser){
            if (noteViewModel?.hasUser == false){
                Log.d("Notes", "Notes error: No user login")
            }
        }

    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    notes: Notes,
    onLongClick:() -> Unit,
    onClick:() -> Unit,
) {
    Card(
        modifier = Modifier
            .combinedClickable(
                onLongClick = { onLongClick.invoke() },
                onClick = { onClick.invoke() }
            )
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column {
            Text(
                text = notes.title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                modifier = Modifier.padding(4.dp)
                )
            Spacer(modifier = Modifier.size(4.dp))
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                Text(
                    text = notes.content,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(4.dp),
                    maxLines = 4,
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                Text(
                    text = formatDate(notes.timestamp),
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.End),
                    maxLines = 4,
                )
            }
        }


    }

}

private fun formatDate(timestamp: Timestamp): String {
    val sdf = SimpleDateFormat("DD-mm-yy hh:mm", Locale.getDefault())
    return sdf.format(timestamp.toDate())
}