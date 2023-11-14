package com.example.artmaster.login

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.artmaster.MainActivity
import com.example.artmaster.R

class Logout : MainActivity(), AddingLoginHeader{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @Composable
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    fun createLogOut(){
        // flag and remember variables
        var scrollingState = rememberScrollState()

        var emailInput by remember {
            mutableStateOf("")
        }

        var passwordInput by remember {
            mutableStateOf("")
        }

        //------------------------------ MAIN CONTAINER ----------------------------//
        Scaffold(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 900.dp)
                .verticalScroll(scrollingState),
            topBar = {
                super.TobBarMain()
            }
        ) {
            // ---------------------- COLUMN GENERAL CONTAINER ----------------------//
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // ---------------------- IMAGE HEADER ----------------------//
                InsertHeader(
                    imageLogin = painterResource(id = R.mipmap.user) ,
                    descriptionLogin = stringResource(id = R.string.ic_user))

                // ---------------------- TITLE "Hasta Luego" ----------------------//
                InsertTitle(text = stringResource(id = R.string.hasta_luego))

                // --------------------- EMAIL FIELD -------------------------------//
                //TODO: Add here OutLineTextField for email

                // --------------------- PASSWORD FIELD -------------------------------//
                //TODO: Add here OutLineTextField for password

            }
        }
    }



}