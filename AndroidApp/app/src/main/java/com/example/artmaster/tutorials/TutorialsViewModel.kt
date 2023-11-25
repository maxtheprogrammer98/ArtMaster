package com.example.artmaster.tutorials

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TutorialsViewModel : ViewModel(){
    // mutable variable reference
    val tutorialsState = mutableStateOf(
        ArrayList<TutorialsModels>())

    suspend fun fetchTutorials():ArrayList<TutorialsModels>{
        // instantiating firestore
        val db = Firebase.firestore
        // collection reference
        val tutorialsCollection = db.collection("tutorials")
        // retrieved information
        var tutorialsFetched = ArrayList<TutorialsModels>()
        // get request
        try {
            tutorialsCollection.get().await().map {
                // deserializing documents and transforming them into models
                val result =it.toObject(TutorialsModels::class.java)
                tutorialsFetched.add(result)
            }
        } catch (e:FirebaseFirestoreException){
            Log.e("error", "error while fetching data", e)
        }
        // returning fetched information
        return tutorialsFetched
    }

    private fun getTutorialsData(){
        viewModelScope.launch {
            tutorialsState.value = fetchTutorials()
        }
    }
}