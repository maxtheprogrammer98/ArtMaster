package com.example.artmaster.tutorials

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * enables the app to deserialize the firebase documents based
 * on the created model
 */
class TutorialsViewModel : ViewModel(){
    // variable that stores the fetch data
    val stateTutorials = mutableStateOf(
        arrayListOf<TutorialsModels>()
    )

    //initializing function that returns data
    init {
        getData()
    }

    //function that fetchs data asynchronically
    suspend fun fetchTutorialsDocs() : ArrayList<TutorialsModels>{
        // variable that stores fetched documents
        val fetchedDocuments = ArrayList<TutorialsModels>()
        // instantiating firebase
        val db = Firebase.firestore
        // referencing collection
        val collectionRef = db.collection("tutoriales")
        // fetching process
        try {
            collectionRef
                //TODO: research how to filter directly from here:
                //.whereEqualTo("nombre","" )
                .get()
                .await()
                .map {
                    val result = it.toObject(TutorialsModels::class.java)
                    fetchedDocuments.add(result)
                }
        } catch ( e : FirebaseFirestoreException){
            Log.e("error", "error while fetching tutorials documents", e)
        }
        //return statement
        return fetchedDocuments
    }

    // process asynchronous request
    fun getData(){
        viewModelScope.launch {
            stateTutorials.value = fetchTutorialsDocs()
        }
    }
}