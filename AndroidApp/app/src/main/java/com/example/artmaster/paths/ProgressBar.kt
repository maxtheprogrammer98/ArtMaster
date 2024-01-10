package com.example.artmaster.paths

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    val percentageDone = calculatePercentageDone(userDoneTutorials, pathTutorials)

    // composable wrapper
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(10.dp)
    ){
        // composable bar
        LinearProgressIndicator(
            progress = percentageDone,
            color = Color.Green,
            trackColor = Color.White)
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
    // counting occurrances
    for (element in pathTutorials){
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