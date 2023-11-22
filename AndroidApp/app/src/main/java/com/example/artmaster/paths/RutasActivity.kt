package com.example.artmaster.paths

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
import com.google.firebase.initialize
import kotlin.coroutines.suspendCoroutine

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

                // ----------------- CARDS -----------------//
                CreateCards()
            }
        }
    }

    /**
     * Asynchronous funtion that fetchs information from firestore
     * to generate cards
     */
    @SuppressLint("MutableCollectionMutableState")
    @Composable
    fun CreateCards(){
        //--------------- BASE ARRAY ------------------------//
        var learningPaths by remember {
            mutableStateOf(ArrayList<PathsGenerator>())
        }
        // ------------- RETRIEVING INFORMATION -----------------//
        LaunchedEffect(Unit){
            getRutas(learningPaths)
        }
        Log.i("testing", "learningPath: ${learningPaths.size}")
        // ----------- PATH CARDS---------//
        learningPaths.forEach {
                path -> Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(),
            shape = CircleShape
        ){
            //TODO: implement picasso to render images

            // image logo
            Image(
                painter = painterResource(id = R.mipmap.articon),
                contentDescription = "art icon",
                alignment = Alignment.Center)

            // title
            Text(
                text = path.nombre,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(15.dp))

            // information
            Text(
                text = path.informacion,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp,0.dp))

            // dificultad
            Text(
                text = "Dificultad: ${path.dificultad}",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp))

            // btn (redirects user to learning path)
            Button(
                onClick = { /*TODO: add validating function*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)){
                Text(
                    text = stringResource(id = R.string.btn_aceptar))
                }
            }
        }
    }

    /**
     * it makes a get request to retrieve all the routes stored in the DB
     */
    fun getRutas(pathsArray:ArrayList<PathsGenerator>){
        // instantiating FireStore
        val db = Firebase.firestore
        // referencing collection
        val rutasCollection = db.collection("rutas")
        //executing get request
        rutasCollection.get()
            // success listener
            .addOnSuccessListener { documents ->
                if(!documents.isEmpty){
                    // arraylist that will update the main one
                    var updatedArray = ArrayList<PathsGenerator>()
                    for(document in documents){
                        val pathDocument = PathsGenerator(
                            // creating object from document
                            ID = document.id,
                            nombre = document.getString("nombre").toString(),
                            informacion = document.getString("informacion").toString(),
                            imagen = document.getString("imagen").toString(),
                            dificultad = document.getString("dificultad").toString()
                        )
                        // adding it into arraylist
                        Log.i("test", "document addded: ${pathDocument.ID} / ${pathDocument.nombre}")
                        updatedArray.add(pathDocument)
                    }
                    //updating main arraylist
                    pathsArray = updatedArray

                }
            }
            // failure listener
            .addOnFailureListener { exception ->
                Log.e("error", "error connection DB", exception)
            }
    }

}