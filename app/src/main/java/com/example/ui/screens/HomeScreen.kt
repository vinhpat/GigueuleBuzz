package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
object JoinRoute

@Serializable
object SessionRoute

@Serializable
object CreateSessionRoute

@Serializable
object SplashRoute

@Composable
fun HomeScreen(
    onCreateSession: () -> Unit,
    onJoinSession: () -> Unit
) {
    // Infinite transition for gentle laidback floaty wiggling
    val infiniteTransition = rememberInfiniteTransition(label = "home_float")
    
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating"
    )

    val pinkWiggle by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pink_wiggle"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CuteVanillaCream)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- GOOFY FLOATING BADGE CHARACTER ---
        Box(
            modifier = Modifier
                .offset(y = floatOffset.dp)
                .rotate(pinkWiggle)
                .size(100.dp)
                .cartoonShadow(shadowColor = CuteCocoaCharcoal, offsetX = 6.dp, offsetY = 6.dp, shapeRadius = 32.dp)
                .border(4.dp, CuteCocoaCharcoal, RoundedCornerShape(32.dp))
                .background(CuteBubblegumPink, RoundedCornerShape(32.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Cute smiley face inside the badge
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.size(10.dp, 12.dp).background(CuteCocoaCharcoal, CircleShape))
                    Box(modifier = Modifier.size(10.dp, 12.dp).background(CuteCocoaCharcoal, CircleShape))
                }
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .size(24.dp, 10.dp)
                        .background(CuteSunnyYellow, RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // --- BRAND TITLE SECTION ---
        Box(contentAlignment = Alignment.Center) {
            // Text Shadow
            Text(
                text = "PaddyBuzz!",
                fontSize = 44.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CuteCocoaCharcoal,
                textAlign = TextAlign.Center,
                modifier = Modifier.offset(x = 4.dp, y = 4.dp)
            )
            Text(
                text = "PaddyBuzz!",
                fontSize = 44.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CuteSunnyYellow,
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Cozy Laidback Subheading
        Text(
            text = "🎈 TAP, BUZZ, AND WIN COZILY! 🎈",
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold,
            color = CuteCocoaCharcoal.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(64.dp))
        
        // --- 3D CARTOON BUTTON 1: HOST SESSION ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .cartoonShadow(shadowColor = CuteCocoaCharcoal, offsetX = 6.dp, offsetY = 6.dp, shapeRadius = 20.dp)
                .background(CuteManaColorYellow, CartoonButtonShape) // Using custom yellow fallback
                .border(3.dp, CuteCocoaCharcoal, CartoonButtonShape)
                .bouncyClickable(onClick = onCreateSession),
            contentAlignment = Alignment.Center
        ) {
            // Sunny yellow background button body
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CuteSunnyYellow, CartoonButtonShape),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "👑 HOST MASTER PANEL", 
                        fontSize = 16.sp, 
                        fontWeight = FontWeight.ExtraBold, 
                        color = CuteCocoaCharcoal
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // --- 3D CARTOON BUTTON 2: JOIN SESSION ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .cartoonShadow(shadowColor = CuteCocoaCharcoal, offsetX = 6.dp, offsetY = 6.dp, shapeRadius = 20.dp)
                .background(CuteSkyBlue, CartoonButtonShape)
                .border(3.dp, CuteCocoaCharcoal, CartoonButtonShape)
                .bouncyClickable(onClick = onJoinSession),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🎮 JOIN FRIENDS COZILY", 
                    fontSize = 16.sp, 
                    fontWeight = FontWeight.ExtraBold, 
                    color = CuteCocoaCharcoal
                )
            }
        }
        
        Spacer(modifier = Modifier.height(56.dp))
        
        // --- LAIDBACK FOOTER ---
        Card(
            colors = CardDefaults.cardColors(containerColor = CuteCloudWhite),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .border(2.dp, CuteCocoaCharcoal, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "STATUS // ALL CORES FLUFFY AND HAPPY",
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CuteLimeSoda,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Help variable to provide colors if not found
private val CuteManaColorYellow = Color(0xFFFFCC00)
