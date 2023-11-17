package com.example.artmaster

import android.content.Intent
import android.app.Application
import android.content.Context
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artmaster.graphicElements.ItemsGenerator
import com.example.artmaster.login.Login
import com.example.artmaster.profile.ProfileActivity
import com.example.artmaster.register.RegisterActivity
import com.example.artmaster.ui.theme.ArtMasterTheme
import com.example.artmaster.ui.theme.darkBlue
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlin.math.exp

open class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                }
            }
            TobBarMain()
        }
    }

    @Composable
    fun AddHeader(){
        Row (modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.mipmap.header),
                contentDescription = stringResource(R.string.header),
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(0.dp, 60.dp)
            )
        }
    }

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

        // list that containing all the menu options
        val allMenuOptions = listOf<ItemsGenerator>(inicioOption,
            rutasOption,
            favsOption,
            notasOption,
            registroOption,
            loginOption,
            perfilOption,
            adminOption)


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
            }
        }
    }

    /**
     * validates which menu option was selected
     */
    fun validateSelecOption(optionSelec : String){
        when(optionSelec){
            "Login / Logout" ->{
                Intent(applicationContext, Login::class.java).also {
                    startActivity(it)
                }
            }
            "Registro" -> {
                Intent(applicationContext, RegisterActivity::class.java).also {
                    startActivity(it)
                }
            }
            "Perfil" -> {
                Intent(applicationContext, ProfileActivity::class.java).also {
                    startActivity(it)
                }
            }else ->{
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
            true
        )

        val itemRutas = ItemsGenerator(
            "rutas",
            "seccion rutas",
            Icons.Filled.Create,
            false,
            false
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
                .wrapContentSize()
                .background(colorResource(id = R.color.dark_blue)),
        ){
            sectionsApp.forEach {
                    section -> NavigationBarItem(
                selected = screenSelected.any { it.name == section.name },
                onClick = {  },
                icon = {
                    Icon(
                        imageVector = section.icon,
                        contentDescription = section.contentDescription,
                        tint = colorResource(id = R.color.white))
                },
                label = {
                    Text(
                        text = section.name,
                        color = colorResource(id = R.color.white))
                }
            )
            }

        }


    }
}




