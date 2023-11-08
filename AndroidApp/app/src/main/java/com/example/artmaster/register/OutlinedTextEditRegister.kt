package com.example.artmaster.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidatedOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    contDesc: String,
    icon: String,
    isPassword: Boolean = false,
    isPasswordOpen: Boolean,
    onPasswordVisibilityToggle: () -> Unit
) {
    val imageVector = when (icon) {
        "person" -> Icons.Default.Person
        "email" -> Icons.Default.Email
        "password" -> Icons.Default.Lock
        else -> Icons.Default.Person
    }
    OutlinedTextField(

        value = value,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        label = { Text(text = label) },
        visualTransformation = if (!isPasswordOpen && isPassword) {
            PasswordVisualTransformation()
        } else {
            visualTransformation
        },
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp),
        shape = RoundedCornerShape(50),
        singleLine = true,
        leadingIcon = {
            Row(
                modifier = Modifier.padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = contDesc,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Spacer(modifier = Modifier
                    .width(1.dp)
                    .height(25.dp)
                    .background(Color.Gray)
                )
            }
        },
        textStyle = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        ),
        isError = isError,
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = onPasswordVisibilityToggle) {
                    if (!isPasswordOpen) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Mostrar Password"
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Ocultar Password"
                        )
                    }
                }
            }
        }
    )

    errorMessage?.let { message ->
        if (!isError) {
            Text(
                text = message,
                color = Color.Red,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 14.dp)
            )
        }
    }
}
