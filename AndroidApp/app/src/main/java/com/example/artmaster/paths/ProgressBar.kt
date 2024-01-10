package com.example.artmaster.paths

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.artmaster.user.UsersViewModel

/**
 * it shows how much progress the user has made in each path
 */
@Composable
fun CustomLinearProgressBar(
    pathID : String,
    usersViewModel: UsersViewModel = viewModel(),
    pathsViewModel: PathsViewModel = viewModel()
){
    // base variables
    val pathTutorials = pathsViewModel.getSpecificData(pathID)
    val userDoneTutorials = usersViewModel.userStateProfile.value.completados
    val percentageDone : Float
}

/**
 * performs the needed calculations to obtain the percentage
 * and returns the result as a float value
 */
fun calculatePercentageDone(userDone : ArrayList<String>, pathTutorials : ArrayList<String>) : Float{
    // base variables
    val numberOfTutorials = pathTutorials.size
    var occurrances = 0
    val percentageDone : Float
    // counting occurrances
    //TODO: Optimize this process! (perhaps merging both arrays and counting occurrances)
    pathTutorials.forEach { tutorialPath ->
        userDone.forEach { tutorialUser ->
            if (tutorialPath.equals(tutorialUser)){
                occurrances++
            }
        }
    }
    // calculating percentage
    percentageDone = ((occurrances * 100) / numberOfTutorials).toFloat()
    // return statement
    return percentageDone

}