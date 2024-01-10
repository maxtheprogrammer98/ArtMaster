package com.example.artmaster.paths

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
    // testing
    Log.i("progress bar", "percentage : $percentageDone")
    Log.i("progress bar", "path tutos : ${pathTutorials.size}")
    Log.i("progress bar", "user done: ${userDoneTutorials.size}")

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
            progress = percentageDone,
            color = Color.Green,
            trackColor = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape))
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