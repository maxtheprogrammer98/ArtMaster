package com.example.artmaster.login

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artmaster.R

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

/**
 * creates login form
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
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
        //setting background color
        modifier = Modifier
            .background(Color.DarkGray)
    ){
        Column(
            // contains user icon
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
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
                color = Color.White,
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
            TextField(
                value = usersEmail,
                onValueChange = {usersEmail = it},
                label = {
                    Text(text = stringResource(id = R.string.email))
                },
                modifier = Modifier
                    .padding(20.dp))

            //password field
            TextField(
                value = usersPassword ,
                onValueChange = {usersPassword = it},
                label = {
                    Text(text = stringResource(id = R.string.password))
                },
                modifier = Modifier
                    .padding(20.dp))
        }

        // forgot password?
        Text(
            text = stringResource(id = R.string.password_olvidada),
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(20.dp))
        
    }
}

