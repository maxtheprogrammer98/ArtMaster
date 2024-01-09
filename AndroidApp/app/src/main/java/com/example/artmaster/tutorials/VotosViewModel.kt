package com.example.artmaster.tutorials

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * retrieves the votes related to the selected tutorial
 */
class VotosViewModel : ViewModel(){
    // base variable that stores fetched documents
    @SuppressLint("MutableCollectionMutableState")
    val stateVotes = mutableStateOf(
        arrayListOf<VotosModels>()
    )

    // base variable that stores the filter parameter
    private var tutorialID : String = ""
    // setter
    fun setTutorialID(ID : String){
        tutorialID = ID
        getDataVotes(tutorialID)
    }

    /**
     * function that fetches data asynchronously
     */
    suspend fun fetchVotes(tutorialID: String) : ArrayList<VotosModels>{
        // val that stores fetch data temporaly
        val fetchDocuments = ArrayList<VotosModels>()
        // instantiating firebase
        val db = Firebase.firestore
        // collection reference
        val votosCollection = db.collection("votos")
        // executing request and handling results
        try {
            votosCollection
                .whereEqualTo("tutorialID", tutorialID)
                .get()
                .await()
                .map {
                    // deserializing document
                    val doc = it.toObject(VotosModels::class.java)
                    // adding document into arraylist
                    fetchDocuments.add(doc)
                }
        } catch (e : FirebaseFirestoreException){
            Log.e("error fb", "error : $e")
        }
        // return statement
        return fetchDocuments
    }

    /**
     * Triggering fetching function
     */
    fun getDataVotes(tutorialID: String){
        viewModelScope.launch {
            stateVotes.value = fetchVotes(tutorialID)
        }
    }


    /**
     * Calculates the average score of the tutorial displayed
     */
    fun calculateAverage() : Int{
        // counter variable
        var counter = 0
        // total scores
        var totalScore = 0
        // result variable
        val finalResult:Int
        // calculating
        stateVotes.value.forEach { elem ->
            // increasing counter
            counter++
            // adding score to total
            totalScore += elem.puntuacion
        }
        // final result
        finalResult = totalScore / counter
        // returning result
        return finalResult
    }

}