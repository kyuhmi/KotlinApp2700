package org.kym.todoapp.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BlinkingDotIndicator(
    dotSize: Dp = 16.dp,
    dotColor: Color = Color.Red,
    blinkDurationMillis: Int = 1000
) {
    // create an infinite transition to animate the dot's opacity
    val infiniteTransition = rememberInfiniteTransition(label = "BlinkingDotTransition")

    // animate dot by changing alpha to fully visible and invisible
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = blinkDurationMillis
                1f at 0 // stay visible at start
                1f at blinkDurationMillis / 2 // stay visible at halfway point
                0f at blinkDurationMillis / 2 + 1 // instantly switch to invisible
                0f at blinkDurationMillis // stay invisible for other half
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "BlinkingDotAlpha"
    )

    Box(
        modifier = Modifier
            .size(dotSize)
            .graphicsLayer {
                this.alpha = alpha
            }
            .background(dotColor, CircleShape)
    )
}