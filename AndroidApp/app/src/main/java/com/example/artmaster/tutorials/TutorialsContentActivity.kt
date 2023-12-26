package com.example.artmaster.tutorials

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.artmaster.MainActivity
import com.example.artmaster.R
import java.io.Serializable

/**
 * this activity contains the tutorials content, from the main activity
 * the menu bar is inhereted
 */
class TutorialsContentActivity : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // avoiding deprecated alert
        fun <T : Serializable?> getSerializable(activity: Activity, name: String, clazz: Class<T>): T
        {
            return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                activity.intent.getSerializableExtra(name, clazz)!!
            else
                activity.intent.getSerializableExtra(name) as T
        }

        //getting intent data
        val tutorialData = getSerializable(this, "TUTORIAL_DATA", TutorialsModels::class.java)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ){
                // null safety check
                if (tutorialData != null){
                    Log.i("tuto_content", tutorialData.nombre)
                   TutorialsLayoutContent(tutorialModels = tutorialData)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "no ha sido posible obtener la info del tutorial",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    fun TutorialsLayoutContent(tutorialModels: TutorialsModels){
        /* ------------------ MAIN LAYOUT ----------------------- */
        Scaffold(
            topBar = {
                super.TobBarMain()
            },
            bottomBar = {
                super.BottomBar()
            },
            modifier = Modifier
                .fillMaxSize()
        ){
            /* ------------------ CONTENT ----------------------- */
            AddTutorialContent(
                nombre = tutorialModels.nombre,
                descripcion = tutorialModels.descripcion,
                informacion = tutorialModels.informacion,
                calificacion = tutorialModels.calificacion)
        }
    }

}