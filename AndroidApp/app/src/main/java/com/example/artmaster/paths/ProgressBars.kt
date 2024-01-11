package com.example.artmaster.paths

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.artmaster.user.UsersViewModel

/**
 * it shows how much progress the user has made in each path
 */
@Composable
fun CustomLinearProgressBar(
    pathID : String,
    usersViewModel: UsersViewModel = viewModel(),
    pathsViewModel: PathsViewModel = viewModel(),
){
    // base variables
    val pathTutorials = pathsViewModel.getSpecificData(pathID)
    val userDoneTutorials = usersViewModel.userStateProfile.value.completados
    val percentageDone = calculatePercentageDone(userDoneTutorials, pathTutorials) / 100

    //animation properties
    val animatedProgress = animateFloatAsState(
        targetValue = percentageDone,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec).value
    
    // progress text indicator
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ){
        Text(text = "progreso: ${(percentageDone*100).toInt()}%")
    }

    // composable wrapper
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(10.dp)
            .wrapContentSize(Alignment.Center)
    ){
        // composable bar
        LinearProgressIndicator(
            progress = animatedProgress,
            color = Color.Green,
            trackColor = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape))
    }

}

/**
 * shows the progress in the path through a circular indicator
 * @param IDpath
 * it's necessary to specify the path in order to get the needed data
 */
@Composable
fun CustomCiricularProgressBar(
    IDpath : String,
    pathsViewModel: PathsViewModel = viewModel(),
    usersViewModel: UsersViewModel = viewModel()
){
    // extracting data from view models
    val userVM = usersViewModel.userStateProfile.value.completados
    val pathsVM = pathsViewModel.getSpecificData(IDpath)
    val percentageDone = calculatePercentageDone(userVM,pathsVM)

    // general wrapper
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
            .padding(0.dp, 20.dp, 0.dp, 0.dp)
    ){

        // animation properties (which includes progress percentage)
        val animatedProgress = animateFloatAsState(
            targetValue = percentageDone / 100,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec).value

        // text wrapper
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
        ){
            // text indicator
            Text(
                text = "progreso: $percentageDone%",
                modifier = Modifier.padding(20.dp),
                textAlign = TextAlign.Center,
                fontSize = 10.sp)
        }

        //widget wrapper
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ){
            // widget bar
            CircularProgressIndicator(
                progress = animatedProgress,
                color = Color.Green,
                strokeWidth = 10.dp,
                modifier = Modifier.size(100.dp))
        }
    }
}

/**
 * performs the needed calculations to obtain the percentage
 * and returns the result as a float value
 */
fun calculatePercentageDone(userDone : ArrayList<String>, pathTutorials : ArrayList<String>) : Float{
    // base variables
    val numberOfTutorials = pathTutorials.size
    val percentageDone : Float
    val elementCountMap = mutableMapOf<Any, Int>()
    val occurrances : Int
    val mergedArray = userDone + pathTutorials
    // counting occurrances
    for (element in mergedArray){
        elementCountMap[element] = elementCountMap.getOrDefault(element,0)+1
    }
    // defining number of occurrances
    occurrances = elementCountMap.count { it.value > 1 }
    // calculating percentage value
    if (numberOfTutorials != 0){
        percentageDone = ((occurrances * 100) / numberOfTutorials).toFloat()
    } else {
        percentageDone = 0f
    }
    // return statement
    return percentageDone
}