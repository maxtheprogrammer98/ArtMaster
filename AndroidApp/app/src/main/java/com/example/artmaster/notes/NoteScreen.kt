@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class
)

package com.example.artmaster.notes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberDismissState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artmaster.MainActivity
import com.example.artmaster.R
import com.example.artmaster.ui.theme.ArtMasterTheme
import com.google.firebase.Timestamp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Activity for handling the notes screen.
 * Displays the UI for viewing and interacting with a list of notes.
 */
class NoteActivity: MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMasterTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    NoteScreen(
                        noteViewModel = NoteViewModel(),
                        onNoteClick = {noteId ->
                            // Start DetailActivity with the selected noteId
                            Log.d("noteID", "onNoteScreen: id: $noteId")
                            Intent(applicationContext, DetailActivity::class.java).apply {
                                putExtra("noteId", noteId)  // Pass the noteId as an extra
                                startActivity(this)
                            }
                        },
                        navToDetailScreen = {
                            Intent(applicationContext, DetailActivity::class.java).also {
                                startActivity(it)
                            }
                        }
                    )
                }
            }
        }
    }

    // Main UI composition for the notes screen
    @OptIn(androidx.compose.animation.ExperimentalAnimationApi::class)
    @Composable
    fun NoteScreen(
        noteViewModel: NoteViewModel?,
        onNoteClick: (id: String) -> Unit,
        navToDetailScreen: () -> Unit,
    ) {
        // Retrieve the UI state from the ViewModel or use a default state
        val noteUiState = noteViewModel?.noteUiState ?: NoteUiState()
        // State variables for managing the delete dialog
        var openDialog by remember {
            mutableStateOf(false)
        }
        var selectedNote: Notes? by remember {
            mutableStateOf(null)
        }
        // Coroutine scope for managing coroutines
        val scope = rememberCoroutineScope()
        // Scaffold state for managing the scaffold (app bar, snackbar, etc.)
        val scaffoldState = rememberScaffoldState()

        // State for managing the search query
        var searchQuery by remember { mutableStateOf("") }

        // Launch the effect to load notes when the composition is first created
        LaunchedEffect(key1 = Unit) {
            noteViewModel?.loadNotes()
        }

        // Main scaffold composition
        Scaffold(
            scaffoldState = scaffoldState,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navToDetailScreen.invoke() },
                    backgroundColor = MaterialTheme.colorScheme.background
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            topBar = {
                // Custom top bar from the MainActivity
                super.TobBarMain()
            },
            bottomBar = {
                // Custom bottom bar from the MainActivity
                super.BottomBar()
            },
        ) { padding ->
            // Box to hold the main content and background image
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // Add background image
                Image(
                    painter = painterResource(R.drawable.bg05),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )

                // Column to hold the main content
                Column(
                    modifier = Modifier.padding(padding),
                    verticalArrangement = Arrangement.Top
                ) {
                    // Add a search bar
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                "Buscar notas",
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { searchQuery = "" },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.background
                                    )
                                }
                            }
                        }
                    )

                    // Check the state of the notes list and display content accordingly
                    when (val notesList = noteUiState.notesList) {
                        is Resources.Loading -> {
                            // Show a loading indicator when notes are being loaded
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentSize(align = Alignment.Center)
                            )
                        }

                        is Resources.Success -> {
                            Log.d("NoteScreen", "Notes: ${noteUiState.notesList.data}")
                            // Show the list of notes when successfully loaded
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(1),
                                contentPadding = PaddingValues(16.dp),
                            ) {
                                items(
                                    filterNotes(notesList.data ?: emptyList(), searchQuery)
                                ) { note ->
                                    // Compose item for each note
                                    SwipeToDeleteNote(
                                        item = note,
                                        onDelete = { noteViewModel?.deleteNote(it) },
                                        content = {
                                            NoteItem(
                                                notes = note,
                                                onLongClick = {
                                                    openDialog = true
                                                    selectedNote = note
                                                },
                                            ) {
                                                onNoteClick.invoke(note.documentId)
                                            }
                                        }
                                    )
                                }
                            }
                            // Animated visibility for the delete dialog
                            AnimatedVisibility(visible = openDialog) {
                                // AlertDialog for confirming note deletion
                                AlertDialog(
                                    onDismissRequest = {
                                        openDialog = false
                                    },
                                    title = { Text(text = "Eliminar nota?") },
                                    confirmButton = {
                                        Button(
                                            onClick = {
                                                selectedNote?.documentId?.let {
                                                    noteViewModel?.deleteNote(it)
                                                }
                                                openDialog = false
                                                scope.launch {
                                                    scaffoldState.snackbarHostState
                                                        .showSnackbar("Nota eliminada exitosamente")
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                backgroundColor = Color.Red
                                            ),
                                        ) {
                                            Text(text = "Borrar")
                                        }
                                    },
                                    dismissButton = {
                                        Button(onClick = { openDialog = false }) {
                                            Text(text = "Cancelar")
                                        }
                                    }
                                )
                            }
                        }
                        // Show an error message if loading notes fails
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
        }
        // Launch effect to handle the case when there is no user logged in
        LaunchedEffect(key1 = noteViewModel?.hasUser) {
            if (noteViewModel?.hasUser == false) {
                Log.d("Notes", "Notes error: No user login")
            }
        }
    }


}

