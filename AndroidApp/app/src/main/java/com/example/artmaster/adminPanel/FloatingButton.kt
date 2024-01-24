package com.example.artmaster.adminPanel

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artmaster.adminPaths.DetailPathActivity
import com.example.artmaster.adminTutorials.DetailTutorialActivity
import com.example.artmaster.adminUsers.DetailUserActivity
import com.example.artmaster.notes.NoteActivity


@Composable
fun MultiFloatingButton(
    multiFloatingState: MultiFloatingState,
    onMultiFabStateChange:(MultiFloatingState) -> Unit,
    context: Context,
    items: List<MinFabItem>
){

    val transition = updateTransition(targetState = multiFloatingState, label = "transition")

    val rotate by transition.animateFloat(label = "rotate") {
        if (it == MultiFloatingState.Expanded) 315f else 0f
    }

    val fabScale by transition.animateFloat(label = "fabScale") {
        if (it == MultiFloatingState.Expanded) 36f else 0f
    }

    val alpha by transition.animateFloat(
        label = "alpha",
        transitionSpec = { tween(durationMillis = 50) }
    ) {
        if (it == MultiFloatingState.Expanded) 1f else 0f
    }

    val textShadow by transition.animateDp(
        label = "textShadow",
        transitionSpec = { tween(durationMillis = 50) }
    ) {
        if (it == MultiFloatingState.Expanded) 2.dp else 0.dp
    }

    Column(
        horizontalAlignment = Alignment.End
    ) {
        if (transition.currentState == MultiFloatingState.Expanded) {
            items.forEach {
                MinFab(
                    item = it,
                    onMinFabItemClick = {minFabItem ->
                        when(minFabItem.identifier) {

                            Identifier.Users.name -> {
                                Toast.makeText(context, "Agregar usuarios", Toast.LENGTH_SHORT).show()
                                context.startActivity(Intent(context, DetailUserActivity::class.java))
                            }
                            Identifier.Tutorials.name -> {
                                Toast.makeText(context, "Agregar tutoriales", Toast.LENGTH_SHORT).show()
                                context.startActivity(Intent(context, DetailTutorialActivity::class.java))
                            }
                            Identifier.Paths.name -> {
                                Toast.makeText(context, "Agregar rutas", Toast.LENGTH_SHORT).show()
                                context.startActivity(Intent(context, DetailPathActivity::class.java))
                            }
                        }

                    },
                    alpha = alpha,
                    textShadow = textShadow,
                    fabScale = fabScale
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
        }


        FloatingActionButton(
            onClick = {
                onMultiFabStateChange(
                    if (transition.currentState == MultiFloatingState.Expanded) {
                        MultiFloatingState.Collapse
                    }else {
                        MultiFloatingState.Expanded
                    }
                )
            },
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.rotate(rotate)
            )
        }
    }

}



@Composable
fun MinFab(
    item: MinFabItem,
    alpha: Float,
    textShadow: Dp,
    fabScale: Float,
    showLabel: Boolean = true,
    onMinFabItemClick: (MinFabItem) -> Unit
){
    val buttonColor = MaterialTheme.colorScheme.primary
    val shadow = Color.Black.copy(.5f)

    Row {
        if (showLabel) {
            Text(
                text = item.label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .alpha(
                        animateFloatAsState(
                            targetValue = alpha,
                            animationSpec = tween(50), label = ""
                        ).value
                    )
                    .shadow(textShadow)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(start = 6.dp, end = 6.dp, top = 4.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
        }
        Canvas(modifier = Modifier
            .size(32.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                onClick = {
                    onMinFabItemClick.invoke(item)
                },
                indication = rememberRipple(
                    bounded = false,
                    radius = 20.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            )) {

            drawCircle(
                color = shadow,
                radius = fabScale,
                center = Offset(
                    center.x + 2f,
                    center.y + 2f,
                )
            )

            drawCircle(
                color = buttonColor,
                radius = fabScale
            )

            drawImage(
                image = item.icon,
                topLeft = Offset(
                    center.x - (item.icon.width / 2),
                    center.y - (item.icon.width / 2),
                ),
                alpha = alpha
            )

        }
    }


}