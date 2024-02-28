package com.example.artmaster.adminUsers

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.layout.ContentScale
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
import kotlinx.coroutines.launch


class UsersActivity: MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMasterTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    UserScreen(
                        userViewModel = UsersViewModel(),
                        onUserClick = {userID ->
                            // Start DetailActivity with the selected userID
                            Log.d("userID", "onUserScreen: id: $userID")
                            Intent(applicationContext, DetailUserActivity::class.java).apply {
                                putExtra("userID", userID)  // Pass the noteId as an extra
                                startActivity(this)
                            }
                        },
                        navToDetailUserScreen = {
                            Intent(applicationContext, DetailUserActivity::class.java).also {
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
    fun UserScreen(
        userViewModel: UsersViewModel?,
        onUserClick: (id: String) -> Unit,
        navToDetailUserScreen: () -> Unit,
    ) {
        Log.d("UserScreen", "Initializing PathScreen")
        // Retrieve the UI state from the ViewModel or use a default state
        val userUiState = userViewModel?.userUiState ?: UserUiState()
        // State variables for managing the delete dialog
        var openDialog by remember {
            mutableStateOf(false)
        }
        var selectedUser: Users? by remember {
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
            Log.d("UserScreen", "LaunchedEffect: Loading users")
            userViewModel?.loadUsers()
        }


        // Main scaffold composition
        Scaffold(
            scaffoldState = scaffoldState,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navToDetailUserScreen.invoke() },
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
                                "Buscar usuarios",
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


                    // Check the state of the users list and display content accordingly
                    when (val usersList = userUiState.userList) {
                        is UsersResources.Loading -> {
                            Log.d(
                                "UserScreen",
                                "UserResources.Loading: ${userUiState.userList.data}"
                            )
                            // Show a loading indicator when users are being loaded
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentSize(align = Alignment.Center)
                            )
                        }

                        is UsersResources.Success -> {
                            Log.d(
                                "UserScreen",
                                "UserResources.Success: ${userUiState.userList.data}"
                            )
                            // Show the list of users when successfully loaded
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(1),
                                contentPadding = PaddingValues(16.dp),

                                ) {
                                items(
                                    filterUsers(usersList.data ?: emptyList(), searchQuery)
                                ) { user ->
                                    // Compose item for each note
                                    UserItem(
                                        users = user,
                                        onLongClick = {
                                            openDialog = true
                                            selectedUser = user
                                        },
                                    ) {
                                        onUserClick.invoke(user.userID)
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
                                    title = { Text(text = "Eliminar usuario?") },
                                    confirmButton = {
                                        Button(
                                            onClick = {
                                                selectedUser?.userID?.let {
                                                    userViewModel?.deleteUser(it)
                                                }
                                                openDialog = false
                                                scope.launch {
                                                    scaffoldState.snackbarHostState
                                                        .showSnackbar("Usuario eliminada exitosamente")
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
                        // Show an error message if loading users fails
                        else -> {
                            Text(
                                text = userUiState
                                    .userList.throwable?.localizedMessage ?: "Error desconocido",
                                color = Color.Red,
                                modifier = Modifier.padding(16.dp)
                            )
                            Log.d(
                                "Firestore", userUiState
                                    .userList.throwable?.localizedMessage ?: "Error desconocido"
                            )
                        }
                    }
                }
            }

        }
    }


}


// Composable function for rendering a single path item
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserItem(
    users: Users,
    onLongClick: () -> Unit,
    onClick: () -> Unit,
) {
    Log.d("UserItem", "user: $users")
    // Card to represent a user item
    androidx.compose.material.Card(
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
            // Name of the user
            Text(
                text = users.name,
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

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                Text(
                    text = users.email,
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

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                Text(
                    text = if (users.isAdmin) "Administrador" else "Usuario",
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


private fun filterUsers(users: List<Users>, query: String): List<Users> {
    return users.filter {
        it.name.contains(query, ignoreCase = true) || it.email.contains(query, ignoreCase = true)
    }
}