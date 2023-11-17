package com.example.artmaster.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artmaster.R
import com.example.artmaster.ui.theme.ArtMasterTheme
import com.example.artmaster.MainActivity
import com.example.artmaster.register.RegisterActivity
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.auth.auth

class Login : MainActivity(), AddingLoginHeader, AuthenticateUsers {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                    CreateLogin()
                }
            }
        }
    }


    /**
     * creates login that implements FB
     */
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    //@Preview
    @OptIn(ExperimentalMaterial3Api::class)
    fun CreateLogin(){
        // base variable that stores the input
        var inputMail by remember {
            mutableStateOf("")
        }

        // base variable that stores the input
        var inputPassword by remember {
            mutableStateOf("")
        }

        val scrollState = rememberScrollState()

        var showPassword by remember {
            mutableStateOf(VisualTransformation.None)
        }

        //main container
        Scaffold(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .heightIn(max = 800.dp),

            topBar = {
                //inserting topbar from parent class
                super.TobBarMain()
            },

            bottomBar = {
                //inserting bottombar from parent class
                super.BottomBar()
            }
        ){

            //inserting login content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                // --------------------- HEADER / USER ICON ------------------------//
                InsertHeader(
                    imageLogin = painterResource(id = R.mipmap.user),
                    descriptionLogin = stringResource(id = R.string.ic_user))

                // --------------------- WELCOME! ------------------------//
                InsertTitle(
                    text = stringResource(id = R.string.bienvenido))

                // --------------------- EMAIL FIELD ------------------------//
                OutlinedTextField(
                    value = inputMail,
                    onValueChange = {inputMail = it},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    label = {
                        Text(text = stringResource(id = R.string.email))
                    },
                    supportingText = {
                        Text(text = stringResource(id = R.string.email_sample))
                    },
                    shape = CircleShape,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = "email icon")
                    }
                )

                // --------------------- PASSWORD FIELD ------------------------//
                OutlinedTextField(
                    value = inputPassword ,
                    onValueChange = {inputPassword = it},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    label = {
                        Text(text = stringResource(id = R.string.password))
                    },
                    shape = CircleShape,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Info ,
                            contentDescription = "password icon")
                    },
                    visualTransformation = showPassword,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (showPassword == VisualTransformation.None){
                                    // password is hidden
                                    showPassword = PasswordVisualTransformation()
                                } else {
                                    // password is shown
                                    showPassword = VisualTransformation.None
                                }
                            }){
                                Icon(
                                    painter = painterResource(id = R.mipmap.icpassword),
                                    contentDescription = stringResource(id = R.string.esconder_password))
                        }
                    }
                )


                // --------------------- FORGOT PASSWORD? ------------------------//
                Text(
                    text = stringResource(id = R.string.password_olvidada),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                        .padding(10.dp)
                        .clickable {
                            resetUsersPassword(applicationContext)
                        },
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center)

                // --------------------- BTN LOGIN ------------------------//
                Button(
                    onClick = {
                        // triggers authenticating function
                        authenticateUsers(inputMail,inputPassword, applicationContext)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .heightIn(min = 60.dp),
                    ){
                    Text(
                        text = stringResource(id = R.string.iniciar_sesion),
                        fontSize = 18.sp)
                    Icon(
                        imageVector = Icons.Filled.Done ,
                        contentDescription = stringResource(id = R.string.iniciar_sesion),
                        modifier = Modifier
                            .padding(10.dp, 0.dp))
                }
            }
        }
    }




}

