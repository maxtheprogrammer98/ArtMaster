package com.example.artmaster.tutorials

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.artmaster.MainActivity
import com.example.artmaster.R
import com.example.artmaster.paths.CustomCiricularProgressBar
import com.example.artmaster.user.UsersViewModel

class TutorialsPreviewActivity : MainActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // receiving data from intent
        val namePath = intent.getStringExtra("NAME_PATH") as String
        val idPath = intent.getStringExtra("ID_PATH") as String

        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                TutorialsLayout(namePath, idPath)
            }
        }
    }

    /**
     * creates the entire layout for this section
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun TutorialsLayout(
        pathName:String,
        pathID:String,
        tutorialsViewModel: TutorialsViewModel = viewModel(),
        usersViewModel: UsersViewModel = viewModel()){
        // ------------------------ VARIABLES ----------------------//
        val scrollingState = rememberScrollState()
        val userModel = usersViewModel.userStateProfile.value
        // updadting state based on choosen path
        tutorialsViewModel.fetchTutorialsFiltered(pathName)
        // ------------------------ MAIN LAYOUT ----------------------//
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),

            topBar = {
                super.TobBarMain()
            },

            bottomBar = {
                super.BottomBar()
            }
        ){
            //--------------------- MAIN CONTAINER -------------------//
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollingState)
                    .padding(0.dp, 60.dp)
            ){
                // adding wooden background
                Image(
                    painter = painterResource(id = R.mipmap.fondo6),
                    contentDescription = stringResource(id = R.string.fondo),
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.matchParentSize())

                // general wrapper
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ){
                    // widget that shows progress
                    CustomCiricularProgressBar(IDpath = pathID)

                    // creates search bar that interacts with the view model
                    CreateSerachBar(pathName = pathName)

                    // generates cards dynamically based on VM state
                    if (tutorialsViewModel.stateTutorials.value.isNotEmpty()){
                        tutorialsViewModel.stateTutorials.value.forEach {
                                model -> CardsTutorials(
                            tutorialModel = model,
                            userModel = userModel,
                            navigateTo = {
                                //intent specification
                                val intent = Intent(applicationContext,TutorialsContentActivity::class.java)
                                intent.putExtra("TUTORIAL_DATA", model)
                                // starting following activity
                                startActivity(intent)
                                // finishing current activity
                                finish()
                            })
                        }
                    }
                    
                    // spacer
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }
    }
}