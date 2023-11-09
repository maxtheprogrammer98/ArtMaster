package com.example.artmaster.register

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artmaster.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Objects


@Composable
fun RegisterScreen(context: Context, navigateToLogin: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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

    val favoritos: ArrayList<String> = ArrayList()
    val completados: ArrayList<String> = ArrayList()
    val isAdmin by remember { mutableStateOf(false) }

//  isEmailInvalid = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
//  isPasswordInvalid = password.isEmpty() || !passwordPattern.matches(password)


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(R.string.logo_register),
            modifier = Modifier.size(160.dp)
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
                createUserFirebase(username, email, password, context, favoritos, completados, isAdmin) { navigateToLogin() }
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


/**
 * SIGN UP USERS
 */
private fun createUserFirebase(username: String, email: String, password: String, context: Context, favoritos: ArrayList<String>, completados: ArrayList<String>, isAdmin: Boolean, navigateToLogin: () -> Unit){
    FirebaseAuth
        .getInstance()
        .createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener {
            if (it.isSuccessful){
                val auth: FirebaseAuth = FirebaseAuth.getInstance()
                val firebaseUser: FirebaseUser? = auth.currentUser
                val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
                val id = Objects.requireNonNull<FirebaseUser>(auth.currentUser).uid
                val map: MutableMap<String, Any> = HashMap()
                map["id"] = id
                map["name"] = username
                map["email"] = email
                map["password"] = password
                map["favoritos"] = favoritos
                map["completados"] = completados
                map["isAdmin"] = isAdmin

                assert(firebaseUser != null)
                firebaseUser!!.sendEmailVerification()

                firebaseFirestore.collection("usuarios").document(id).set(map)
                    .addOnSuccessListener {
                        navigateToLogin()

                        Toast.makeText(
                            context, context
                                .getString(R.string.successful_register),
                            Toast.LENGTH_SHORT).show()
                    }
            }
        }
        .addOnFailureListener {
            Toast.makeText(context,"Error: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
        }
}


/**
 * SOCIAL MEDIA BTN
 */
@Composable
fun SocialMediaBtn() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
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
                    contentDescription = stringResource(R.string.cont_desc_google),
                    modifier = Modifier.size(20.dp),
                    //tint = Color.Unspecified,
                )

                Spacer(modifier = Modifier.width(10.dp))
                
                Text(
                    text = stringResource(R.string.google),
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
                    contentDescription = stringResource(R.string.cont_desc_facebook),
                    modifier = Modifier.size(20.dp),
                    //tint = Color.Unspecified,
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = stringResource(R.string.facebook),
                    fontFamily = FontFamily.Monospace,
                    //color = Color.Black
                )
            }
        }
    }
}