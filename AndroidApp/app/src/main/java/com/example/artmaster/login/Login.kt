package com.example.artmaster.login

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artmaster.R
import com.example.artmaster.ui.theme.ArtMasterTheme

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                }
            }
            LoginApp()
        }
    }

    /**
     * creates login form
     */
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
//@Preview
    fun LoginApp(){
        // variables that handle the user's input
        var usersEmail by remember {
            mutableStateOf("email@gmail.com")
        }

        var usersPassword by rememberSaveable{
            mutableStateOf("")
        }
        
        // using a column as main container
        Column(
            // allowing vertical scrolling
            modifier = Modifier.verticalScroll(ScrollState(0))
        ){
            Column(
                // contains user icon
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .wrapContentSize(Alignment.Center)
            ){
                Image(
                    painter = painterResource(id = R.mipmap.user) ,
                    contentDescription = stringResource(id = R.string.ic_user),
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp))
            }

            Column(
                // contains "welcome" text
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .wrapContentSize(Alignment.Center)
            ){
                Text(
                    text = stringResource(id = R.string.bienvenido),
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp)
            }

            Column(
                //contains text fields
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            ){
                // email field
                OutlinedTextField(
                    value = usersEmail,
                    onValueChange = {usersEmail = it},
                    label = {
                        Text(text = stringResource(id = R.string.email))
                    },
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))

                //password field
                OutlinedTextField(
                    value = usersPassword ,
                    onValueChange = {usersPassword = it},
                    label = {
                        Text(text = stringResource(id = R.string.password))
                    },
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
            }

            // forgot password?
            Text(
                text = stringResource(id = R.string.password_olvidada),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth())

            //login button
            Button(
                onClick = {Log.i("login", "btn_login")},
                modifier = Modifier
                    .padding(30.dp, 20.dp)
                    .fillMaxWidth()){
                Text(text = stringResource(id = R.string.iniciar_sesion))
            }


            // alternative login options
            Text(
                text = stringResource(id = R.string.login_alt),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(10.dp))

            Row(
                //sing in via facebook / google
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            ) {
                Button(
                    onClick = { Log.i("login", "via fb")},
                    modifier = Modifier.padding(20.dp)){
                    Text(text = stringResource(id = R.string.login_fb))
                }

                Button(
                    onClick = { Log.i("login", "via google")},
                    modifier = Modifier.padding(20.dp)){
                    Text(text = stringResource(id = R.string.login_google))
                }
            }
        }
    }
}

