package com.example.artmaster.login

import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

/**
 * includes authentication related functions
 */
interface AuthenticateUsers {
    /**
     * authenticate the user based on the provided credentials (email and password)
     */
    fun authenticateUsers(emailUser : String, passwordUser : String, context:Context){
        //toast alerts
        val toastSuccess = Toast.makeText(
            context,
            "el logueo ha sido exitoso",
            Toast.LENGTH_SHORT)

        val toastFailure = Toast.makeText(
            context,
            "ha ocurrido un error en el proceso de logueo, no ha sido posible iniciar sesion",
            Toast.LENGTH_SHORT)

        val toastDBerror = Toast.makeText(
            context,
            "no se ha podido conectar con la BD",
            Toast.LENGTH_SHORT)

        // it initialize the auth service from FB
        val auth = Firebase.auth
        //validating user
        auth.signInWithEmailAndPassword(emailUser,passwordUser)
            // monitoring request
            .addOnCompleteListener{ task ->
                // SUCCESS
                if (task.isSuccessful){
                    toastSuccess.show()
                    // storing user's data in variable
                    val user = auth.currentUser
                    if (user != null) {
                        Toast.makeText(
                            context,
                            "Bienvenido! ${user.email}",
                            Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // FAILURE
                    toastFailure.show()
                }
            }.addOnFailureListener{
                // ERROR TRYING TO CONNECT WITH DB
                toastDBerror.show()
            }
    }

    /**
     * sends an email to the user with a link to change his password
     */
    fun resetUsersPassword(context: Context){
        // toast messages
        val loginError = Toast.makeText(
            context,
            "no estas logueado!",
            Toast.LENGTH_SHORT
        )

        val resetSuccessful = Toast.makeText(
            context,
            "se ha enviado a tu mail un link para que actualices tu contraseña",
            Toast.LENGTH_SHORT
        )

        val authError = Toast.makeText(
            context,
            "ha ocurrido un error al realizar la peticion a firebase auth",
            Toast.LENGTH_SHORT
        )

        //instantiating firebase auth service
        val auth = Firebase.auth
        // identifiyng current user
        val user = Firebase.auth.currentUser
        if(user != null){
            //extracting user's email
            val usersmail = user.email.toString()
            //executes request
            auth.sendPasswordResetEmail(usersmail)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        resetSuccessful.show()
                    } else{
                        authError.show()
                    }
                }
        } else {
            // otherwise, an alert is displayed
            loginError.show()
        }
    }
}