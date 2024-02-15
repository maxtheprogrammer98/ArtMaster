package com.example.artmaster.aisection

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
    model : GenerativeModel,
    context: Context){

    // variable that stores the input
    var inputUser by remember { mutableStateOf("") }

    // --------------- Toast Messages -----------------//
    val successMessage = Toast.makeText(
        context,
        "La solicitud puede demorar unos segundos en procesarse",
        Toast.LENGTH_SHORT
    )
    val failureMessage = Toast.makeText(
        context,
        "El campo no puede estar vacio!",
        Toast.LENGTH_SHORT
    )

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
                    if (inputUser.isNotEmpty()){
                        // calling the function that updates the viewModel
                        viewModelAIsection.getHelpClick(
                            model = model,
                            input = inputUser
                        )
                        // displaying sucess message
                        successMessage.show()
                    } else {
                        // displaying error message
                        failureMessage.show()
                    }

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

/**
 * displays the response of the first option (general questions)
 */
@Composable
fun DisplayResponseAI(viewModelAIsection: AiAssistantViewModel = viewModel()){
    // reference variable
    val aiResponse = viewModelAIsection.stateGenericAnswer.value

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
            .padding(15.dp)
            .heightIn(90.dp, 125.dp)
            .clickable { createIntent(context, intentTo) }
            .background(Color.White, shape = RoundedCornerShape(12.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        // --------------------  icon --------------------//
        Image(
            painter = painterIcon,
            contentDescription = "icon",
            modifier = Modifier
                .size(65.dp)
                .padding(10.dp))

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
    //error message
    val errorMessage = Toast.makeText(
        context,
        "ha ocurrido un error, intenta de nuevo",
        Toast.LENGTH_SHORT
    )

    // validation
    if (intentTo.equals("textActivity")){
        // text option
        val intent = Intent(context, AiAssistantActivityText::class.java)
        context.startActivity(intent)

    } else if (intentTo.equals("imageActivity")){
        // image option
        val intent = Intent(context, AiAssistantActivityImg::class.java)
        context.startActivity(intent)

    } else if (intentTo.equals("ideasActivity")) {
        // ideas option
        val intent = Intent(context, AiAssistantActivityIdeas::class.java)
        context.startActivity(intent)

    } else {
        // displaying error message
        errorMessage.show()
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

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = stringResource(id = R.string.ai_image_uploaded),
            fontWeight = FontWeight.Bold,
            color = Color.White)
    }

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
            .padding(10.dp, 20.dp, 10.dp, 0.dp)
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
            .padding(10.dp, 20.dp, 10.dp, 0.dp)
    ){
        Text(text = stringResource(id = R.string.btn_ask_ai))
    }

    // ---------------------- DELETE BUTTON ------------------------------//
    Button(
        onClick = {
            // removes uploaded image
            selectedImage = null
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 20.dp, 10.dp, 0.dp)
    ){
        Text(text = stringResource(id = R.string.ai_remove_image))
    }


}

/**
 * displays a text giving you feedback of your drawing (drawing feedback)
 */
@Composable
fun DisplayFeedbackDrawing(viewModel: AiAssistantViewModel = viewModel()){
    // reference variable
    val aiResponse = viewModel.stateFeedbackDrawing.value

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


/**
 * gives you a text with different ideas for drawings (ideas suggestions)
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GetDrawingIdeas(
    viewModel: AiAssistantViewModel = viewModel(),
    model: GenerativeModel,
    context: Context
){
    // --------------- variables -----------------//
    var inputText by remember { mutableStateOf("")}

    // --------------- Toast Messages -----------------//
    val successMessage = Toast.makeText(
        context,
        "La solicitud puede demorar unos segundos en procesarse",
        Toast.LENGTH_SHORT
    )
    val failureMessage = Toast.makeText(
        context,
        "El campo no puede estar vacio!",
        Toast.LENGTH_SHORT
    )

    // --------------- wrapper -----------------//
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(15.dp)
            )
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(15.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        // --------------- title -----------------//
        Text(
            text = stringResource(id = R.string.ai_title_input),
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.padding(10.dp))

        // --------------- text field -----------------//
        OutlinedTextField(
            value = inputText,
            onValueChange = {inputText = it},
            leadingIcon = {
                IconButton(
                    onClick = {
                        if(inputText.isNotEmpty()){
                            // executing request
                            viewModel.getIdeasDrawing(
                                model = model,
                                input = inputText
                            )
                            // sucess message
                            successMessage.show()
                        } else {
                            // displaying message
                            failureMessage.show()
                    }
                }){
                    Icon(
                        imageVector = Icons.TwoTone.Done,
                        contentDescription = "icon OK")
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        inputText = ""
                    }
                ){
                    Icon(
                        imageVector = Icons.TwoTone.Clear,
                        contentDescription = stringResource(id = R.string.borrar))    
                }
            },
            placeholder = {
                Text(text = "Dime que estilo de dibujo te gusta y te dare algunas ideas!")
            })

        // --------------- spacer -----------------//
        Spacer(modifier = Modifier.height(10.dp))
    }
}


/**
 * displays the ideas after the request was sent
 */
@Composable
fun DisplayDrawingIdeas(
    viewModel: AiAssistantViewModel = viewModel(),
    model: GenerativeModel
){
    // --------------- variables -----------------//
    val ideasSuggested = viewModel.stateIdeasSuggested.value

    // -------------- wrapper -----------------//
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(15.dp)
            )
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(15.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        // ----------------- title --------------------//
        Text(
            text = stringResource(id = R.string.ai_titulo),
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.padding(10.dp))

        // ----------------- AI response --------------------//
        Text(
            text = ideasSuggested,
            modifier = Modifier.padding(20.dp))
    }
}

@Composable
fun MainIaIcon(){
    // boolean flag
    var enabled by remember { mutableStateOf(true)}
    // icon animation state
    val animationValue by animateFloatAsState(if (enabled) 1f else 0f)

    // ai icon
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 20.dp, 0.dp, 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(id = R.mipmap.iconia),
            contentDescription = "AI logo",
            modifier = Modifier
                .height(130.dp)
                .width(130.dp)
                .alpha(animationValue))
    }
}