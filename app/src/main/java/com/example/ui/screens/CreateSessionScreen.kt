package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSessionScreen(
    onCreateSubmit: (String, String, Int) -> Unit,
    onResumeSubmit: (String) -> Unit,
    onBack: () -> Unit
) {
    var resumeSessionId by remember { mutableStateOf("") }
    var sessionName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var maxParticipants by remember { mutableStateOf("10") }
    
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CuteVanillaCream)
            .verticalScroll(scrollState)
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // --- CUTE HOST ICON ---
        Box(
            modifier = Modifier
                .size(72.dp)
                .cartoonShadow(shadowColor = CuteCocoaCharcoal, offsetX = 4.dp, offsetY = 4.dp, shapeRadius = 24.dp)
                .border(3.dp, CuteCocoaCharcoal, RoundedCornerShape(24.dp))
                .background(CuteSunnyYellow, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("👑", fontSize = 36.sp)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Master Console", 
            fontSize = 28.sp, 
            fontWeight = FontWeight.ExtraBold, 
            color = CuteCocoaCharcoal
        )
        Text(
            text = "Create a funny new buzzer room or reconnect to a previous session!", 
            fontSize = 12.sp, 
            fontWeight = FontWeight.Bold,
            color = CuteCocoaCharcoal.copy(alpha = 0.6f), 
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))
        
        // --- RESUME BLOCK ---
        Text(
            text = "RECONNECT TO SESSION CODE (IF ANY)",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = CuteCocoaCharcoal,
            modifier = Modifier.align(Alignment.Start).padding(start = 12.dp, bottom = 4.dp)
        )
        TextField(
            value = resumeSessionId,
            onValueChange = { resumeSessionId = it.uppercase() },
            placeholder = { Text("e.g. PQ88", color = CuteCocoaCharcoal.copy(alpha = 0.4f), fontWeight = FontWeight.Bold) },
            modifier = Modifier
                .fillMaxWidth()
                .border(3.dp, CuteCocoaCharcoal, CartoonInputShape),
            singleLine = true,
            textStyle = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = CuteCocoaCharcoal),
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
        
        // Cozy divider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.height(2.dp).weight(1f).background(CuteCocoaCharcoal.copy(alpha = 0.15f)))
            Text(
                text = " ⭐ OR CREATE NEW VECTOR ⭐ ", 
                fontSize = 11.sp, 
                fontWeight = FontWeight.ExtraBold, 
                color = CuteCocoaCharcoal.copy(alpha = 0.5f),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Box(modifier = Modifier.height(2.dp).weight(1f).background(CuteCocoaCharcoal.copy(alpha = 0.15f)))
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // --- NEW GAME NAME ---
        Text(
            text = "NEW GAME ROOM NAME",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = CuteCocoaCharcoal,
            modifier = Modifier.align(Alignment.Start).padding(start = 12.dp, bottom = 4.dp)
        )
        TextField(
            value = sessionName,
            onValueChange = { sessionName = it },
            placeholder = { Text("e.g. Trivia Madness Round 1", color = CuteCocoaCharcoal.copy(alpha = 0.4f), fontWeight = FontWeight.Bold) },
            modifier = Modifier
                .fillMaxWidth()
                .border(3.dp, CuteCocoaCharcoal, CartoonInputShape),
            singleLine = true,
            enabled = resumeSessionId.isBlank(),
            textStyle = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = CuteCocoaCharcoal),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = if (resumeSessionId.isBlank()) CuteCloudWhite else CuteCocoaCharcoal.copy(alpha = 0.05f),
                unfocusedContainerColor = if (resumeSessionId.isBlank()) CuteCloudWhite else CuteCocoaCharcoal.copy(alpha = 0.05f),
                disabledContainerColor = CuteCocoaCharcoal.copy(alpha = 0.05f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = CuteCocoaCharcoal
            ),
            shape = CartoonInputShape
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // --- GAME DESCRIPTION ---
        Text(
            text = "ROOM DESCRIPTION",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = CuteCocoaCharcoal,
            modifier = Modifier.align(Alignment.Start).padding(start = 12.dp, bottom = 4.dp)
        )
        TextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("e.g. Fun trivia questions for kids", color = CuteCocoaCharcoal.copy(alpha = 0.4f), fontWeight = FontWeight.Bold) },
            modifier = Modifier
                .fillMaxWidth()
                .border(3.dp, CuteCocoaCharcoal, CartoonInputShape),
            maxLines = 2,
            enabled = resumeSessionId.isBlank(),
            textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CuteCocoaCharcoal),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = if (resumeSessionId.isBlank()) CuteCloudWhite else CuteCocoaCharcoal.copy(alpha = 0.05f),
                unfocusedContainerColor = if (resumeSessionId.isBlank()) CuteCloudWhite else CuteCocoaCharcoal.copy(alpha = 0.05f),
                disabledContainerColor = CuteCocoaCharcoal.copy(alpha = 0.05f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = CuteCocoaCharcoal
            ),
            shape = CartoonInputShape
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        // --- MAX PLAYERS ---
        Text(
            text = "MAX NICKNAME SLOTS",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = CuteCocoaCharcoal,
            modifier = Modifier.align(Alignment.Start).padding(start = 12.dp, bottom = 4.dp)
        )
        TextField(
            value = maxParticipants,
            onValueChange = { maxParticipants = it.filter { char -> char.isDigit() } },
            placeholder = { Text("10", color = CuteCocoaCharcoal.copy(alpha = 0.4f), fontWeight = FontWeight.Bold) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .border(3.dp, CuteCocoaCharcoal, CartoonInputShape),
            singleLine = true,
            enabled = resumeSessionId.isBlank(),
            textStyle = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = CuteCocoaCharcoal),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = if (resumeSessionId.isBlank()) CuteCloudWhite else CuteCocoaCharcoal.copy(alpha = 0.05f),
                unfocusedContainerColor = if (resumeSessionId.isBlank()) CuteCloudWhite else CuteCocoaCharcoal.copy(alpha = 0.05f),
                disabledContainerColor = CuteCocoaCharcoal.copy(alpha = 0.05f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = CuteCocoaCharcoal
            ),
            shape = CartoonInputShape
        )

        Spacer(modifier = Modifier.height(36.dp))
        
        // --- BUTTON TRIGGER LOGIC ---
        val isResuming = resumeSessionId.isNotBlank()
        val triggerEnabled = isResuming || sessionName.isNotBlank()
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .cartoonShadow(
                    shadowColor = if (triggerEnabled) CuteCocoaCharcoal else Color.Transparent, 
                    offsetX = if (triggerEnabled) 5.dp else 0.dp, 
                    offsetY = if (triggerEnabled) 5.dp else 0.dp, 
                    shapeRadius = 20.dp
                )
                .background(
                    if (triggerEnabled) CuteSunnyYellow else CuteCocoaCharcoal.copy(alpha = 0.15f), 
                    CartoonButtonShape
                )
                .border(
                    width = if (triggerEnabled) 3.dp else 2.dp, 
                    color = if (triggerEnabled) CuteCocoaCharcoal else CuteCocoaCharcoal.copy(alpha = 0.2f), 
                    shape = CartoonButtonShape
                )
                .bouncyClickable(enabled = triggerEnabled) {
                    if (triggerEnabled) {
                        if (isResuming) {
                            onResumeSubmit(resumeSessionId)
                        } else {
                            onCreateSubmit(sessionName, description, maxParticipants.toIntOrNull() ?: 10) 
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isResuming) "⭐ RESUME MASTER GAME" else "🚀 LAUNCH NEW GAME!", 
                fontSize = 16.sp, 
                fontWeight = FontWeight.ExtraBold, 
                color = if (triggerEnabled) CuteCocoaCharcoal else CuteCocoaCharcoal.copy(alpha = 0.4f)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(
            onClick = onBack,
            modifier = Modifier.height(48.dp)
        ) {
            Text(
                "🎈 Back to Home", 
                fontWeight = FontWeight.ExtraBold, 
                color = CuteCocoaCharcoal.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
