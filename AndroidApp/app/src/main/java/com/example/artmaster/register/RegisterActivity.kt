package com.example.artmaster.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artmaster.MainActivity
import com.example.artmaster.R
import com.example.artmaster.login.Login
import com.example.artmaster.profile.ProfileActivity
import com.example.artmaster.ui.theme.ArtMasterTheme

class RegisterActivity: MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMasterTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    RegisterScreen(
                        navigateToLogin = {
                            Intent(applicationContext, Login::class.java).also {
                                startActivity(it)
                            }
                        },
                        navigateToProfile = {
                            Intent(applicationContext, ProfileActivity::class.java).also {
                                startActivity(it)
                                finish()
                            }
                        }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RegisterScreen(navigateToLogin: () -> Unit, navigateToProfile: () -> Unit) {

        val scrollState = rememberScrollState()
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()
        val context = LocalContext.current


        var username by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val favoritos: ArrayList<String> = ArrayList()
        val completados: ArrayList<String> = ArrayList()
        val drawingArray: ArrayList<String> = ArrayList()
        val photoUrl by remember { mutableStateOf("") }
        val isAdmin by remember { mutableStateOf(false) }

        var isPasswordOpen by remember { mutableStateOf(false) }
        var isPasswordVisible by remember { mutableStateOf(false) }

        var isUsernameInvalid by remember { mutableStateOf(false) }
        var isEmailInvalid by remember { mutableStateOf(false) }
        var isPasswordInvalid by remember { mutableStateOf(false) }

        val passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}\$".toRegex()

        val usernameErrorMessage = stringResource(R.string.username_error_message)
        val emailErrorMessage = stringResource(R.string.email_error_message)
        val passwordErrorMessage = stringResource(R.string.password_error_message_01) +
                stringResource(R.string.password_error_message_02) +
                stringResource(R.string.password_error_message_03) +
                stringResource(R.string.password_error_message_04)

        val messageIsUsernameInvalid = stringResource(R.string.is_username_invalid)
        val messageIsEmailInvalid = stringResource(R.string.is_email_invalid)
        val messageIsPasswordInvalid = stringResource(R.string.is_password_invalid)
        val messageCompleteAll = stringResource(R.string.complete_all_fields)
        val messageError = stringResource(R.string.error_message_register_user)



        //main container
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .heightIn(max = 1050.dp),

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
                Image(
                    painter = painterResource(id = R.drawable.profile_default),
                    contentDescription = stringResource(R.string.logo_register),
                    modifier = Modifier.size(160.dp)
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(50)),
                )

                Text(
                    text = stringResource(R.string.register_title),
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
                    label = stringResource(R.string.username_label_register),
                    errorMessage = if (isUsernameInvalid) usernameErrorMessage else null,
                    modifier = Modifier.fillMaxWidth(),
                    contDesc = stringResource(R.string.cont_desc_username_register),
                    icon = stringResource(R.string.icon_username_register),
                    isPassword = false,
                    isPasswordOpen = false,
                    onPasswordVisibilityToggle = {
                        isPasswordOpen = !isPasswordOpen
                    }
                )


                ValidatedOutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        isEmailInvalid = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    },
                    label = stringResource(R.string.email_label_register),
                    errorMessage = if (isEmailInvalid) emailErrorMessage else null,
                    modifier = Modifier.fillMaxWidth(),
                    contDesc = stringResource(R.string.cont_desc_email_register),
                    icon = stringResource(R.string.icon_email_register),
                    isPassword = false,
                    isPasswordOpen = false,
                    onPasswordVisibilityToggle = {
                        isPasswordOpen = !isPasswordOpen
                    }
                )

                ValidatedOutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        isPasswordInvalid = password.isEmpty() || !passwordPattern.matches(password)
                    },
                    label = stringResource(R.string.password_label_register),
                    errorMessage = if (isPasswordInvalid) passwordErrorMessage else null,
                    modifier = Modifier.fillMaxWidth(),
                    contDesc = stringResource(R.string.cont_desc_password_register),
                    icon = stringResource(R.string.icon_password_register),
                    isPassword = true,
                    isPasswordOpen = isPasswordVisible,
                    onPasswordVisibilityToggle = {
                        isPasswordVisible = !isPasswordVisible
                    }
                )

                Button(onClick = {
                    if (isUsernameInvalid) {
                        Toast.makeText(context,messageIsUsernameInvalid, Toast.LENGTH_SHORT).show()
                    }else if (isEmailInvalid) {
                        Toast.makeText(context,messageIsEmailInvalid, Toast.LENGTH_SHORT).show()
                    }else if (isPasswordInvalid) {
                        Toast.makeText(context,messageIsPasswordInvalid, Toast.LENGTH_SHORT).show()
                    }else if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context,messageCompleteAll, Toast.LENGTH_SHORT).show()
                    }else if (!isUsernameInvalid && !isEmailInvalid && !isPasswordInvalid) {
                        createUserFirebase(
                            username,
                            email,
                            password,
                            context,
                            favoritos,
                            completados,
                            isAdmin,
                            photoUrl,
                            drawingArray
                        ) { navigateToProfile() }
                    }else{
                        Toast.makeText(context,messageError, Toast.LENGTH_SHORT).show()
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
                        text = stringResource(R.string.btn_register),
                        fontFamily = FontFamily.Monospace,
                        //color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        color = Color.Gray,
                        thickness = 1.dp
                    )

                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(R.string.or_register),
                        fontSize = 18.sp
                    )

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        color = Color.Gray,
                        thickness = 1.dp
                    )
                }

                SocialMediaBtn()

                TextButton(onClick = {
                    navigateToLogin()
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.register_goToLogin),
                        fontFamily = FontFamily.Monospace,
                        color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

        }
    }



}
