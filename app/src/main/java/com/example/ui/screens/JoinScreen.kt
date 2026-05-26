package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text("IQ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Join PaddyBuzz", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
        Text("Enter the session details below", fontSize = 14.sp, color = Color(0xFF64748B))

        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = sessionId,
            onValueChange = { sessionId = it.uppercase() },
            label = { Text("Session ID (e.g. XJ99)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color(0xFFCBD5E1),
                focusedLabelColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Your Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color(0xFFCBD5E1),
                focusedLabelColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = { onJoinSubmit(sessionId, userName) },
            modifier = Modifier.fillMaxWidth().height(64.dp),
            enabled = sessionId.isNotBlank() && userName.isNotBlank(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("JOIN SESSION", fontSize = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onBack) {
            Text("CANCEL", fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
        }
    }
}
