package com.example.artmaster.register

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.artmaster.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Objects


/**
 * SIGN UP USERS
 */
fun createUserFirebase(
    username: String,
    email: String,
    password: String,
    context: Context,
    favoritos: ArrayList<String>,
    completados: ArrayList<String>,
    isAdmin: Boolean,
    photoUrl: String,
    drawingArray: ArrayList<String>,
    navigateToProfile: () -> Unit
){
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
                map["userID"] = id
                map["name"] = username
                map["email"] = email
                map["favoritos"] = favoritos
                map["completados"] = completados
                map["isAdmin"] = isAdmin
                map["photoUrl"] = photoUrl
                map["drawingArray"] = drawingArray

                assert(firebaseUser != null)
                firebaseUser!!.sendEmailVerification()

                firebaseFirestore.collection("usuarios").document(id).set(map)
                    .addOnSuccessListener {
                        navigateToProfile()

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