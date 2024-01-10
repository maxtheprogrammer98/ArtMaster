package com.example.artmaster.paths

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
 * this class includes the different functionalities that are needed
 * to fetch data from firestore
 */
class PathsViewModel : ViewModel(){
    // variable that stores the retrieved data
    val state = mutableStateOf(
        arrayListOf<PathsModels>()
    )

    // initializes the fetching method whenever the class is refered to
    init {
        getData()
    }

    // fecthing data
    suspend fun fetchDataFS():ArrayList<PathsModels>{
        // instaintiating FS database
        val db = Firebase.firestore
        // collection reference
        val rutasCollection = db.collection("rutas")
        // retrieved information
        var rutasRetrieved = ArrayList<PathsModels>()

        // get request
        try {
            rutasCollection.get().await().map {
                val result = it.toObject(PathsModels::class.java)
                rutasRetrieved.add(result)
            }

        }catch (e : FirebaseFirestoreException){
            Log.e("error", "error connecting to DB", e)
        }
        // returnes the fetched data
        return rutasRetrieved
    }

    // getting fetched data
    private fun getData(){
        viewModelScope.launch {
            state.value = fetchDataFS()
        }
    }

    /**
     * it returns the tutorials that a specific path contains
     */
    fun getSpecificData(pathID: String) : ArrayList<String>{
        //testing
        Log.i("VMPaths", pathID )
        Log.i("VMPaths", "size 1: ${state.value.size}")
        // base variable
        var filterResult = ArrayList<String>()
        // filter process
        state.value.forEach { elem ->
            if(elem.id.equals(pathID)){
                filterResult = elem.tutorialesID
            }
        }
        // statement
        return filterResult
    }
}