@Composable
fun <T> SwipeToDeleteNote(
    item: T,
    onDelete: suspend (String) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {

    val context = LocalContext.current
    var isRemoved by remember {
        mutableStateOf(false)
    }
    val state = rememberDismissState(
        confirmStateChange = { value ->
            if (value == DismissValue.DismissedToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong())
            onDelete((item as Notes).documentId)
            Toast.makeText(context, "Nota Eliminada...", Toast.LENGTH_SHORT).show()

        }
    }
    
    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismiss(
            state = state,
            background = {
                DeleteBackground(swipeDismissState = state)
            },
            dismissContent = { content(item) },
            directions = setOf(DismissDirection.EndToStart)
        )
    }

}

@Composable
fun DeleteBackground(
    swipeDismissState: DismissState
) {
    val color = if (swipeDismissState.dismissDirection == DismissDirection.EndToStart) {
        Color.Red
    } else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color, shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}

// Composable function for rendering a single note item
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    notes: Notes,
    onLongClick: () -> Unit,
    onClick: () -> Unit,
) {
    // Card to represent a note item
    Card(
        modifier = Modifier
            .combinedClickable(
                onLongClick = { onLongClick.invoke() },
                onClick = { onClick.invoke() }
            )
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colorScheme.background
    ) {
        // Column to arrange content within the card
        Column {
            // Title of the note
            Text(
                text = notes.title,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                overflow = TextOverflow.Clip,
                modifier = Modifier
                    .padding(4.dp)
                    .padding(horizontal = 4.dp)
            )
            // Spacer for vertical separation
            Spacer(modifier = Modifier.size(4.dp))
            // Content of the note with ellipsis for overflow
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                Text(
                    text = notes.content,
                    fontFamily = FontFamily.Monospace,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(4.dp)
                        .padding(horizontal = 4.dp),
                    maxLines = 4,
                )
            }
            // Spacer for vertical separation
            Spacer(modifier = Modifier.padding(4.dp))
            // Timestamp of the note with ellipsis for overflow
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                Text(
                    text = formatDate(notes.timestamp),
                    fontFamily = FontFamily.Monospace,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(4.dp)
                        .padding(end = 4.dp)
                        .align(Alignment.End),
                    maxLines = 4,
                )
            }
        }
    }
}


// Function to format a timestamp into a readable date-time string
private fun formatDate(timestamp: Timestamp): String {
    val sdf = SimpleDateFormat("dd-MM-yy ~ hh:mm", Locale.getDefault())
    return sdf.format(timestamp.toDate())
}


// Function to filter notes based on the search query
fun filterNotes(notes: List<Notes>, query: String): List<Notes> {
    return notes.filter {
        it.title.contains(query, ignoreCase = true) || it.content.contains(query, ignoreCase = true)
    }
}