package com.example.artmaster.home

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.artmaster.R
import com.example.artmaster.aisection.AiAssistantActivityPreview
import com.example.artmaster.paths.PathsActivity

@Composable
fun FrontPage(){
    // scrolling variable option
    val scrolling = rememberScrollState()

    // ----------- WRAPPER -------------//
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .horizontalScroll(scrolling),
        horizontalArrangement = Arrangement.Center
    ){
        // ----------- FRONT PAGE IMAGE -------------//
        Image(
            painter = painterResource(id = R.mipmap.fullportada),
            contentDescription = stringResource(id = R.string.imagen) )
    }
}


@Composable
fun Header(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 20.dp)
    ){
        Image(
            painter = painterResource(id = R.mipmap.header),
            contentDescription = stringResource(id = R.string.header),
            modifier = Modifier.fillMaxWidth())
    }
}

/**
 * creates cards that show a brief description of the main sections
 * and redirect the user to them
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsSections(
    title : String,
    icon : Painter,
    description : String,
    intentTo: String,
    context: Context
){
    // ------------------- CARD ELEMENT ------------------- //
    Card(
        onClick = {
            // redirects the user to the selected activity
            openActivity(
                intentTo = intentTo,
                context = context
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(color = Color.White, shape = RoundedCornerShape(15.dp)),

        shape = CardDefaults.elevatedShape,
    ){
        // ------------------- wrapper ------------------- //
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White, shape = RoundedCornerShape(15.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ){
            // -------- IMAGE ICON ---------- //
            Image(
                painter = icon,
                contentDescription = "icon $title",
                modifier = Modifier
                    .height(70.dp)
                    .width(70.dp)
                    .padding(10.dp))

            // -------- TITLE ---------- //
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.padding(10.dp,0.dp))

            // -------- spacer ---------- //
            Spacer(modifier = Modifier.height(12.dp))

            // -------- description ---------- //
            Text(
                text = description,
                modifier = Modifier.padding(10.dp,0.dp))

            // -------- spacer ---------- //
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

fun openActivity(
    intentTo: String,
    context : Context
){
    // validation
    if (intentTo.equals("IAactivity")){
        // redirect to activity:
        val intent = Intent(context, AiAssistantActivityPreview::class.java)
        context.startActivity(intent)

    } else if(intentTo.equals("NotesActivity")){
        // redirect to activity:
        Toast.makeText(
            context,
            "seccion en construccion",
            Toast.LENGTH_SHORT
        ).show()

    } else if(intentTo.equals("RutasActivity")){
        // redirect to activity:
        val intent = Intent(context,PathsActivity::class.java)
        context.startActivity(intent)

    } else {
        // invalid option message
        Toast.makeText(
            context,
            "opcion no valida",
            Toast.LENGTH_SHORT
        ).show()
    }
}