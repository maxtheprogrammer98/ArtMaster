package com.example.artmaster.adminPaths

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artmaster.MainActivity
import com.example.artmaster.ui.theme.ArtMasterTheme
import kotlinx.coroutines.launch
import kotlin.math.log

class PathsActivity: MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMasterTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    PathScreen(
                        pathViewModel = PathsViewModel(),
                        onPathClick = {pathID ->
                            // Start DetailActivity with the selected noteId
                            Log.d("pathID", "onPathScreen: id: $pathID")
                            Intent(applicationContext, DetailPathActivity::class.java).apply {
                                putExtra("pathID", pathID)  // Pass the noteId as an extra
                                startActivity(this)
                            }
                        },
                        navToDetailPathScreen = {
                            Intent(applicationContext, DetailPathActivity::class.java).also {
                                startActivity(it)
                            }
                        }
                    )
                }
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @OptIn(androidx.compose.animation.ExperimentalAnimationApi::class)
    @Composable
    fun PathScreen(
        pathViewModel: PathsViewModel?,
        onPathClick: (id: String) -> Unit,
        navToDetailPathScreen: () -> Unit,
    ) {
        Log.d("PathScreen", "Initializing PathScreen")
        // Retrieve the UI state from the ViewModel or use a default state
        val pathUiState = pathViewModel?.pathUiState ?: PathUiState()
        // State variables for managing the delete dialog
        var openDialog by remember {
            mutableStateOf(false)
        }
        var selectedPath: Paths? by remember {
            mutableStateOf(null)
        }

        // Coroutine scope for managing coroutines
        val scope = rememberCoroutineScope()
        // Scaffold state for managing the scaffold (app bar, snackbar, etc.)
        val scaffoldState = rememberScaffoldState()

        // State for managing the search query
        var searchQuery by remember { mutableStateOf("") }


        // Launch the effect to load path when the composition is first created
        LaunchedEffect(key1 = Unit) {
            Log.d("PathScreen", "LaunchedEffect: Loading paths")
            pathViewModel?.loadPaths()
        }


        // Main scaffold composition
        Scaffold(
            scaffoldState = scaffoldState,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navToDetailPathScreen.invoke() },
                    backgroundColor = MaterialTheme.colorScheme.onPrimary
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

            // Column to hold the main content
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(color = MaterialTheme.colorScheme.background)) {

                // Add a search bar
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar rutas", color = MaterialTheme.colorScheme.background.copy(alpha = 0.4f)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
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


                // Check the state of the paths list and display content accordingly
                when(val pathsList = pathUiState.pathList) {
                    is PathResources.Loading -> {
                        Log.d("PathScreen", "PathResources.Loading: ${pathUiState.pathList.data}")
                        // Show a loading indicator when notes are being loaded
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(align = Alignment.Center)
                        )
                    }

                    is PathResources.Success -> {
                        Log.d("PathScreen", "PathResources.Success: ${pathUiState.pathList.data}")
                        // Show the list of paths when successfully loaded
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(1),
                            contentPadding = PaddingValues(16.dp),

                            ) {
                            items(
                                filterPaths(pathsList.data ?: emptyList(), searchQuery)
                            ) { path ->
                                // Compose item for each note
                                PathItem(
                                    paths = path,
                                    onLongClick = {
                                        openDialog = true
                                        selectedPath = path
                                    },
                                ) {
                                    onPathClick.invoke(path.pathsID)
                                }

                            }
                        }
                        // Animated visibility for the delete dialog
                        AnimatedVisibility(visible = openDialog) {
                            // AlertDialog for confirming note deletion
                            AlertDialog(
                                onDismissRequest = {
                                    openDialog = false
                                },
                                title = { Text(text = "Eliminar ruta?") },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            selectedPath?.pathsID?.let {
                                                pathViewModel?.deletePath(it)
                                            }
                                            openDialog = false
                                            scope.launch {
                                                scaffoldState.snackbarHostState
                                                    .showSnackbar("Ruta eliminada exitosamente")
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
                    // Show an error message if loading path fails
                    else -> {
                        Text(
                            text = pathUiState
                                .pathList.throwable?.localizedMessage ?: "Error desconocido",
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                        Log.d("Firestore", pathUiState
                            .pathList.throwable?.localizedMessage ?: "Error desconocido")
                    }
                }
            }
        }

    }



}


// Composable function for rendering a single path item
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PathItem(
    paths: Paths,
    onLongClick: () -> Unit,
    onClick: () -> Unit,
) {
    Log.d("PathItem", "Path: $paths")
    // Card to represent a path item
    androidx.compose.material.Card(
        modifier = Modifier
            .combinedClickable(
                onLongClick = { onLongClick.invoke() },
                onClick = { onClick.invoke() }
            )
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colorScheme.primary
    ) {
        // Column to arrange content within the card
        Column {
            // Name of the path
            Text(
                text = paths.nombre,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                overflow = TextOverflow.Clip,
                modifier = Modifier
                    .padding(4.dp)
                    .padding(horizontal = 4.dp)
            )
            // Spacer for vertical separation
            Spacer(modifier = Modifier.size(4.dp))

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                Text(
                    text = paths.informacion,
                    fontFamily = FontFamily.Monospace,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(4.dp)
                        .padding(horizontal = 4.dp),
                    maxLines = 4,
                )
            }
            // Spacer for vertical separation
            Spacer(modifier = Modifier.padding(4.dp))

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                Text(
                    text = paths.dificultad,
                    fontFamily = FontFamily.Monospace,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
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


private fun filterPaths(paths: List<Paths>, query: String): List<Paths> {
    return paths.filter {
        it.nombre.contains(query, ignoreCase = true) || it.dificultad.contains(query, ignoreCase = true)
    }
}