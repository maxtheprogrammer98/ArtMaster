package com.example.artmaster.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.artmaster.login.Login
import com.example.artmaster.ui.theme.ArtMasterTheme

class ProfileActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMasterTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ProfileScreen(navigateToLogin = { navigateToLogin() })
                }
            }
        }
    }

    private fun navigateToLogin() {
        Intent(applicationContext, Login::class.java).also {
            startActivity(it)
            finish()
        }
    }

}