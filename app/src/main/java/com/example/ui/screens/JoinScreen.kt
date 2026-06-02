package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinScreen(
    onJoinSubmit: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var sessionId by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CuteVanillaCream)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- CUTE CLIENT BADGE ---
        Box(
            modifier = Modifier
                .size(72.dp)
                .cartoonShadow(shadowColor = CuteCocoaCharcoal, offsetX = 4.dp, offsetY = 4.dp, shapeRadius = 24.dp)
                .border(3.dp, CuteCocoaCharcoal, RoundedCornerShape(24.dp))
                .background(CuteSkyBlue, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "🎮", 
                fontSize = 32.sp
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = "Join the Game!", 
            fontSize = 28.sp, 
            fontWeight = FontWeight.ExtraBold, 
            color = CuteCocoaCharcoal,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Go to your friend's screen and look for the Code!", 
            fontSize = 13.sp, 
            fontWeight = FontWeight.Bold,
            color = CuteCocoaCharcoal.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))
        
        // --- INPUT: SESSION CODE ---
        Text(
            text = "SESSION CODE (4 LETTERS)",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = CuteCocoaCharcoal,
            modifier = Modifier.align(Alignment.Start).padding(start = 12.dp, bottom = 6.dp)
        )
        
        TextField(
            value = sessionId,
            onValueChange = { sessionId = it.uppercase() },
            placeholder = { Text("e.g. PQ88", color = CuteCocoaCharcoal.copy(alpha = 0.4f), fontWeight = FontWeight.Bold) },
            modifier = Modifier
                .fillMaxWidth()
                .border(3.dp, CuteCocoaCharcoal, CartoonInputShape),
            singleLine = true,
            textStyle = TextStyle(
                fontWeight = FontWeight.ExtraBold, 
                fontSize = 16.sp, 
                color = CuteCocoaCharcoal
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = CuteCloudWhite,
                unfocusedContainerColor = CuteCloudWhite,
                disabledContainerColor = CuteCloudWhite,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = CuteCocoaCharcoal
            ),
            shape = CartoonInputShape
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // --- INPUT: YOUR NICKNAME ---
        Text(
            text = "YOUR GOOFY NICKNAME",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = CuteCocoaCharcoal,
            modifier = Modifier.align(Alignment.Start).padding(start = 12.dp, bottom = 6.dp)
        )
        
        TextField(
            value = userName,
            onValueChange = { userName = it },
            placeholder = { Text("e.g. Paddy Jr", color = CuteCocoaCharcoal.copy(alpha = 0.4f), fontWeight = FontWeight.Bold) },
            modifier = Modifier
                .fillMaxWidth()
                .border(3.dp, CuteCocoaCharcoal, CartoonInputShape),
            singleLine = true,
            textStyle = TextStyle(
                fontWeight = FontWeight.ExtraBold, 
                fontSize = 16.sp, 
                color = CuteCocoaCharcoal
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = CuteCloudWhite,
                unfocusedContainerColor = CuteCloudWhite,
                disabledContainerColor = CuteCloudWhite,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = CuteCocoaCharcoal
            ),
            shape = CartoonInputShape
        )
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // --- BOUNCY CLIENT JOIN ACTION BUTTON ---
        val formValid = sessionId.isNotBlank() && userName.isNotBlank()
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .cartoonShadow(
                    shadowColor = if (formValid) CuteCocoaCharcoal else Color.Transparent, 
                    offsetX = if (formValid) 5.dp else 0.dp, 
                    offsetY = if (formValid) 5.dp else 0.dp, 
                    shapeRadius = 20.dp
                )
                .background(
                    if (formValid) CuteBubblegumPink else CuteCocoaCharcoal.copy(alpha = 0.15f), 
                    CartoonButtonShape
                )
                .border(
                    width = if (formValid) 3.dp else 2.dp, 
                    color = if (formValid) CuteCocoaCharcoal else CuteCocoaCharcoal.copy(alpha = 0.2f), 
                    shape = CartoonButtonShape
                )
                .bouncyClickable(enabled = formValid) {
                    if (formValid) {
                        onJoinSubmit(sessionId, userName)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🚀 CONNECT TO GAME!", 
                fontSize = 16.sp, 
                fontWeight = FontWeight.ExtraBold, 
                color = if (formValid) CuteCloudWhite else CuteCocoaCharcoal.copy(alpha = 0.4f)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(
            onClick = onBack,
            modifier = Modifier.height(48.dp)
        ) {
            Text(
                text = "🎈 Back to Home", 
                fontWeight = FontWeight.ExtraBold, 
                color = CuteCocoaCharcoal.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        }
    }
}
