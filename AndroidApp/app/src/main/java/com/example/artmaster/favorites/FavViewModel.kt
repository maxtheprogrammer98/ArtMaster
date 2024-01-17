package com.example.artmaster.favorites

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artmaster.tutorials.TutorialsModels
import com.example.artmaster.user.GetUserInfoAuth
import com.example.artmaster.user.UserModels
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * retrieves the fav tutorials selected by the current user
 */
class FavViewModel : ViewModel(),GetUserInfoAuth {
    // base variables
    @SuppressLint("MutableCollectionMutableState")
    var tutorialsModels = mutableStateOf(ArrayList<TutorialsModels>())
    @SuppressLint("MutableCollectionMutableState")
    var userFavs = mutableStateOf(ArrayList<String>())
    val userEmail = getCurrentUserEmail()

    //initializing
    init {
        getFavsUser()
    }
    
    /**
     * executes an asynchronous fetch request to firestore in order
     * to retrieve the user's fav tutorials
     */
    private suspend fun fetchUserFavs(userEmail : String) : ArrayList<String>{
        // base variable
        var favs = ArrayList<String>()
        // instantiating firebase
        val db = Firebase.firestore
        // collection reference
        val collectionRef = db.collection("usuarios")
        // executing get request
        try {
            collectionRef
                .whereEqualTo("email", userEmail)
                .get()
                .await()
                .map {
                    // deserializing document
                    val documentID = it.id
                    val result = it.toObject(UserModels::class.java).copy(id = documentID)
                    favs = result.completados
                }
        } catch (e : FirebaseFirestoreException){
            // handling errors
            Log.e("favs" , "error while fetching user data", e)
        }
        //testing
        Log.i("favs VM", "favs array's size: ${favs.size}")
        // return statement
        return favs
    }

    /**
     * retrieves the fav tutorials from firestore
     */
    private suspend fun fetchTutorialsFav(userFavs :ArrayList<String>) : ArrayList<TutorialsModels>{
        //testing
        Log.i("test VM", "size argument: ${userFavs.size}")
        // base variable
        var tutorials = ArrayList<TutorialsModels>()
        // instantiating firestore
        val db = Firebase.firestore
        // referencing collection
        val collectionRef = db.collection("tutoriales")
        // GET REQUEST
        // handling results
        try {
            collectionRef
                // filtering documents
                .whereIn(FieldPath.documentId(),userFavs)
                // executing request
                .get()
                // waiting for server's response
                .await()
                // retrieving documents
                .documents
                // deserializing docs
                .map {
                    val result = it.toObject(TutorialsModels::class.java) as TutorialsModels
                    // adding to reference array
                    tutorials.add(result)
                }
        }catch (e : FirebaseFirestoreException){
            // displaying error
            Log.e("favVM", "failed server connection", e)
        }
        // testing
        Log.i("favs VM", "tutorials array's size: ${tutorials.size}")
        // return statement
        return tutorials

    }

    /**
     * launches asynchronous function to fetch the fav tutorials
     */
    fun getFavsUser(){
        viewModelScope.launch {
            userFavs.value = fetchUserFavs(userEmail)
            tutorialsModels.value = fetchTutorialsFav(userFavs.value)
        }
    }


}