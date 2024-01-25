package com.example.artmaster.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artmaster.MainActivity
import com.example.artmaster.ui.theme.ArtMasterTheme
import com.example.artmaster.R
import com.example.artmaster.graphicElements.HeaderMain


class HomeActivity : MainActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                    CreateHome()
                }
            }
        }
    }

    //images list
    val imagesList = listOf<Int>(
        R.mipmap.portada1,
        R.mipmap.portada2,
        R.mipmap.portada3
    )

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun CreateHome(){
        // remember variables
        val scrollableSateVertical = rememberScrollState()
        
        //sections description
        val rutas = SectionsGenerator(
            title = stringResource(id = R.string.rutas),
            text = stringResource(id = R.string.seccion_rutas),
            image = painterResource(id = R.mipmap.articon)
        )

        val favs = SectionsGenerator(
            title = stringResource(id = R.string.favoritos),
            text = stringResource(id = R.string.seccion_favoritos),
            image = painterResource(id = R.mipmap.fav)
        )

        val notes = SectionsGenerator(
            title = stringResource(id = R.string.notas),
            text = stringResource(id = R.string.seccion_notas),
            image = painterResource(id = R.mipmap.notesicon)
        )

        val perfil = SectionsGenerator(
            title = stringResource(id = R.string.perfil),
            text = stringResource(id = R.string.seccion_perfil),
            image = painterResource(id = R.mipmap.usericon)
        )

        // list with all the section descriptions
        val listaSections = listOf<SectionsGenerator>(rutas,favs,notes,perfil)

        // ------------------------ SCAFFOLD MAIN LAYOUT ---------------------------//
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
            // ------------------------ BOX MAIN CONTAINER ---------------------------//
            Column(
                modifier = Modifier
                    .verticalScroll(scrollableSateVertical)
                    .padding(0.dp, 60.dp, 0.dp, 0.dp)
            ){

                // adding header
                Header()

                // adding front page
                FrontPage()

                // wrapper
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 0.dp, 0.dp, 70.dp)
                ){
                    //insterting descriptions dinamically
                    listaSections.forEach {
                        // main container
                            elem -> Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(200.dp, 400.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .padding(12.dp)
                            .background(Color.LightGray)

                    ){
                        // logo
                        Image(
                            painter = elem.image ,
                            contentDescription = elem.title,
                            modifier = Modifier
                                .height(100.dp)
                                .align(Alignment.CenterHorizontally))

                        // title
                        Text(
                            text = elem.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 6.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.Bold)

                        //text / content
                        Text(
                            text = elem.text,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp)
                    }
                    }
                }
            }
        }
    }
}