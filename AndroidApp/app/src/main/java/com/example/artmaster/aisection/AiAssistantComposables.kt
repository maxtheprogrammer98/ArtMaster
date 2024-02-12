package com.example.artmaster.aisection

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material.icons.twotone.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.artmaster.R
import com.google.ai.client.generativeai.GenerativeModel

/**
 * sets a text field that will input a request to AI model
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputFieldAIhelp(
    viewModelAIsection: AiAssistantViewModel = viewModel(),
    model : GenerativeModel){
    // variable that stores the input
    var inputUser by remember {
        mutableStateOf("")
    }

    // --------------------  WRAPPER --------------------//
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(15.dp))
            .shadow(4.dp, shape = RoundedCornerShape(15.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        // --------------------  separation --------------------//
        Spacer(modifier = Modifier.height(20.dp))

        // --------------------  title --------------------//
        Text(
            text = stringResource(id = R.string.ai_title_input),
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Bold)

        // --------------------  separation  --------------------//
        Spacer(modifier = Modifier.height(20.dp))

        // --------------------  text field input --------------------//
        OutlinedTextField(
            value = inputUser,
            onValueChange = {inputUser = it},
            modifier = Modifier.padding(20.dp),
            placeholder = {
                Text(text = stringResource(id = R.string.ai_help))
            },
            shape = CircleShape,
            trailingIcon = {
                IconButton(
                    // removes text
                    onClick = {inputUser = ""}
                ){
                    Icon(
                        imageVector = Icons.TwoTone.Clear,
                        contentDescription = stringResource(id = R.string.borrar))
                }
            },
            leadingIcon = {
                IconButton(onClick = {
                    // calling the function that updates the viewModel
                    viewModelAIsection.getHelpClick(
                        model = model,
                        input = inputUser
                    )
                }){
                    Icon(
                        imageVector = Icons.TwoTone.Done,
                        contentDescription = stringResource(id = R.string.aceptar))
                }
            })

        // --------------------  separation --------------------//
        Spacer(modifier = Modifier.height(20.dp))
    }


}


@Composable
fun DisplayAIresponseText(viewModelAIsection: AiAssistantViewModel = viewModel()){
    // reference variable
    val aiResponse = viewModelAIsection.stateContentResponseText.value

    // --------------------  WRAPPER --------------------//
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(15.dp))
            .shadow(4.dp, shape = RoundedCornerShape(15.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){



        // --------------------  title --------------------//
        Text(
            text = stringResource(id = R.string.ai_titulo),
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(20.dp))


        // --------------------  text response --------------------//
        Text(
            text = aiResponse,
            modifier = Modifier.padding(12.dp))

        // --------------------  separation --------------------//
        Spacer(modifier = Modifier.height(20.dp))
    }
}


@Composable
fun AddRobotIcon(painterIcon: Painter){
    // --------------------  WRAPPER --------------------//
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(30.dp),
        contentAlignment = Alignment.Center
    ){
        // --------------------  icon --------------------//
        Image(
            painter = painterIcon,
            contentDescription = stringResource(id = R.string.robot_icon),
            modifier = Modifier
                .height(125.dp)
                .width(125.dp))
    }
}

/**
 * enables you to add options in the IA section
 */
@Composable
fun AddOptionAI(
    painterIcon: Painter,
    description : String,
    context: Context,
    intentTo: String){
    // --------------------  WRAPPER --------------------//
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clickable { createIntent(context, intentTo) }
            .background(Color.White, shape = RoundedCornerShape(12.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        // --------------------  icon --------------------//
        Image(
            painter = painterIcon,
            contentDescription = "icon",
            modifier = Modifier.size(80.dp))

        // --------------------  description --------------------//
        Text(
            text = description,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(6.dp))
    }
}

/**
 * identifies which option was selected and opens the corresponding activity
 */
fun createIntent(context: Context, intentTo: String){
    // validation
    if (intentTo.equals("textActivity")){
        // text option
        val intent = Intent(context, AiAssistantActivityText::class.java)
        context.startActivity(intent)

    } else if (intentTo.equals("imageActivity")){
        // image option
        val intent = Intent(context, AiAssistantActivityImg::class.java)
        context.startActivity(intent)

    } else {
        // error message
        Toast.makeText(
            context,
            "invalid option",
            Toast.LENGTH_SHORT).show()
    }
}

/**
 * enables the user to upload a drawing which
 * will be analyzed by the AI
 */
@Composable
fun BtnUploadImg(
    viewModel: AiAssistantViewModel = viewModel(),
    model: GenerativeModel,
    context: Context){

    // selected image
    var selectedImage by remember { mutableStateOf<Uri?>(null)}

    // object needed for conversion (uri to bitmap)
    val contentResolver = context.contentResolver

    // launcher
    val singlePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {uri -> selectedImage = uri })

    // ---------------------- PREVIEW IMAGE ------------------------------//

    AsyncImage(
        model = selectedImage,
        contentDescription = stringResource(id = R.string.imagen),
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.Crop)

    // ---------------------- UPLOADING BUTTON ------------------------------//
    Button(
        onClick = {
            // 1 -  executing launcher which will open the galery to select the image
            singlePickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ){
        Text(text = stringResource(id = R.string.btn_subir))
    }

    // ---------------------- ASK IA BUTTON ------------------------------//
    Button(
        onClick = {
            // 2- transforming uri image into bitmap
            if (selectedImage != null){
                val inputStream = contentResolver.openInputStream(selectedImage as Uri)
                val selecImageBitMap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close() // close after use

                // 3 - updating viewModel
                viewModel.getFeedbackDrawing(
                    model = model,
                    image = selecImageBitMap
                )
            } else {
                // displaying error
                Toast.makeText(
                    context,
                    "debes subir una imagen primero!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ){
        Text(text = stringResource(id = R.string.btn_ask_ai))
    }

}


@Composable
fun DisplayFeedback(viewModel: AiAssistantViewModel = viewModel()){

    // reference variable
    val aiResponse = viewModel.stateContentResponseImg.value

    // --------------------  WRAPPER --------------------//
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(15.dp))
            .shadow(4.dp, shape = RoundedCornerShape(15.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        // --------------------  title --------------------//
        Text(
            text = stringResource(id = R.string.ai_titulo),
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(20.dp))


        // --------------------  text response --------------------//
        Text(
            text = aiResponse,
            modifier = Modifier.padding(12.dp))

        // --------------------  separation --------------------//
        Spacer(modifier = Modifier.height(20.dp))
    }
}
