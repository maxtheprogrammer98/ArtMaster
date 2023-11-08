package com.example.artmaster.register

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artmaster.R


@Composable
fun RegisterScreen(context: Context) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordOpen by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    var isUsernameInvalid by remember { mutableStateOf(false) }
    var isEmailInvalid by remember { mutableStateOf(false) }
    var isPasswordInvalid by remember { mutableStateOf(false) }

    val passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}\$".toRegex()

    val usernameErrorMessage = "El Username debe contener más de 3 caracteres"
    val emailErrorMessage = "El correo electrónico no es válido."
    val passwordErrorMessage = "Tu contraseña debe contener al menos 8 caracteres,\n" +
            " incluyendo al menos una letra mayúscula,\n" +
            " una letra minúscula, un número y\n" +
            " un carácter especial como @, #, \$, %, ^, &, + o !."

//  isEmailInvalid = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
//  isPasswordInvalid = password.isEmpty() || !passwordPattern.matches(password)


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Logo Register",
            modifier = Modifier.size(160.dp)
        )

        Text(
            text = "CREA TU CUENTA",
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp
        )

        ValidatedOutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                isUsernameInvalid = username.length < 3 || username.isEmpty() || username.length > 24
            },
            label = "Username",
            errorMessage = if (isUsernameInvalid) usernameErrorMessage else null,
            modifier = Modifier.fillMaxWidth(),
            contDesc = "Icono Username",
            icon = "person",
            isPassword = false,
            isPasswordOpen = false,
            onPasswordVisibilityToggle = {
                // Cuando el usuario hace clic en el ícono de alternancia de visibilidad
                isPasswordOpen = !isPasswordOpen
            }
        )


        ValidatedOutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                isEmailInvalid = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
            },
            label = "Email",
            errorMessage = if (isEmailInvalid) emailErrorMessage else null,
            modifier = Modifier.fillMaxWidth(),
            contDesc = "Icono Email",
            icon = "email",
            isPassword = false,
            isPasswordOpen = false,
            onPasswordVisibilityToggle = {
                // Cuando el usuario hace clic en el ícono de alternancia de visibilidad
                isPasswordOpen = !isPasswordOpen
            }
        )

        ValidatedOutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                isPasswordInvalid = password.isEmpty() || !passwordPattern.matches(password)
            },
            label = "Password",
            errorMessage = if (isPasswordInvalid) passwordErrorMessage else null,
            modifier = Modifier.fillMaxWidth(),
            contDesc = "Icono Password",
            icon = "password",
            isPassword = true,
            isPasswordOpen = isPasswordVisible,
            onPasswordVisibilityToggle = {
                // Cuando el usuario hace clic en el ícono de alternancia de visibilidad
                isPasswordVisible = !isPasswordVisible
            }
        )


        Button(onClick = {
            if (isUsernameInvalid) {
                Toast.makeText(context,"Completa el campo Username correctamente", Toast.LENGTH_SHORT).show()
            }else if (isEmailInvalid) {
                Toast.makeText(context,"Completa el campo Email correctamente", Toast.LENGTH_SHORT).show()
            }else if (isPasswordInvalid) {
                Toast.makeText(context,"Completa el campo Password correctamente", Toast.LENGTH_SHORT).show()
            }else if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context,"Completa todos los campos", Toast.LENGTH_SHORT).show()
            }else if (!isUsernameInvalid && !isEmailInvalid && !isPasswordInvalid) {
                Toast.makeText(context,"Registro exitoso!", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,"Ocurrio un error.", Toast.LENGTH_SHORT).show()
            }
        },
            colors = ButtonDefaults.buttonColors(
                //containerColor = Color.Cyan
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 40.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp
            )
        ) {
            Text(
                text = "REGISTRATE",
                fontFamily = FontFamily.Monospace,
                //color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        SocialMediaBtn()

        TextButton(onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Text(
                text = "Ya tienes una cuenta? Inicia Sesion.",
                fontFamily = FontFamily.Monospace,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}



@Composable
fun SocialMediaBtn() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = {},
            colors = ButtonDefaults.buttonColors(
                //containerColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp
            ),
            contentPadding = PaddingValues(horizontal = 26.dp, vertical = 10.dp)
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Google",
                    modifier = Modifier.size(20.dp),
                    //tint = Color.Unspecified,
                )

                Spacer(modifier = Modifier.width(10.dp))
                
                Text(
                    text = "Google",
                    fontFamily = FontFamily.Monospace,
                    //color = Color.Black
                )
            }
        }

        Button(onClick = {},
            colors = ButtonDefaults.buttonColors(
                //containerColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp
            ),
            contentPadding = PaddingValues(horizontal = 26.dp, vertical = 10.dp)
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Facebook",
                    modifier = Modifier.size(20.dp),
                    //tint = Color.Unspecified,
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Facebook",
                    fontFamily = FontFamily.Monospace,
                    //color = Color.Black
                )
            }
        }
    }
}