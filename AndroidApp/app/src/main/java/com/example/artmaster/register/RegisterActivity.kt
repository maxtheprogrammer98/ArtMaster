package com.example.artmaster.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.artmaster.MainActivity
import com.example.artmaster.login.Login
import com.example.artmaster.profile.ProfileActivity
import com.example.artmaster.ui.theme.ArtMasterTheme

class RegisterActivity: MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMasterTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    registerMenu()
                }
            }
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun registerMenu(){

        val scrollState = rememberScrollState()

        //main container
        Scaffold(
            modifier = Modifier
                .fillMaxWidth(),

            topBar = {
                //inserting topbar from parent class
                super.TobBarMain()
            },

            bottomBar = {
                //inserting bottombar from parent class
                super.BottomBar()
            }
        ){
            // Apply vertical scroll to the content inside the Scaffold
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .heightIn(max = 800.dp),
            ) {
                RegisterScreen(
                    navigateToLogin = { navigateToLogin() },
                    navigateToProfile = { navigateToProfile() }
                )
            }
        }
    }

    private fun navigateToLogin() {
        Intent(applicationContext, Login::class.java).also {
            startActivity(it)
        }
    }

    private fun navigateToProfile() {
        Intent(applicationContext, ProfileActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

}
