package com.example.artmaster.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.artmaster.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun ProfileHeader(user: User, navigateToLogin: () -> Unit) {
    val auth = FirebaseAuth.getInstance()


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))

        user.photoUrl?.let { imageUrl ->
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .crossfade(1000)
                    .build(),
                loading = {
                    CircularProgressIndicator()
                },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
            Log.d("ProfileScreen", "Photo URL: $imageUrl")
        } ?: run {
            // If user.photoUrl is null, display the default profile photo
            Image(
                painter = painterResource(id = R.drawable.profile_default),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.name,
                fontSize = 18.sp,
                fontWeight = SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = user.email,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }


        if (user.isAdmin) {
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painterResource(id = R.drawable.ic_odin),
                contentDescription = null,
                modifier = Modifier
                    .size(42.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))
        IconButton(onClick = {
            auth.signOut()
            navigateToLogin()
        }) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Cerrar Sesion",
                tint = Color.Red,
                modifier = Modifier
                    .size(32.dp)
                    .padding(4.dp)
                )
        }

    }

}

@Composable
fun ProfileInfoItem(
    icon: ImageVector,
    label: String,
    value: String,
    isEditable: Boolean = false,
    onEditClick: ((String) -> Unit)? = null
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedValue by remember { mutableStateOf(value) }
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userId = currentUser?.uid
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .background(color = MaterialTheme.colorScheme.background, CircleShape)
                .padding(4.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (isEditing) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    BasicTextField(
                        value = editedValue,
                        onValueChange = { editedValue = it },
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(4.dp)
                            )
                            .padding(4.dp)
                    )
                    IconButton(
                        onClick = {
                            if (editedValue.length in 3..24) {
                                // Save edit on Firestore
                                onEditClick?.invoke(editedValue)

                                // Update the field on Firestore
                                userId?.let { userId ->
                                    updateUserField(userId, "name", editedValue)
                                }

                                Toast.makeText(
                                    context,
                                    "Nombre editado",
                                    Toast.LENGTH_SHORT
                                ).show()

                                isEditing = false
                            } else {
                                Toast.makeText(
                                    context,
                                    "El nombre debe tener entre 3 y 24 caracteres.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    ) {
                        if (isEditing) {
                            // Conditionally change the icon based on the editing state
                            val editIcon = if (editedValue.length in 3..24) {
                                Icons.Default.Check
                            } else {
                                Icons.Default.Warning // You can change this to another icon or remove it
                            }

                            Icon(
                                imageVector = editIcon,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            // Use a different icon when not editing
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    IconButton(onClick = { isEditing = false }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = value,
                        fontSize = 16.sp,
                    )
                    if (isEditable) {
                        IconButton(onClick = {
                            isEditing = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}




fun updateUserField(userId: String, field: String, value: String) {
    val db = FirebaseFirestore.getInstance()
    val userRef = db.collection("usuarios").document(userId)

    userRef
        .update(field, value)
        .addOnSuccessListener {
            // Update successful
            Log.d("Firestore", "Campo actualizado correctamente: $field")
        }
        .addOnFailureListener { e ->
            // Error update
            Log.w("Firestore", "Error al actualizar el campo $field", e)
        }
}


