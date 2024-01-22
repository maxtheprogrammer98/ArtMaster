package com.example.artmaster.favorites

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artmaster.tutorials.TutorialsModels
import com.example.artmaster.user.GetUserInfoAuth
import com.example.artmaster.user.UserModels
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
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

    /**
     * removes the tutorial from the DB
     */
    fun removeFromFavsDB(
        favID: String,
        userID : String,
        context : Context
    ){
        // 2 - removing tutorial from Firestore DB
        // instantiating db
        val db = Firebase.firestore
        // document reference
        val docRef = db.collection("usuarios").document(userID)
        // updating document
        docRef
            // executing request
            .update("favoritos", FieldValue.arrayRemove(favID))
            // handling results
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    // success message
                    Toast.makeText(
                        context,
                        "tutorial eliminado de favs",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // error message
                    Toast.makeText(
                        context,
                        "no se pudo completar la operacion",
                        Toast.LENGTH_SHORT
                    ).show()
                    // displaying technical message
                    Log.e("favVM", "failed server connection: ${task.exception}")
                }
            }
    }

    /**
     * removes the tutorial from the VM
     * so the change is reflected on the IU as well
     */
    fun removeFromFavVM(
        tutorialID: String
    ){
        // Creating a new list that excludes the item with the matching ID
        val updatedList = tutorialsModels.value.filterNot { tutorial ->
            tutorial.id == tutorialID
        }

        // Updating the state with the new list
        tutorialsModels.value = updatedList as ArrayList<TutorialsModels>
    }


    /**
     * filters the models based on the passed argument from the search bar
     * any tutorial which name contains the specified input will be displayed
     */
    private suspend fun filterTutorialsDB(input : String) : ArrayList<TutorialsModels>{
        //TODO: filter by matching the containing letters rather than the whole word

        // array reference
        val filterTutorials = ArrayList<TutorialsModels>()
        // instantiating firebase
        val db = Firebase.firestore
        // collection reference
        val collectionRef = db.collection("tutoriales")
        // executing GET REQUEST
        try {
            collectionRef
                // filter
                .whereEqualTo("nombre", input)
                // fetching data
                .get()
                // waiting for server's response
                .await()
                // deserializing content
                .map {
                    val result = it.toObject(TutorialsModels::class.java)
                    // adding model to reference array
                    filterTutorials.add(result)
                }

        } catch (e : FirebaseFirestoreException){
            // catching errors
            Log.e("favs_VM", "FAILED SERVER CONNECTION", e)
        }

        // testing
        Log.i("test FAV_VM", "tutorials fetched: ${filterTutorials.size}")

        // return statement
        return filterTutorials
    }

    /**
     * triggers the async function that filters the tutorials
     */
    fun filterSearchBar(input: String){
        viewModelScope.launch {
            tutorialsModels.value = filterTutorialsDB(input)
        }
    }
}