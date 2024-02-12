package com.example.artmaster.aisection

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
    // state variable
    var stateContentResponse = mutableStateOf("Respuesta de la IA...")
    // request
    fun getHelpClick(model : GenerativeModel, input : String){
        // asynchronous request
       viewModelScope.launch {
           // passing request
           val response = model.generateContent(
               content {
                   text(input)
               }
           )
           // storing result
           stateContentResponse.value = response.text.toString()
       }

    }



}