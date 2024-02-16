package com.example.artmaster.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.artmaster.MainActivity
import com.example.artmaster.R
import com.example.artmaster.login.Login
import com.example.artmaster.ui.theme.ArtMasterTheme
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
        val showDialog = remember { mutableStateOf(false) }
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()

        val user = dataViewModel.state.value

        val scrollState = rememberScrollState()

        var multiFloatingState by remember {
            mutableStateOf(MultiFloatingState.Collapse)
        }

        val items = listOf(
            MinFabItem(
                icon = ImageBitmap.imageResource(id = R.drawable.ic_profile_picture),
                label = "Agregar foto de perfil",
                identifier = Identifier.Photo.name
            ),
            MinFabItem(
                icon = ImageBitmap.imageResource(id = R.drawable.ic_add_drawing),
                label = "Agregar dibujo",
                identifier = Identifier.Drawing.name
            ),
            MinFabItem(
                icon = ImageBitmap.imageResource(id = R.drawable.ic_change_password),
                label = "Cambiar contrasenia",
                identifier = Identifier.Password.name
            ),
        )

        fun showCustomDialog() {
            showDialog.value = true
        }


        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .heightIn(max = 800.dp),
            scaffoldState = scaffoldState,
            floatingActionButton = {
                MultiFloatingButton(
                    multiFloatingState = multiFloatingState,
                    onMultiFabStateChange = {
                        multiFloatingState = it
                    },
                    items = items,
                    context = context,
                    dataViewModel = dataViewModel,
                    showCustomDialog = ::showCustomDialog
                )
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
                                Icon(imageVector = Icons.Default.List, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
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
                                Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Favoritos")
                            }
                        }
                    }

                    if (showDialog.value) {
                        ChangePasswordDialog(
                            onConfirmClicked = { currentPassword, newPassword ->
                                // Enforce specific password criteria
                                val passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#\$%^&*()])(?=\\S+\$).{8,}\$".toRegex()
                                if (newPassword.matches(passwordRegex)) {
                                    // Reauthenticate the user with their current password
                                    if (currentPassword.isNotBlank()){
                                        val userProfile = Firebase.auth.currentUser
                                        val credential = EmailAuthProvider.getCredential(userProfile?.email!!, currentPassword)

                                        userProfile.reauthenticate(credential)
                                            .addOnSuccessListener {
                                                // Change the user's password
                                                userProfile.updatePassword(newPassword)
                                                    .addOnSuccessListener {
                                                        // Password changed successfully
                                                        showDialog.value = false
                                                        Toast.makeText(context, "Contrasena actualizada", Toast.LENGTH_SHORT).show()
                                                    }
                                                    .addOnFailureListener { exception ->
                                                        // Handle password change errors
                                                        Toast.makeText(context, "Fallo al cambiar la contrasena: ${exception.message}", Toast.LENGTH_SHORT).show()
                                                    }
                                            }
                                            .addOnFailureListener { exception ->
                                                // Handle reauthentication errors
                                                Toast.makeText(context, "Fallo en reautenticar: ${exception.message}", Toast.LENGTH_LONG).show()
                                            }
                                    } else {
                                        Toast.makeText(context, "Su contrasena actual debe ser valida", Toast.LENGTH_LONG).show()
                                    }
                                } else {
                                    // Notify the user that the new password does not meet the criteria
                                    Toast.makeText(context, "La contrasena debe tener al menos 8 caracteres e incluir una letra mayuscula, un numero, y un caracter especial", Toast.LENGTH_LONG).show()
                                }
                            },
                            onDismissClicked = {
                                showDialog.value = false
                            }
                        )

                    }

                    if (user.drawingArray.isNotEmpty()) {
                        ImageLayoutView(selectedImages = user.drawingArray.map { Uri.parse(it) },onDeleteDrawing = { uriToDelete ->
                            dataViewModel.deleteUserDrawing(uriToDelete.toString())
                        })
                    } else {
                        Spacer(modifier = Modifier.fillMaxSize())
                    }


                }

        }
    }

}