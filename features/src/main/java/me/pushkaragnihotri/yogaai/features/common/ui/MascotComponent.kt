package me.pushkaragnihotri.yogaai.features.common.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

enum class MascotState {
    HAPPY,
    MEDITATIVE,
    ENCOURAGING
}

@Composable
fun ZenMascot(
    state: MascotState,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    
    // Breathing animation for fluid motion
    val breathingScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (state == MascotState.MEDITATIVE) 3000 else 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Occasional blinking
    val blink by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 4000
                1f at 0
                1f at 3800
                0f at 3900
                1f at 4000
            }
        )
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val faceColor = when (state) {
        MascotState.HAPPY -> primaryColor
        MascotState.MEDITATIVE -> MaterialTheme.colorScheme.secondary
        MascotState.ENCOURAGING -> MaterialTheme.colorScheme.tertiary
    }

    Box(modifier = modifier.size(100.dp).padding(8.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = (size.width / 2) * breathingScale
            
            // Glassmorphic outer glow
            drawCircle(
                color = faceColor.copy(alpha = 0.2f),
                radius = radius * 1.2f,
                center = center
            )
            // Solid mascot body
            drawCircle(
                color = faceColor,
                radius = radius,
                center = center
            )
            
            val eyeOffsetY = size.height * 0.4f
            val eyeOffsetX = size.width * 0.25f
            // If meditative, eyes are closed (scaling down the Y effectively, but here we just draw small/invisible or lines)
            val isClosed = state == MascotState.MEDITATIVE
            
            if (isClosed) {
                // Closed eyes lines
                drawLine(
                    color = Color.White,
                    start = Offset(center.x - eyeOffsetX - size.width * 0.05f, center.y - size.height * 0.05f),
                    end = Offset(center.x - eyeOffsetX + size.width * 0.05f, center.y - size.height * 0.05f),
                    strokeWidth = size.width * 0.03f
                )
                drawLine(
                    color = Color.White,
                    start = Offset(center.x + eyeOffsetX - size.width * 0.05f, center.y - size.height * 0.05f),
                    end = Offset(center.x + eyeOffsetX + size.width * 0.05f, center.y - size.height * 0.05f),
                    strokeWidth = size.width * 0.03f
                )
            } else {
                val eyeRadius = size.width * 0.08f * blink
                drawCircle(
                    color = Color.White,
                    radius = eyeRadius,
                    center = Offset(center.x - eyeOffsetX, center.y - size.height * 0.05f)
                )
                drawCircle(
                    color = Color.White,
                    radius = eyeRadius,
                    center = Offset(center.x + eyeOffsetX, center.y - size.height * 0.05f)
                )
            }
            
            // Mouth based on state
            when (state) {
                MascotState.HAPPY -> {
                    drawArc(
                        color = Color.White,
                        startAngle = 0f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(center.x - eyeOffsetX * 0.8f, center.y + eyeOffsetY * 0.1f),
                        size = androidx.compose.ui.geometry.Size(eyeOffsetX * 1.6f, eyeOffsetX),
                        style = Stroke(width = size.width * 0.04f)
                    )
                }
                MascotState.MEDITATIVE -> {
                     drawLine(
                         color = Color.White,
                         start = Offset(center.x - eyeOffsetX * 0.5f, center.y + eyeOffsetY * 0.4f),
                         end = Offset(center.x + eyeOffsetX * 0.5f, center.y + eyeOffsetY * 0.4f),
                         strokeWidth = size.width * 0.04f
                     )
                }
                MascotState.ENCOURAGING -> {
                     drawArc(
                         color = Color.White,
                         startAngle = 180f,
                         sweepAngle = 180f,
                         useCenter = false,
                         topLeft = Offset(center.x - eyeOffsetX * 0.8f, center.y + eyeOffsetY * 0.6f),
                         size = androidx.compose.ui.geometry.Size(eyeOffsetX * 1.6f, eyeOffsetX * 0.5f),
                         style = Stroke(width = size.width * 0.04f)
                     )
                }
            }
        }
    }
}
