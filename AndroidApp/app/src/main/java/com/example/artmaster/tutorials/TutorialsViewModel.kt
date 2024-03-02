package com.example.artmaster.tutorials

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * enables the app to deserialize the firebase documents based
 * on the created model
 */
class TutorialsViewModel : ViewModel(){
    // variable that stores the fetch data
    @SuppressLint("MutableCollectionMutableState")
    val stateTutorials = mutableStateOf(
        arrayListOf<TutorialsModels>()
    )

    /**
     * function that fetchs data asynchronically
     */
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
                    val documentID = it.id
                    val result = it.toObject(TutorialsModels::class.java).copy(id = documentID)
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

    /**
     * Gets the matching tutorials based on the input and path specified
     */
    suspend fun filterModels(input:String, path:String) : ArrayList<TutorialsModels>{
        // temporal storage
        val queryTutorials = ArrayList<TutorialsModels>()
        // instantiating firebase
        val db = Firebase.firestore
        // collection reference
        val tutorialsCollection = db.collection("tutoriales")
        // get request
        try {
            tutorialsCollection
                .whereEqualTo("nombre", input)
                .whereEqualTo("rutaNombre", path)
                .get()
                .await()
                .map {
                    val result = it.toObject(TutorialsModels::class.java)
                    queryTutorials.add(result)
                }
        } catch (e : FirebaseFirestoreException){
            Log.e("fb_error", "error while executing fetch request", e)
        }
        // returning queried models
        return queryTutorials
    }

    /**
     * triggers the function that filters the tutorials based on the input and path provided
     */
    fun getFilterData(input:String, path:String){
        viewModelScope.launch {
            stateTutorials.value = filterModels(
                input = input,
                path = path)
        }
    }

    /**
     * fetchs tutorials based on their specific path
     */
    suspend fun getTutorialsFromPath(path:String) : ArrayList<TutorialsModels>{
        // reference variable
        var tutorialsFromPath = ArrayList<TutorialsModels>()
        // instantiating DB
        val db = Firebase.firestore
        // collection reference
        val collectionRef = db.collection("tutoriales")
        // executing request and handling possible errors
        try {
            collectionRef
                // filter specification
                .whereEqualTo("rutaNombre", path)
                // get request
                .get()
                // waiting for server response
                .await()
                // deserializing documents
                .map {
                    val result = it.toObject(TutorialsModels::class.java)
                    tutorialsFromPath.add(result)
                }
        } catch (e : FirebaseFirestoreException){
            Log.e("get_request_tutos", "get request unsuccessful", e)
        }
        // return statement
        return tutorialsFromPath
    }

    /**
     * executes async request that fetches tutorials based on their path
     */
    fun fetchTutorialsFiltered(path: String){
        viewModelScope.launch {
            stateTutorials.value = getTutorialsFromPath(path)
        }
    }

}