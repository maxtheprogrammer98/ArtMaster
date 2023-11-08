package com.example.artmaster.register

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artmaster.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(context: Context) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordOpen by remember { mutableStateOf(false) }

    var isUsernameInvalid by remember { mutableStateOf(false) }
    var isEmailInvalid by remember { mutableStateOf(false) }
    var isPasswordInvalid by remember { mutableStateOf(false) }

    val passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}\$".toRegex()

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
                .padding(top = 14.dp),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp
        )

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                isUsernameInvalid = username.length < 3 || username.isEmpty() || username.length > 24
                            },
            label = { Text(text = "Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp),
//            colors = TextFieldDefaults.outlinedTextFieldColors(
//                textColor = Color.Black,
//                focusedBorderColor = Color.Black,
//                unfocusedBorderColor = Color.Black,
//                cursorColor = Color.Black
//            ),
            shape = RoundedCornerShape(50),
            singleLine = true,
            leadingIcon = {
                Row(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Username Icon",
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
            isError = isUsernameInvalid
        )

        if (isUsernameInvalid) {
            Text(
                text = "El Username debe contener mas de 3 caracteres",
                color = Color.Red,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                isEmailInvalid = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                            },
            label = { Text(text = "Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp),
//            colors = TextFieldDefaults.outlinedTextFieldColors(
//                textColor = Color.Black,
//                focusedBorderColor = Color.Black,
//                unfocusedBorderColor = Color.Black,
//                cursorColor = Color.Black
//            ),
            shape = RoundedCornerShape(50),
            singleLine = true,
            leadingIcon = {
                Row(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Icon",
//                        tint = Color.Black,
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
            isError = isEmailInvalid
        )

        if (isEmailInvalid) {
            Text(
                text = "El correo electrónico no es válido.",
                color = Color.Red,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                isPasswordInvalid = password.isEmpty() || !passwordPattern.matches(password)
                            },
            label = { Text(text = "Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp),
//            colors = TextFieldDefaults.outlinedTextFieldColors(
//                textColor = Color.Black,
//                focusedBorderColor = Color.Black,
//                unfocusedBorderColor = Color.Black,
//                cursorColor = Color.Black
//            ),
            shape = RoundedCornerShape(50),
            singleLine = true,
            leadingIcon = {
                Row(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password Icon",
                        //tint = Color.Black,
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

            visualTransformation = if (!isPasswordOpen) PasswordVisualTransformation() else VisualTransformation.None,
            isError = isPasswordInvalid,
            trailingIcon = {
                IconButton(onClick = { isPasswordOpen = !isPasswordOpen }) {
                    if (!isPasswordOpen) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Mostrar Password",
                            //tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }else {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Ocultar Password",
                            //tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }


        )

        if (isPasswordInvalid) {
            Text(
                text = "Tu contraseña debe contener al menos 8 caracteres,\n incluyendo al menos una letra mayúscula,\n una letra minúscula, un número y\n un carácter especial como @, #, \$, %, ^, &, + o !.",
                color = Color.Red,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

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
                .padding(top = 20.dp),
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