package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }

    // Goofy eye motion animation states
    val infiniteTransition = rememberInfiniteTransition(label = "eye_motion")
    
    // Look left & right offset
    val eyeLookX by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "eye_look"
    )

    // Eye blinking height scale
    val eyeBlinkScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                1.0f at 0
                1.0f at 2600
                0.1f at 2700 // Quick blink starts
                1.0f at 2800 // Blink ends
                1.0f at 3000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "eye_blink"
    )

    // Bouncing entry scale
    val mainScale by animateFloatAsState(
        targetValue = if (startAnimation) 1.0f else 0.1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "main_scale"
    )

    // Goofy gentle wiggling rotation
    val bodyRotation by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wiggle_rot"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2200L) // Cozy visual showtime
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CuteVanillaCream),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .scale(mainScale)
                .rotate(bodyRotation)
        ) {
            // --- GOOFY CARTOON BUZZER MONSTER CREATURE ---
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .cartoonShadow(shadowColor = CuteCocoaCharcoal, offsetX = 8.dp, offsetY = 8.dp, shapeRadius = 40.dp)
                    .border(4.dp, CuteCocoaCharcoal, RoundedCornerShape(40.dp))
                    .clip(RoundedCornerShape(40.dp))
                    .background(CuteBubblegumPink)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Goofy expressive eyeballs
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        // Left Eye
                        Box(
                            modifier = Modifier
                                .size(42.dp, 48.dp)
                                .scale(scaleX = 1f, scaleY = eyeBlinkScale)
                                .border(3.dp, CuteCocoaCharcoal, CircleShape)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            // Pupil looking around
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .offset(x = eyeLookX.dp, y = (-2).dp)
                                    .clip(CircleShape)
                                    .background(CuteCocoaCharcoal)
                            )
                        }

                        // Right Eye
                        Box(
                            modifier = Modifier
                                .size(42.dp, 48.dp)
                                .scale(scaleX = 1f, scaleY = eyeBlinkScale)
                                .border(3.dp, CuteCocoaCharcoal, CircleShape)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            // Pupil looking around
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .offset(x = eyeLookX.dp, y = (-2).dp)
                                    .clip(CircleShape)
                                    .background(CuteCocoaCharcoal)
                            )
                        }
                    }

                    // Smiling goofy mouth below nose
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(18.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            // Draw a joyful cartoon smile
                            val path = androidx.compose.ui.graphics.Path().apply {
                                moveTo(0f, 0f)
                                quadraticTo(
                                    size.width / 2f, size.height * 1.5f,
                                    size.width, 0f
                                )
                            }
                            drawPath(
                                path = path,
                                color = CuteCocoaCharcoal,
                                style = androidx.compose.ui.graphics.drawscope.Stroke(
                                    width = 4.dp.toPx(),
                                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- BOUNCY CARTOON TITLE ---
            Box(contentAlignment = Alignment.Center) {
                // Background deep yellow block shadow text
                Text(
                    text = "PaddyBuzz",
                    color = CuteCocoaCharcoal,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.offset(x = 3.dp, y = 4.dp)
                )
                // Main vibrant orange text
                Text(
                    text = "PaddyBuzz",
                    color = CuteSunnyYellow,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Cheerful description
            Text(
                text = "TASTY BUZZER SESSIONS // v2.0",
                color = CuteCocoaCharcoal.copy(alpha = 0.6f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}
