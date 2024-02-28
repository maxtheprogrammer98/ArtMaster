package com.example.artmaster.adminPaths

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.artmaster.MainActivity
import com.example.artmaster.ui.theme.ArtMasterTheme
import kotlinx.coroutines.launch

class DetailPathActivity: MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pathID = intent.getStringExtra("pathID")
        setContent {
            ArtMasterTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    DetailPathScreen(detailPathsViewModel = DetailPathsViewModel(), pathID = pathID ?: "") {
                        Intent(applicationContext, PathsActivity::class.java).also {
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
    fun DetailPathScreen(
        detailPathsViewModel: DetailPathsViewModel?,
        pathID: String,
        onNavigate:() -> Unit
    ) {
        val context = LocalContext.current
        // Retrieve the UI state from the ViewModel or use a default state
        val detailPathUiState = detailPathsViewModel?.detailPathUiState ?: DetailPathUiState()
        // Determine if the forms are not blank
        val isFormsNotBlank = detailPathUiState.nombre.isNotBlank() &&
                detailPathUiState.informacion.isNotBlank() && detailPathUiState.dificultad.isNotBlank()
        // Determine if the path ID is not blank
        val isPathIDNotBlank = pathID.isNotBlank()
        // Determine the appropriate icon based on the state
        val icon = if (isPathIDNotBlank) Icons.Default.Refresh else Icons.Default.Check



        // Perform actions when the composition is launched
        LaunchedEffect(key1 = Unit) {
            // If forms are not blank, get the note; otherwise, reset the state
            if (pathID.isNotBlank()) {
                detailPathsViewModel?.getPath(pathID)
            }else {
                detailPathsViewModel?.resetState()
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
                            // If the path ID is not blank, update the path; otherwise, add a new path
                            if (isPathIDNotBlank) {
                                detailPathsViewModel?.updatePath(pathID)
                            }else {
                                detailPathsViewModel?.addPath()
                            }
                        },
                        backgroundColor = MaterialTheme.colorScheme.background
                    ) {
                        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
                    }
                }
            }
        ) { padding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(color = MaterialTheme.colorScheme.background)
            ) {
                // Show a snackbar for a successfully added path
                if (detailPathUiState.pathAddedStatus) {
                    scope.launch {
                        scaffoldState.snackbarHostState
                            .showSnackbar("Ruta agregada exitosamente")
                        detailPathsViewModel?.resetPathAddedStatus()
                        onNavigate.invoke()
                    }
                }

                // Show a snackbar for a successfully updated path
                if (detailPathUiState.updatePathStatus) {
                    scope.launch {
                        scaffoldState.snackbarHostState
                            .showSnackbar("Ruta editada exitosamente")
                        detailPathsViewModel?.resetPathAddedStatus()
                        onNavigate.invoke()
                    }
                }

                // Input field for the path name
                CustomOutlinedTextField(
                    value = detailPathUiState.nombre,
                    onValueChange = { detailPathsViewModel?.onNombreChange(it) },
                    label = { Text(
                        text = "Nombre",
                        fontFamily = FontFamily.Monospace,
                    ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                Log.d("nombre path", "DetailPathScreen: ${detailPathUiState.nombre}")

                // Input field for the path information
                CustomOutlinedTextField(
                    value = detailPathUiState.informacion,
                    onValueChange = { detailPathsViewModel?.onInformacionChange(it) },
                    label = { Text(
                        text = "Informacion",
                        fontFamily = FontFamily.Monospace,
                    ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .weight(1f)
                )


                // Input field for the path difficulty
                CustomOutlinedTextField(
                    value = detailPathUiState.dificultad,
                    onValueChange = { detailPathsViewModel?.onDificultadChange(it) },
                    label = { Text(
                        text = "Dificultad",
                        fontFamily = FontFamily.Monospace,
                    ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                // Input field for the path URL image
                CustomOutlinedTextField(
                    value = detailPathUiState.imagen,
                    onValueChange = { detailPathsViewModel?.onImagenChange(it) },
                    label = { Text(
                        text = "Imagen URL",
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
            backgroundColor = MaterialTheme.colorScheme.background,
            focusedBorderColor = MaterialTheme.colorScheme.onBackground,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(25.dp)
    )
}