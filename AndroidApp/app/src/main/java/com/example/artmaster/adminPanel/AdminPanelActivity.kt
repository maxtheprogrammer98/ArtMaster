package com.example.artmaster.adminPanel

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artmaster.MainActivity
import com.example.artmaster.R
import com.example.artmaster.adminPaths.PathsActivity
import com.example.artmaster.adminTutorials.TutorialsActivity
import com.example.artmaster.adminUsers.UsersActivity
import com.example.artmaster.ui.theme.ArtMasterTheme


class AdminPanelActivity: MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMasterTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AdminPanelScreen(
                        navigateToUsers = {
                            Intent(applicationContext, UsersActivity::class.java).also {
                                startActivity(it)
                            }
                        },
                        navigateToTutoriales = {
                            Intent(applicationContext, TutorialsActivity::class.java).also {
                                startActivity(it)
                            }
                        },
                        navigateToRutas = {
                            Intent(applicationContext, PathsActivity::class.java).also {
                                startActivity(it)
                            }
                        }
                    )
                }
            }
        }
    }



    @Composable
    fun AdminPanelScreen(
        navigateToUsers: () -> Unit,
        navigateToTutoriales: () -> Unit,
        navigateToRutas: () -> Unit
    ) {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()
        var multiFloatingState by remember {
            mutableStateOf(MultiFloatingState.Collapse)
        }
        val items = listOf(
            MinFabItem(
                icon = ImageBitmap.imageResource(id = R.drawable.ic_add_user),
                label = "Agregar usuarios",
                identifier = Identifier.Users.name
            ),
            MinFabItem(
                icon = ImageBitmap.imageResource(id = R.drawable.ic_add_guide),
                label = "Agregar tutoriales",
                identifier = Identifier.Tutorials.name
            ),
            MinFabItem(
                icon = ImageBitmap.imageResource(id = R.drawable.ic_paths),
                label = "Agregar rutas",
                identifier = Identifier.Paths.name
            ),
        )

        Scaffold(
                scaffoldState = scaffoldState,
                floatingActionButton = {
                    MultiFloatingButton(
                        multiFloatingState = multiFloatingState,
                        onMultiFabStateChange = {
                            multiFloatingState = it
                        },
                        items = items,
                        context = context
                    )
                },
                topBar = {
                    // Custom top bar from the MainActivity
                    super.TobBarMain()
                },
                bottomBar = {
                    // Custom bottom bar from the MainActivity
                    super.BottomBar()
                },

        ) { padding ->

            // Box to hold the main content and background image
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bg05),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    modifier = Modifier
                        .padding(vertical = 42.dp)
                        .padding(16.dp)
                        .background(
                            MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(16.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        item {
                            AdminCard("Usuarios", Icons.Default.Person, navigateToUsers)
                            Spacer(modifier = Modifier.height(42.dp))
                            AdminCard("Tutoriales", Icons.Default.Edit, navigateToTutoriales)
                            Spacer(modifier = Modifier.height(42.dp))
                            AdminCard("Rutas", Icons.Default.Build, navigateToRutas)
                        }
                    }

                }
            }
        }


    }



}



@Composable
fun AdminCard(title: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}
