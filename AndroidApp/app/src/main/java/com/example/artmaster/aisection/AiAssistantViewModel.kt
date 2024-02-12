package com.example.artmaster.aisection

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

/**
 * manages all the flow of the sections content
 */
class AiAssistantViewModel() : ViewModel() {
    // state variables
    var stateContentResponseText = mutableStateOf("Respuesta de la IA...") // text request response
    var stateImageUploaded = mutableStateOf("") // image uploaded
    var stateContentResponseImg = mutableStateOf("") // drawing feedback

    // request from text input
    fun getHelpClick(model : GenerativeModel, input : String){
        // asynchronous request
       viewModelScope.launch {
           // passing request
           val response = model.generateContent(
               // setting arguments
               content {
                   text(input)
               }
           )
           // storing result
           stateContentResponseText.value = response.text.toString()
       }

    }

    // request based on image
    fun getFeedbackDrawing(model: GenerativeModel, image : Bitmap){
        // asynchronous request
        viewModelScope.launch {
            // passing request
            val feedback = model.generateContent(
                // setting arguments
                content {
                    text("que opinas de este dibujo?")
                    image(image)
                }
            )
            // storing result
            stateContentResponseImg.value = feedback.text.toString()
        }

    }




}