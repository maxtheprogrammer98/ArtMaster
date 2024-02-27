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
    // TODO: group all these state variables into a single object!
    var stateGenericAnswer = mutableStateOf("Respuesta de la IA...") // text request response
    var stateFeedbackDrawing = mutableStateOf("") // drawing feedback
    var stateIdeasSuggested = mutableStateOf("") // feedback ideas

    /**
     * request based on input text (general question)
     */
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
           stateGenericAnswer.value = response.text.toString()
       }

    }

    /**
     * request based on bitmap image (drawing feedback)
     */
    fun getFeedbackDrawing(model: GenerativeModel, image : Bitmap){
        // asynchronous request
        viewModelScope.launch {
            // passing request
            val feedback = model.generateContent(
                // setting arguments
                content {
                    text("que opinas de este dibujo, tienes alguna sugerencia?")
                    image(image)
                }
            )
            // storing result
            stateFeedbackDrawing.value = feedback.text.toString()
        }

    }

    /**
     * request based on input text (drawing ideas)
     */
    fun getIdeasDrawing(model: GenerativeModel, input: String){
        // asynchronous request
        viewModelScope.launch {
            // passing request
            val ideas = model.generateContent(
                content {
                    text("me puedes dar ideas para algun dibujo de estilo: $input ?")
                }
            )
            // storing result
            stateIdeasSuggested.value = ideas.text.toString()
        }
    }

}