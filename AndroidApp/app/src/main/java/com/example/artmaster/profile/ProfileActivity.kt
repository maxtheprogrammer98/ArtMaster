package com.example.artmaster.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.artmaster.MainActivity
import com.example.artmaster.login.Login
import com.example.artmaster.ui.theme.ArtMasterTheme

class ProfileActivity: MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMasterTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ProfileScreen(dataViewModel = UserViewModel()) {
                        Intent(applicationContext, Login::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ProfileScreen(
        dataViewModel: UserViewModel = viewModel(),
        navigateToLogin: () -> Unit
    ) {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()

        val user = dataViewModel.state.value


        var selectedImage by remember { mutableStateOf<Uri?>(null) }

        var selectedImages by remember {
            mutableStateOf<List<Uri?>>(emptyList())
        }

        val profilePhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                selectedImage = uri
                // Update user's photo in ViewModel and Firebase
                uri?.let { dataViewModel.updateUserPhoto(it) }
                Toast.makeText(
                    context,
                    "Se actualizo tu foto de perfil.",
                    Toast.LENGTH_LONG
                ).show()
            }
        )

        val singleDrawingPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                selectedImages = listOf(uri)
                uri?.let { dataViewModel.updateUserDrawing(it) }

                Toast.makeText(
                    context,
                    "Se agregaro tu dibujo",
                    Toast.LENGTH_LONG
                ).show()
            }
        )


        fun launchDrawingPicker() {
            singleDrawingPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }


        fun launchPhotoProfilePicker() {
            profilePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        val scrollState = rememberScrollState()


        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .heightIn(max = 1000.dp),
            scaffoldState = scaffoldState,
            topBar = {
                // Custom top bar from the MainActivity
                super.TobBarMain()
            },
            bottomBar = {
                // Custom bottom bar from the MainActivity
                super.BottomBar()
            },


            ) { padding ->

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(padding)
                        .background(MaterialTheme.colorScheme.background),
                ) {
                    ProfileHeader(user, navigateToLogin)
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileInfoItem(Icons.Default.Person, "Nombre", user.name, true, onEditClick = {})
                    Spacer(modifier = Modifier.height(8.dp))
                    ProfileInfoItem(Icons.Default.Email, "Correo Electrónico", user.email, false)
                    Spacer(modifier = Modifier.height(32.dp))


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // First Card
                        Card(
                            onClick = {
                                Toast.makeText(context, "Redirecting to tutoriales", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(imageVector = Icons.Default.List, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Tutoriales")
                            }
                        }

                        // Second Card
                        Card(
                            onClick = {
                                Toast.makeText(context, "Redirecting to favorite", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(start = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Favoritos")
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Button to pick a new profile photo
                        Button(onClick = {
                            launchPhotoProfilePicker()
                        }) {
                            Text("Subir foto de perfil")
                        }

                        // Button to pick several photos
                        Button(onClick = {
                            launchDrawingPicker()
                        }) {
                            Text("Sube un dibujo")
                        }
                    }

                    ImageLayoutView(selectedImages = user.drawingArray.map { Uri.parse(it) })
                }

        }
    }

}