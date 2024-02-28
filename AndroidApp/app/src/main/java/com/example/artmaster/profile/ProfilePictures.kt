package com.example.artmaster.profile

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest


@Composable
fun ImageLayoutView(selectedImages: List<Uri?>, onDeleteDrawing: (Uri) -> Unit) {
    val context = LocalContext.current
    val showDeleteDialog = remember { mutableStateOf(false) }
    val selectedDrawingUrl = remember { mutableStateOf("") }

    fun showDeleteDialog(drawingUrl: String) {
        selectedDrawingUrl.value = drawingUrl
        showDeleteDialog.value = true
    }
    
    LazyVerticalGrid(
        columns = GridCells.Adaptive(128.dp),
        contentPadding = PaddingValues(
            start = 24.dp,
            top = 16.dp,
            end = 24.dp,
            bottom = 70.dp
        ),
        content = {
            items(selectedImages) {uri ->
                Card(
                    modifier = Modifier
                        .width(120.dp)
                        .height(160.dp)
                        .padding(4.dp)
                        .fillMaxWidth(),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(uri)
                                .crossfade(true)
                                .crossfade(1000)
                                .build(),
                            loading = {
                                LinearProgressIndicator()
                            },
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    uri?.let { showDeleteDialog(it.toString()) }
                                }
                                .clip(RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Crop
                        )
                        if (showDeleteDialog.value) {
                            DeleteDrawing(
                                onConfirmClicked = {
                                    selectedDrawingUrl.value.let { onDeleteDrawing(Uri.parse(it)) }
                                    Toast.makeText(
                                        context,
                                        "Tu dibujo fue eliminado",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    showDeleteDialog.value = false
                                },
                                onDismissClicked = {
                                    showDeleteDialog.value = false
                                },
                                selectedDrawingUrl.value
                            )
                        }
                    }

                }
            }
        }
    )

}


@Composable
fun DeleteDrawing(
    onConfirmClicked: () -> Unit,
    onDismissClicked: () -> Unit,
    selectedDrawingUrl: String
) {
    Dialog(
        onDismissRequest = onDismissClicked
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .size(400.dp)
                    .padding(8.dp)
                    .padding(top = 16.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(selectedDrawingUrl)
                        .crossfade(true)
                        .crossfade(1000)
                        .build(),
                    loading = {
                        LinearProgressIndicator()
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Fit
                )
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = onDismissClicked,
                ) {
                    Text(text = "Salir")
                }
                TextButton(
                    onClick = { onConfirmClicked() }
                ) {
                    Text(text = "Borrar", color = Color.Red, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}