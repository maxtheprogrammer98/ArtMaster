package com.example.artmaster.adminTutorials

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.artmaster.MainActivity
import com.example.artmaster.adminPaths.PathsViewModel
import com.example.artmaster.ui.theme.ArtMasterTheme
import kotlinx.coroutines.launch

class DetailTutorialActivity: MainActivity() {
    private val pathsViewModel by viewModels<PathsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tutorialID = intent.getStringExtra("tutorialID")
        setContent {
            ArtMasterTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    DetailTutorialScreen(
                        detailTutorialViewModel = DetailTutorialViewModel(),
                        tutorialID = tutorialID ?: "",
                        pathsViewModel = pathsViewModel
                    ) {
                        Intent(applicationContext, TutorialsActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }
                }
            }
        }
        pathsViewModel.loadPaths()
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    @Composable
    fun DetailTutorialScreen(
        detailTutorialViewModel: DetailTutorialViewModel?,
        tutorialID: String,
        pathsViewModel: PathsViewModel,
        onNavigate:() -> Unit
    ) {

        val context = LocalContext.current
        // Retrieve the UI state from the ViewModel or use a default state
        val detailTutorialUiState = detailTutorialViewModel?.detailTutorialUiState ?: DetailTutorialUiState()
        // Determine if the forms are not blank
        val isFormsNotBlank = detailTutorialUiState.nombre.isNotBlank() &&
                detailTutorialUiState.informacion.isNotBlank() && detailTutorialUiState.descripcion.isNotBlank()
        // Determine if the tutorial ID is not blank
        val isPathIDNotBlank = tutorialID.isNotBlank()
        // Determine the appropriate icon based on the state
        val icon = if (isPathIDNotBlank) Icons.Default.Refresh else Icons.Default.Check


        // Observe the names from PathsViewModel
        val rutasNombres by remember(pathsViewModel.pathUiState.pathNames.data) {
            val names = mutableStateOf(emptyList<String>())
            pathsViewModel.pathUiState.pathNames.data?.let { names.value = it }
            names
        }

        // Load paths when the activity is created or when the pathNames data changes
        LaunchedEffect(pathsViewModel.pathUiState.pathNames.data) {
            pathsViewModel.loadPaths()
        }

        Log.d("rutasNombres", "DetailTutorialScreen: $rutasNombres")

        // Perform actions when the composition is launched
        LaunchedEffect(key1 = Unit) {
            // If forms are not blank, get the tutorial; otherwise, reset the state
            if (tutorialID.isNotBlank()) {
                detailTutorialViewModel?.getTutorial(tutorialID)
            }else {
                detailTutorialViewModel?.resetState()
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
                            // If the tutorial ID is not blank, update the path; otherwise, add a new path
                            if (isPathIDNotBlank) {
                                detailTutorialViewModel?.updateTutorial(tutorialID)
                            }else {
                                detailTutorialViewModel?.addTutorial()
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
                // Show a snackbar for a successfully added tutorial
                if (detailTutorialUiState.tutorialAddedStatus) {
                    scope.launch {
                        scaffoldState.snackbarHostState
                            .showSnackbar("Tutorial agregada exitosamente")
                        detailTutorialViewModel?.resetTutorialAddedStatus()
                        onNavigate.invoke()
                    }
                }

                // Show a snackbar for a successfully updated tutorial
                if (detailTutorialUiState.updateTutorialStatus) {
                    scope.launch {
                        scaffoldState.snackbarHostState
                            .showSnackbar("Tutorial editada exitosamente")
                        detailTutorialViewModel?.resetTutorialAddedStatus()
                        onNavigate.invoke()
                    }
                }

                // Input field for the tutorial name
                CustomOutlinedTextField(
                    value = detailTutorialUiState.nombre,
                    onValueChange = { detailTutorialViewModel?.onNombreChange(it) },
                    label = { Text(
                        text = "Nombre",
                        fontFamily = FontFamily.Monospace,
                    ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                CustomOutlinedTextField(
                    value = detailTutorialUiState.descripcion,
                    onValueChange = { detailTutorialViewModel?.onDescripcionChange(it) },
                    label = { Text(
                        text = "Descripcion",
                        fontFamily = FontFamily.Monospace,
                    ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                Log.d("nombre tutorial", "DetailTutorialScreen: ${detailTutorialUiState.nombre}")

                // Input field for the tutorial information
                CustomOutlinedTextField(
                    value = detailTutorialUiState.informacion,
                    onValueChange = { detailTutorialViewModel?.onInformacionChange(it) },
                    label = { Text(
                        text = "Informacion",
                        fontFamily = FontFamily.Monospace,
                    ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .weight(1f)
                )


                // Input field for the path video
                CustomOutlinedTextField(
                    value = detailTutorialUiState.video,
                    onValueChange = { detailTutorialViewModel?.onVideoChange(it) },
                    label = { Text(
                        text = "Video URL",
                        fontFamily = FontFamily.Monospace,
                    ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                // Input field for the path URL image
                CustomOutlinedTextField(
                    value = detailTutorialUiState.imagen,
                    onValueChange = { detailTutorialViewModel?.onImagenChange(it) },
                    label = { Text(
                        text = "Imagen URL",
                        fontFamily = FontFamily.Monospace,
                    ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                // Input field for the path rating
                CustomOutlinedFloatField(
                    value = detailTutorialUiState.calificacion,
                    onValueChange = { detailTutorialViewModel?.onCalificacionChange(it) },
                    label = { Text(
                        text = "Calificacion",
                        fontFamily = FontFamily.Monospace,
                    ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )


                CustomRadioGroup(
                    selectedOption = detailTutorialUiState.rutaNombre,
                    options = rutasNombres,
                    onOptionSelected = { detailTutorialViewModel?.onRutaNombreChange(it) },
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


@Composable
fun CustomOutlinedFloatField(
    value: Float,
    onValueChange: (Float) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value.toString(),
        onValueChange = {
            val floatValue = it.toFloatOrNull() ?: 0f
            onValueChange.invoke(floatValue)
        },
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
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(25.dp)
    )
}


@Composable
fun CustomRadioGroup(
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        for (option in options) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onOptionSelected(option) }
            ) {
                RadioButton(
                    selected = option == selectedOption,
                    onClick = null, // null disables user interaction
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = option,
                    color = if (option == selectedOption) {
                        MaterialTheme.colorScheme.primary // Change this to your desired color
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    }
                )
            }
        }
    }
}
