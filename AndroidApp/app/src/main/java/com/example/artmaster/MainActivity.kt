package com.example.artmaster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.twotone.PlayArrow
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.artmaster.adminPanel.AdminPanelActivity
import com.example.artmaster.aisection.AiAssistantActivityPreview
import com.example.artmaster.favorites.FavActivity
import com.example.artmaster.graphicElements.ItemsGenerator
import com.example.artmaster.home.HomeActivity
import com.example.artmaster.login.Login
import com.example.artmaster.login.Logout
import com.example.artmaster.notes.NoteActivity
import com.example.artmaster.paths.PathsActivity
import com.example.artmaster.profile.ProfileActivity
import com.example.artmaster.register.RegisterActivity
import com.example.artmaster.ui.theme.ArtMasterTheme
import com.example.artmaster.ui.theme.darkBlueA
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * this class contains the topbar and bottombar menu
 * which can be accessed via inheretance
 */
open class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            ArtMasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                }
                TobBarMain()
                BottomBar()
            }
        }
    }

    //TODO: Research how to store data locally, in order to implement it here
    //TODO: Encapsulate variables and functions, general review

    //user's role (visitor / user / admin)
    private var usersRole = ""

    /**
     * creates a topbar with a dropdown menu
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    //@Preview
    fun TobBarMain(){

        // 1 - creating flag variable to toggle menu
        var expanded by remember {
            mutableStateOf(false)
        }

        // 2 - creating objets to populate dropdown menu
        val inicioOption = ItemsGenerator(
            stringResource(id = R.string.inicio),
            stringResource(id = R.string.inicio),
            Icons.Filled.Home,
            false,
            true
        )

        val rutasOption = ItemsGenerator(
            stringResource(id = R.string.rutas),
            stringResource(id = R.string.rutas),
            Icons.Filled.Create,
            false,
            true
        )

        val favsOption = ItemsGenerator(
            stringResource(id = R.string.favoritos),
            stringResource(id = R.string.favoritos),
            Icons.Filled.Favorite,
            false,
            false
        )

        val notasOption = ItemsGenerator(
            stringResource(id = R.string.notas),
            stringResource(id = R.string.notas),
            Icons.Filled.DateRange,
            false,
            false
        )

        val loginOption = ItemsGenerator(
            stringResource(id = R.string.login),
            stringResource(id = R.string.login),
            Icons.Filled.Person,
            false,
            true
        )

        val registroOption = ItemsGenerator(
            stringResource(id = R.string.registro),
            stringResource(id = R.string.registro),
            Icons.Filled.AddCircle,
            false,
            true
        )

        val perfilOption = ItemsGenerator(
            stringResource(id = R.string.perfil),
            stringResource(id = R.string.perfil),
            Icons.Filled.AccountCircle,
            false,
            false
        )

        val adminOption = ItemsGenerator(
            stringResource(id = R.string.panel_admin),
            stringResource(id = R.string.panel_admin),
            Icons.Filled.Warning,
            true,
            false
        )

        val IAassistant = ItemsGenerator(
            stringResource(id = R.string.AI_assistant),
            stringResource(id = R.string.AI_assistant),
            Icons.TwoTone.PlayArrow,
            false,
            false
        )

        // list that containing all the menu options (admin / users)
        val allMenuOptions = listOf<ItemsGenerator>(inicioOption,
            rutasOption,
            favsOption,
            notasOption,
            registroOption,
            loginOption,
            perfilOption,
            adminOption,
            IAassistant)

        // determing the user's role to display menu accordingly
        // prior to creating topbar
        getCurrentUserFB()

        // 3 - creating TopBar
        TopAppBar(
            title = {
                Text(
                    text = "ArtMaster",
                    textAlign = TextAlign.End)
            },

            modifier = Modifier
                .fillMaxWidth(),

            actions = {
                // changes flag state
                IconButton(onClick = {expanded = !expanded}) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "icono menu")
                }
            },

            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = colorResource(id = R.color.dark_blue),
                titleContentColor = colorResource(id = R.color.white)
            )
        )

        // 4 - creating dropDownMenu
        Box(
            modifier = Modifier.absoluteOffset(0.dp,60.dp)
        ){
            DropdownMenu(
                expanded =  expanded,
                onDismissRequest = { expanded = false},
                modifier = Modifier
                    .fillMaxWidth()){
                //dynamically created options
                if (usersRole.equals("admin")){
                    // all sections displayed
                    allMenuOptions.forEach {
                            option -> DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = option.icon ,
                                contentDescription = option.contentDescription,
                                modifier = Modifier.padding(15.dp, 0.dp))
                        },
                        text = {
                            Text(text = option.name);
                        },
                        onClick = {
                            validateSelecOption(option.name)
                        }
                    )
                    }
                }else if(usersRole.equals("user")){
                    // the admin panel is not shown
                    allMenuOptions.filter { !it.onlyAdmin }.forEach { option ->
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    imageVector = option.icon ,
                                    contentDescription = option.contentDescription,
                                    modifier = Modifier.padding(15.dp, 0.dp))
                            },
                            text = {
                                Text(text = option.name);
                            },
                            onClick = {
                                validateSelecOption(option.name)
                            }
                        )
                    }
                } else{
                    // displays only the sections available for visitors
                    allMenuOptions.filter {it.visitorCanAccess }.forEach { option ->
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    imageVector = option.icon ,
                                    contentDescription = option.contentDescription,
                                    modifier = Modifier.padding(15.dp, 0.dp))
                            },
                            text = {
                                Text(text = option.name);
                            },
                            onClick = {
                                validateSelecOption(option.name)
                            }
                        )
                    }
                }
            }
        }
    }

    /**
     * validates which menu option was selected
     */
    fun validateSelecOption(optionSelec : String){
        when(optionSelec){
            "Login / Logout" ->{
                if (isUserLogged()){
                    // if user's logged in, then it's redirected to logout
                    Intent(applicationContext, Logout::class.java).also {
                        startActivity(it)
                    }
                } else {
                    // otherwise it's redirected to login section
                    Intent(applicationContext, Login::class.java).also {
                        startActivity(it)
                    }
                }
            }
            "Registro" -> {
                if(!isUserLogged()){
                    // visitor not logged
                    Intent(applicationContext, RegisterActivity::class.java).also {
                        startActivity(it)
                    }
                } else{
                    //if the user's already logged, he cannot access to this section
                    Toast.makeText(
                        applicationContext,
                        "ya estas registrado!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            "Perfil" -> {
                Intent(applicationContext, ProfileActivity::class.java).also {
                    startActivity(it)
                }
            }
            "Notas" -> {
                Intent(applicationContext, NoteActivity::class.java).also {
                    startActivity(it)
                }
            }
            "Panel Admin" -> {
                Intent(applicationContext, AdminPanelActivity::class.java).also {
                    startActivity(it)
                }
            }
            "Inicio" ->{
                Intent(applicationContext, HomeActivity::class.java).also {
                    startActivity(it)
                }
            }
            "Rutas" ->{
                Intent(applicationContext, PathsActivity::class.java).also {
                    startActivity(it)
                }
            }
            "Favoritos" ->{
                Intent(applicationContext, FavActivity::class.java).also {
                    startActivity(it)
                }
            }
            "Asistente IA" ->{
                Intent(applicationContext, AiAssistantActivityPreview::class.java).also {
                    startActivity(it)
                }
            }
            else ->{
            Toast.makeText(applicationContext,
                "la seccion aun se encuentra en construccion",
                Toast.LENGTH_SHORT).show()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    //@Preview
    fun BottomBar(){
        // 1 - creating object items
        val itemInicio = ItemsGenerator(
            "inicio",
            "seccion inicio",
            Icons.Filled.Home,
            false,
            true)

        val itemFavs = ItemsGenerator(
            "favs",
            "seccion favoritos",
            Icons.Filled.Favorite,
            false,
            false
        )

        val itemRutas = ItemsGenerator(
            "rutas",
            "seccion rutas",
            Icons.Filled.Create,
            false,
            true
        )

        // 2 - storing items in arrayList (to iterate it later on)
        val sectionsApp = listOf<ItemsGenerator>(itemInicio,itemFavs,itemRutas)

        // 3 - variable that holds the current screen selected
        val screenSelected by remember {
            mutableStateOf(sectionsApp)
        }

        // 4 - creating menu and its items based on the previous list
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(),
            containerColor = darkBlueA
        ){
            if (usersRole.equals("user") || usersRole.equals("admin")){
                // displays all icons
                sectionsApp.forEach { section -> NavigationBarItem(
                    selected = screenSelected.any { it.name == section.name },
                    onClick = {
                        val intent = navigateToBottomBar(section.name);
                        applicationContext.startActivity(intent)
                    },
                    icon = {
                        Icon(
                            imageVector = section.icon,
                            contentDescription = section.contentDescription,
                            tint = colorResource(id = R.color.white))
                    },
                    label = {
                        Text(
                            text = section.name,
                            color = Color.White)
                    },
                    modifier = Modifier.background(colorResource(id = R.color.dark_blue))
                )
                }
            } else {
                // otherwise only home & learning paths are shown
                sectionsApp.filter{ it.visitorCanAccess}.forEach {
                        section -> NavigationBarItem(
                    selected = screenSelected.any { it.name == section.name },
                    onClick = {
                        val intent = navigateToBottomBar(section.name);
                        applicationContext.startActivity(intent)
                    },
                    icon = {
                        Icon(
                            imageVector = section.icon,
                            contentDescription = section.contentDescription)
                    },
                    label = {
                        Text(
                            text = section.name,
                            color = Color.White)
                    })
                }
            }
        }
    }

    /**
     * obtains the current user (if already signed in)
     */
    fun getCurrentUserFB(){
        // instancing firebase auth
        val user = Firebase.auth.currentUser
        // gettind user's ID
        var userID = ""
        // if user is signed in
        if(user != null){
            user.let {
                userID = user.uid
                Log.i("test", "user identified")}
            isAdmin(userID)
        } else{
            usersRole = "visitor"
            Log.i("test", "visitor")
        }
    }

    /**
     * determines whether the user is loged in or not
     */
    fun isUserLogged():Boolean{
        // variable flag
        var loggedFlag:Boolean
        // instantiating firebase auth service
        val authServiceUser = Firebase.auth.currentUser
        // validating if there's a current user loged in
        if(authServiceUser != null){
            loggedFlag = true
            return loggedFlag
        } else {
            loggedFlag = false
            return loggedFlag
        }
    }

    /**
     * determines whether the user is an admin in order to enable him to acess to the admin panel
     */
    fun isAdmin(userID : String){
        Log.i("testing", "function triggered")
        // instanciating DB
        val db = Firebase.firestore
        // creating document reference
        val docRef = db.collection("usuarios").document(userID)
        // GET REQUEST
        docRef.get()
            // verfies DB connection
            .addOnSuccessListener { document ->
                if (document != null){
                    // extracing document
                    val documentData = document.data
                    // determining user's role
                    val isUserAdmin = documentData?.get("isAdmin")
                    Log.i("testing", "is admin?: " + isUserAdmin.toString())
                    // assigning user's role to display menu accordingly
                    if (isUserAdmin == true){
                        usersRole = "admin"
                        Log.i("testing", "it's admin")
                    } else {
                        usersRole = "user"
                        Log.i("testing", "it's user")
                    }
                }
            }
    }

    // validates which section was clicked
    fun navigateToBottomBar(sectionName : String) : Intent{
        // base variable
        val intent : Intent
        // validation process
        if (sectionName.equals("favs")){
            intent = Intent(applicationContext,FavActivity::class.java)

        } else if (sectionName.equals("rutas")){
            intent = Intent(applicationContext,PathsActivity::class.java)

        } else if(sectionName.equals("inicio")){
            intent = Intent(applicationContext,HomeActivity::class.java)

        } else {
            intent = Intent(applicationContext,HomeActivity::class.java)
        }
        // return statement
        return intent
    }

}




