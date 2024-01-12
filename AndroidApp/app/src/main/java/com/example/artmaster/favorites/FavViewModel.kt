package com.example.artmaster.favorites

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artmaster.tutorials.TutorialsModels
import com.example.artmaster.user.GetUserInfoAuth
import com.example.artmaster.user.UserModels
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * retrieves the fav tutorials selected by the current user
 */
class FavViewModel : ViewModel(),GetUserInfoAuth {
    // base variables
    val tutorialsModels = mutableStateOf(ArrayList<TutorialsModels>())
    private val userFavs = mutableStateOf(ArrayList<String>())


    //initializing
    init {
        getFavsUser()
        getFavsTutorials()
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
                    val result = it.toObject(UserModels::class.java)
                    favs = result.completados
                }
        } catch (e : FirebaseFirestoreException){
            // handling errors
            Log.e("favs" , "error while fetching user data", e)
        }
        // return statement
        return favs
    }

    /**
     * launches asynchronous function
     */
    fun getFavsUser(userEmail: String = getCurrentUserEmail()){
        viewModelScope.launch {
            userFavs.value = fetchUserFavs(userEmail)
        }
    }

    /**
     * retrieves the fav tutorials from firestore
     */
    private suspend fun fetchTutorialsFav() : ArrayList<TutorialsModels>{
        // base variable
        var tutorials = ArrayList<TutorialsModels>()
        // instantiating firestore
        val db = Firebase.firestore
        // referencing collection
        val collectionRef = db.collection("tutoriales")
        // executing get request
        try {
            collectionRef
                .whereArrayContains("id", userFavs)
                .get()
                .await()
                .map {
                    // deserializing documents
                    val result = it.toObject(TutorialsModels::class.java)
                    tutorials.add(result)
                }
        } catch (e : FirebaseFirestoreException){
            Log.e("favs", "error fetching tutorials data", e)
        }
        // return statement
        return tutorials
    }


    /**
     * launches ansynchronous request
     */
    fun getFavsTutorials(){
        viewModelScope.launch {
            tutorialsModels.value = fetchTutorialsFav()
        }
    }

}