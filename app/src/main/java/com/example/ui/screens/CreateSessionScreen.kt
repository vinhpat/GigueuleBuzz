package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSessionScreen(
    onCreateSubmit: (String, String, Int) -> Unit,
    onBack: () -> Unit
) {
    var sessionName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var maxParticipants by remember { mutableStateOf("10") }

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
        Text("Create Session", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
        Text("Setup your PaddyBuzz session", fontSize = 14.sp, color = Color(0xFF64748B))

        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = sessionName,
            onValueChange = { sessionName = it },
            label = { Text("Session Name") },
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
            value = description,
            onValueChange = { description = it },
            label = { Text("Quick Description") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
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
            value = maxParticipants,
            onValueChange = { maxParticipants = it.filter { char -> char.isDigit() } },
            label = { Text("Max Participants") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
            onClick = { 
                onCreateSubmit(sessionName, description, maxParticipants.toIntOrNull() ?: 10) 
            },
            modifier = Modifier.fillMaxWidth().height(64.dp),
            enabled = sessionName.isNotBlank(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("START SESSION", fontSize = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onBack) {
            Text("CANCEL", fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
        }
    }
}
