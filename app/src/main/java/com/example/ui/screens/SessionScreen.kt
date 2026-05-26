package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.BuzzerUiState

@Composable
fun SessionScreen(
    uiState: BuzzerUiState,
    onStartQuiz: () -> Unit,
    onResetQuiz: () -> Unit,
    onBuzz: () -> Unit,
    onLeave: () -> Unit
) {
    val session = uiState.session

    if (session == null) {
        if (uiState.errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "🤔",
                        fontSize = 80.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        "You got Lost!",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        uiState.errorMessage ?: "Something went wrong.",
                        fontSize = 16.sp,
                        color = Color(0xFF64748B),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                    Button(
                        onClick = onLeave,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                    ) {
                        Text("GO BACK", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        return
    }

    if (uiState.isMaster) {
        MasterDashboard(
            sessionId = session.sessionId,
            sessionName = session.sessionName ?: "PaddyBuzz",
            status = session.status,
            participants = session.participants,
            winner = session.winner,
            onStartQuiz = onStartQuiz,
            onResetQuiz = onResetQuiz,
            onLeave = onLeave
        )
    } else {
        ParticipantBuzzer(
            sessionName = session.sessionName ?: "PaddyBuzz",
            status = session.status,
            winner = session.winner,
            userName = uiState.userName ?: "",
            onBuzz = onBuzz,
            onLeave = onLeave
        )
    }
}

@Composable
fun MasterDashboard(
    sessionId: String,
    sessionName: String,
    status: String,
    participants: List<String>,
    winner: String?,
    onStartQuiz: () -> Unit,
    onResetQuiz: () -> Unit,
    onLeave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .shadow(1.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("IQ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(sessionName, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                    Text(
                        "HOST PANEL",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("SESSION ID", fontSize = 10.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Text(sessionId, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155), letterSpacing = 1.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Actions and Status
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("QUIZ STATUS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8), letterSpacing = 1.sp)
                val statusColor = when (status) {
                    "active" -> Color(0xFF4CAF50)
                    "finished" -> Color(0xFFEF4444)
                    else -> MaterialTheme.colorScheme.primary
                }
                Text(
                    text = status.uppercase(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = statusColor
                )

                if (status == "finished" && winner != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("🎉 WINNER: $winner 🎉", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = onStartQuiz, 
                        enabled = status != "active",
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("START", fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = onResetQuiz, 
                        enabled = status == "finished",
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155))
                    ) {
                        Text("RESET", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Participants List
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text("PARTICIPANTS (${participants.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B), letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(12.dp))
            participants.forEach { p ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEEF2FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(p.take(1).uppercase(), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(p, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF334155))
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        
        TextButton(onClick = onLeave, modifier = Modifier.padding(24.dp)) {
            Text("END SESSION", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ParticipantBuzzer(
    sessionName: String,
    status: String,
    winner: String?,
    userName: String,
    onBuzz: () -> Unit,
    onLeave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .shadow(1.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("IQ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(sessionName, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                    Text(
                        status.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp
                    )
                }
            }
            TextButton(onClick = onLeave) {
                Text("LEAVE", color = MaterialTheme.colorScheme.error, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        if (status == "finished" && winner != null) {
            val isMe = winner == userName
            Text(
                text = if (isMe) "🎉 YOU WON! 🎉" else "$winner buzzed first!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (isMe) Color(0xFF4CAF50) else Color(0xFFEF4444)
            )
            Spacer(modifier = Modifier.height(24.dp))
        } else {
            // Status Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEEF2FF))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val indicatorColor = if(status == "active") Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(indicatorColor)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        if (status == "active") "SESSION ACTIVE" else "WAITING FOR HOST",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF312E81),
                        letterSpacing = 1.sp
                    )
                    Text(
                        if (status == "active") "Tap the buzzer now!" else "Wait for the host to signal...",
                        fontSize = 13.sp,
                        color = Color(0xFF4338CA)
                    )
                }
            }
            Spacer(modifier = Modifier.height(48.dp))
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            val isActive = status == "active"
            
            // Outer rings
            Box(modifier = Modifier
                .size(320.dp)
                .clip(CircleShape)
                .background(Color(0xFFC7D2FE).copy(alpha = 0.3f)))
            Box(modifier = Modifier
                .size(360.dp)
                .clip(CircleShape)
                .background(Color(0xFF818CF8).copy(alpha = 0.1f)))

            // Inner Buzzer
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(256.dp)
                    .clip(CircleShape)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = if (isActive) listOf(Color(0xFF6366F1), Color(0xFF4338CA)) else listOf(Color.Gray, Color.DarkGray)
                        )
                    )
                    .clickable(enabled = isActive) { onBuzz() }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "READY TO TAP",
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 3.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "BUZZ!",
                        color = Color.White,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-1).sp
                    )
                }
            }
        }
        
        // Connected User Info
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                Text("👤", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("CONNECTED AS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8), letterSpacing = 1.sp)
                Text(userName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
            }
        }
    }
}
