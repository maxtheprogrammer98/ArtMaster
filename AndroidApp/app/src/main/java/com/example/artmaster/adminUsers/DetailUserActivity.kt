package com.example.artmaster.adminUsers

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.artmaster.MainActivity
import com.example.artmaster.ui.theme.ArtMasterTheme
import kotlinx.coroutines.launch


class DetailUserActivity: MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userID = intent.getStringExtra("userID")
        setContent {
            ArtMasterTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    DetailUserScreen(detailUserViewModel = DetailUserViewModel(), userID = userID ?: "") {
                        Intent(applicationContext, UsersActivity::class.java).also {
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
    fun DetailUserScreen(
        detailUserViewModel: DetailUserViewModel?,
        userID: String,
        onNavigate:() -> Unit
    ) {
        val context = LocalContext.current
        // Retrieve the UI state from the ViewModel or use a default state
        val detailUserUiState = detailUserViewModel?.detailUserUiState ?: DetailUserUiState()

        var password by remember { mutableStateOf("") }
        var isPasswordOpen by remember { mutableStateOf(false) }
        var isPasswordVisible by remember { mutableStateOf(false) }
        var isUsernameInvalid by remember { mutableStateOf(false) }
        var isEmailInvalid by remember { mutableStateOf(false) }
        var isPasswordInvalid by remember { mutableStateOf(false) }
        val passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}\$".toRegex()

        // Determine if the forms are not blank
        val isFormsNotBlank = detailUserUiState.name.isNotBlank() &&
                detailUserUiState.email.isNotBlank()
        // Determine if the user ID is not blank
        val isUserIDNotBlank = userID.isNotBlank()
        // Determine the appropriate icon based on the state
        val icon = if (isUserIDNotBlank) Icons.Default.Refresh else Icons.Default.Check



        // Perform actions when the composition is launched
        LaunchedEffect(key1 = Unit) {
            // If forms are not blank, get the user; otherwise, reset the state
            if (userID.isNotBlank()) {
                detailUserViewModel?.getUser(userID)
            }else {
                detailUserViewModel?.resetState()
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
                            // If the user ID is not blank, update the user; otherwise, add a new user
                            if (isUserIDNotBlank) {
                                detailUserViewModel?.updateUser(userID)
                            }else {
                                detailUserViewModel?.addUser()
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
                // Show a snackbar for a successfully added path
                if (detailUserUiState.userAddedStatus) {
                    scope.launch {
                        scaffoldState.snackbarHostState
                            .showSnackbar("Usuario agregado exitosamente")
                        detailUserViewModel?.resetUserAddedStatus()
                        onNavigate.invoke()
                    }
                }

                // Show a snackbar for a successfully updated user
                if (detailUserUiState.updateUserStatus) {
                    scope.launch {
                        scaffoldState.snackbarHostState
                            .showSnackbar("Usuario editado exitosamente")
                        detailUserViewModel?.resetUserAddedStatus()
                        onNavigate.invoke()
                    }
                }

                // Input field for the user name
                CustomOutlinedTextField(
                    value = detailUserUiState.name,
                    onValueChange = { detailUserViewModel?.onNameChange(it) },
                    label = { Text(
                        text = "Nombre",
                        fontFamily = FontFamily.Monospace,
                    ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                // Input field for the user email
                CustomOutlinedTextField(
                    value = detailUserUiState.email,
                    onValueChange = { detailUserViewModel?.onEmailChange(it) },
                    label = { Text(
                        text = "Email",
                        fontFamily = FontFamily.Monospace,
                    ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )


                // Input field for the path difficulty
                CustomOutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(
                        text = "Contrasena",
                        fontFamily = FontFamily.Monospace,
                    ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                // Input field for the path URL image
                CustomOutlinedTextField(
                    value = detailUserUiState.photoUrl,
                    onValueChange = { detailUserViewModel?.onPhotoUrlChange(it) },
                    label = { Text(
                        text = "Foto de perfil (URL)",
                        fontFamily = FontFamily.Monospace,
                    ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )


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