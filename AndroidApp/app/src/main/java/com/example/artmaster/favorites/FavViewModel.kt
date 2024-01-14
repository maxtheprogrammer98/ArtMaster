package com.example.artmaster.favorites

import android.util.Log
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
    var tutorialsModels = mutableStateOf(ArrayList<TutorialsModels>())
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
    private fun fetchTutorialsFav() : ArrayList<TutorialsModels>{
        // base variable
        var tutorials = ArrayList<TutorialsModels>()
        // instantiating firestore
        val db = Firebase.firestore
        // referencing collection
        val collectionRef = db.collection("tutoriales")
        try {
            // executing get request over each element
            userFavs.value.forEach { elem ->
                // document reference
                collectionRef.document(elem)
                // get request
                    .get()
                    .addOnCompleteListener { documentSnapshot ->
                        // handling results
                        if (documentSnapshot.isSuccessful){
                            // extracting and deserializing data
                            val data = documentSnapshot.result.toObject(TutorialsModels::class.java) as TutorialsModels
                            // adding it to reference array
                            tutorials.add(data)
                        } else{
                            // displaying error
                            Log.e("favs VM", "error: ${documentSnapshot.exception}")
                        }
                    }
            }
        } catch (e : FirebaseFirestoreException){
            Log.e("favs", "error fetching tutorials data", e)
        }
        // testing
        Log.i("favs VM", "tutorials array's size: ${tutorials.size}")
        // return statement
        return tutorials

    }

    /**
     * launches asynchronous function
     */
    fun getFavsUser(){
        viewModelScope.launch {
            // first, it's necessary to get the user's favs
            userFavs.value = fetchUserFavs(userEmail)
        }.also {
            if (it.isCompleted){
                // testing
                Log.i("favs VM", "userFavs request done!")
                // then the tutorials are fetched
                fetchTutorialsFav()
            }
        }
    }

}