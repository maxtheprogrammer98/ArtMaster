package com.example.artmaster.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artmaster.MainActivity
import com.example.artmaster.R
import com.example.artmaster.ui.theme.ArtMasterTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Logout : MainActivity(), AddingLoginHeader, AuthenticateUsers{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent{
            ArtMasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                    CreateLogOut()
                }
            }
        }
    }


    @Composable
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    //@Preview
    fun CreateLogOut(){
        // flag and remember variables
        val scrollingState = rememberScrollState()

        var emailInput by remember {
            mutableStateOf("")
        }

        var passwordInput by remember {
            mutableStateOf("")
        }

        var showPassword by remember {
            mutableStateOf(VisualTransformation.None)
        }

        //------------------------------ MAIN CONTAINER ----------------------------//
        Scaffold(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollingState)
                .heightIn(max = 800.dp),

            topBar = {
                // inserting topbar from parent class
                super.TobBarMain()
            },

            bottomBar = {
                // inserting bottombar from parent class
                super.BottomBar()
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
                OutlinedTextField(
                    value = emailInput ,
                    onValueChange = {emailInput = it},
                    supportingText = {
                        Text(text = stringResource(id = R.string.email_sample))
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.email) )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = stringResource(id = R.string.email))
                    },
                    label = {
                        Text(text = stringResource(id = R.string.email))
                    },
                    shape = CircleShape,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )

                // --------------------- PASSWORD FIELD -------------------------------//
                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = {passwordInput = it},
                    placeholder = {
                        Text(text = stringResource(id = R.string.password))
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = stringResource(id = R.string.password))
                    },
                    label = {
                        Text(text = stringResource(id = R.string.password))
                    },
                    shape = CircleShape,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    visualTransformation = showPassword ,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (showPassword == VisualTransformation.None){
                                    // password is hidden
                                    showPassword = PasswordVisualTransformation()
                                } else {
                                    // password is shown
                                    showPassword = VisualTransformation.None
                                }}
                            ) {
                                Icon(
                                    painter = painterResource(id = R.mipmap.icpassword) ,
                                    contentDescription = stringResource(id = R.string.esconder_password))
                        }
                    }
                )
                
                // --------------------- FORGOT PASSWORD? -------------------------------//
                Text(
                    text = stringResource(id = R.string.password_olvidada),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .clickable {
                            resetUsersPassword(applicationContext)
                        })
                
                // --------------------- ACCEPT BTN -------------------------------//

                Button(
                    onClick = { logOutUser(emailInput,passwordInput) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .heightIn(min = 60.dp),
                    shape = CircleShape){
                    Text(
                        text = stringResource(id = R.string.btn_aceptar),
                        fontSize = 18.sp)
                }
            }
        }
    }

    /**
     * it logs out the user based on the provided credentials
     */
    fun logOutUser(emailUser:String, passwordUser:String){
        //Toast messages
        val incorrectCredentials = Toast.makeText(
            this,
            "los datos ingresados son incorrectos",
            Toast.LENGTH_SHORT)

        val correctCredentials = Toast.makeText(
            this,
            "has cerrado sesion exitosamente",
            Toast.LENGTH_SHORT)

        val errorConnectingDB = Toast.makeText(
            this,
            "error al conectar con la BD",
            Toast.LENGTH_SHORT)

        // instantiating firebase auth
        val firebaseauth = Firebase.auth
        // validating user's credentials first
        firebaseauth.signInWithEmailAndPassword(emailUser,passwordUser)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    //user validated
                    firebaseauth.signOut()
                    // user's redirected the home
                    //TODO: add intent to home section
                    correctCredentials.show()
                } else {
                    incorrectCredentials.show()
                }
            }
            // error trying to connect with DB
            .addOnFailureListener{ exception ->
                errorConnectingDB.show()
            }
    }



}