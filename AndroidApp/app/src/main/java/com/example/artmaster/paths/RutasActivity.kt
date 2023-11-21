package com.example.artmaster.paths

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artmaster.MainActivity
import com.example.artmaster.graphicElements.HeaderMain
import com.example.artmaster.ui.theme.ArtMasterTheme
import com.example.artmaster.R
import com.example.artmaster.graphicElements.PathsGenerator
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class RutasActivity : MainActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                    CreateRutas()
                }
            }
        }
    }

    // array that contains all paths stored in the DB
    var paths = arrayListOf<PathsGenerator>()

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun CreateRutas(){
        // scrolling variable
        val scrollingState = rememberScrollState()

        // ----------------------- MAIN LAYOUT / SCAFFOLD --------------------------//
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
            // ----------------------- MAIN WRAPPER --------------------------//
            Column(
                modifier = Modifier
                    .padding(0.dp, 60.dp, 0.dp, 0.dp)
                    .verticalScroll(scrollingState)

            ){
                // ------------ INSERTING HEADER ---------//
                HeaderMain()

                // ------------ DESCRIPCION  WRAPPER ---------//
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 20.dp)
                ){
                    // ---------- TITULO ---------//
                    Text(
                        text = stringResource(id = R.string.rutas),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 20.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold)

                    // -------- DESCRIPCION ---------//
                    Text(
                        text = stringResource(id = R.string.rutas_descripcion),
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp)
                }

            }
        }
    }

    /**
     * it makes a get request to retrieve all the routes stored in the DB
     */
    fun getRutas(){
        // instantiating firestore DB
        val db = Firebase.firestore
        // referencing collection
        val pathsCollection = db.collection("rutas")
        // GET REQUEST
        pathsCollection.get()
            // success event
            .addOnSuccessListener { result ->
                for(document in result){
                    //creating object from document
                    val pathDocument = PathsGenerator(
                        ID = document.getString("id").toString(),
                        nombre = document.getString("nombre").toString(),
                        informacion = document.getString("informacion").toString(),
                        imagen = document.getString("imagen").toString(),
                        dificultad = document.getString("dificultad").toString()
                    )
                    // adding object into array
                    paths.add(pathDocument)
                }

            }
    }


}