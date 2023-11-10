package com.example.artmaster.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artmaster.R
import com.example.artmaster.ui.theme.ArtMasterTheme
import com.example.artmaster.MainActivity
import com.example.artmaster.register.RegisterActivity

class Login : MainActivity() {
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
     * creates a header with a profile icon
     */
    @Composable
    fun InsertHeader(imageLogin : Painter, descriptionLogin : String){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .wrapContentSize(Alignment.BottomCenter)
        ){
            Image(
                painter = imageLogin,
                contentDescription = descriptionLogin,
                modifier = Modifier
                        .size(100.dp))
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun InsertMailField(){
        // base variable that stores the input
        var inputMail by remember {
            mutableStateOf("")
        }

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
    }


    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun InsertPasswordField(){
        // base variable that stores the input
        var inputPassword by remember {
            mutableStateOf("")
        }

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
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
    }

    @Composable
    fun InsertTitle(text : String){
        Text(
            text = text,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center))
    }

    /**
     * creates the login using different modularized functions
     */
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    //@Preview
    @OptIn(ExperimentalMaterial3Api::class)
    fun CreateLogin(){

        val scrollState = rememberScrollState()

        //main container
        Scaffold(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ){
            //inserting main menu bar
            super.TobBarMain()

            //inserting login content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                InsertHeader(
                    imageLogin = painterResource(id = R.mipmap.user),
                    descriptionLogin = stringResource(id = R.string.ic_user))

                InsertTitle(
                    text = stringResource(id = R.string.bienvenido))

                InsertMailField()

                InsertPasswordField()

                Text(
                    text = stringResource(id = R.string.password_olvidada),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                        .padding(10.dp)
                        .clickable {
                            Toast
                                .makeText(
                                    applicationContext,
                                    "via mail se ha enviado un enlace para que puedas asignar una nueva password",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        },
                    fontSize = 18.sp)

                //TODO: Make function that generates buttons
                Button(
                    onClick = {
                        Toast.makeText(applicationContext,
                            "accion procesada",
                            Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)){
                    Text(text = stringResource(id = R.string.iniciar_sesion) )
                }

                Text(
                    text = stringResource(id = R.string.login_alt),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                        .padding(10.dp),
                    fontSize = 18.sp)

                Button(
                    onClick = {
                        Toast.makeText(applicationContext,
                            "accion procesada",
                            Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)){
                    Text(text = stringResource(id = R.string.login_fb) )
                }

                Button(
                    onClick = {
                        Toast.makeText(applicationContext,
                            "accion procesada",
                            Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)){
                    Text(text = stringResource(id = R.string.login_google) )
                }


            }

        }
    }





}

