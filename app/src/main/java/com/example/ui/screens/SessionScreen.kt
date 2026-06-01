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
import com.example.network.ParticipantDto
import com.example.network.RoundHistoryDto
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import kotlinx.coroutines.delay

import androidx.activity.compose.BackHandler

@Composable
fun SessionScreen(
    uiState: BuzzerUiState,
    onStartQuiz: () -> Unit,
    onStopQuiz: () -> Unit,
    onResetQuiz: () -> Unit,
    onBuzz: () -> Unit,
    onNextQuestion: () -> Unit,
    onLeave: () -> Unit
) {
    BackHandler {
        onLeave()
    }
    
    val session = uiState.session

    if (session == null) {
        if (uiState.errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0F172A))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "⚠️ SYSTEM DIAGNOSTIC REPORT",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEF4444),
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "SIGNAL STREAM STATUS \\\\ TERMINATED",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8),
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            val isMasterRole = if (uiState.isMaster) "HOST / MASTER" else "PARTICIPANT / CLIENT"
                            val currentUserId = uiState.userName ?: "NULL_USER"
                            
                            Text(
                                "• ACTIVE NODE: $currentUserId ($isMasterRole)",
                                fontSize = 13.sp,
                                color = Color(0xFF38BDF8),
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "• INTERRUPT REPORT:",
                                fontSize = 13.sp,
                                color = Color(0xFFF1F5F9),
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF0F172A), RoundedCornerShape(6.dp))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = uiState.errorMessage ?: "CRITICAL DISCORDANCE IN STATE SYNC",
                                    fontSize = 12.sp,
                                    color = Color(0xFFF87171),
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                    lineHeight = 16.sp
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                "RESOLUTION ALGORITHMS:",
                                fontSize = 12.sp,
                                color = Color(0xFFF59E0B),
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "1. RECYCLE SESSION: Backend servers destroy idle sockets to conserve heap memory. Initiate a NEW quiz block as host.\n" +
                                "2. CONNECTION OUTAGE: Verified routes failed or the database purged session context. Validate network interface.",
                                fontSize = 11.sp,
                                color = Color(0xFFCBD5E1),
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = onLeave,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "RECONNECT / LEAVE", 
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
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
            questionCounter = session.questionCounter,
            startTime = session.startTime,
            participants = session.participants ?: emptyList(),
            roundHistory = session.roundHistory ?: emptyList(),
            onStartQuiz = onStartQuiz,
            onStopQuiz = onStopQuiz,
            onNextQuestion = onNextQuestion,
            onResetQuiz = onResetQuiz,
            onLeave = onLeave
        )
    } else {
        ParticipantBuzzer(
            sessionName = session.sessionName ?: "PaddyBuzz",
            status = session.status,
            currentRound = session.questionCounter,
            myParticipant = (session.participants ?: emptyList()).find { it.userName == uiState.userName },
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
    questionCounter: Int,
    startTime: Long?,
    participants: List<ParticipantDto>,
    roundHistory: List<RoundHistoryDto>,
    onStartQuiz: () -> Unit,
    onStopQuiz: () -> Unit,
    onNextQuestion: () -> Unit,
    onResetQuiz: () -> Unit,
    onLeave: () -> Unit
) {
    var participantsExpanded by remember { mutableStateOf(true) }
    var historyExpanded by remember { mutableStateOf(false) }

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("ROUND $questionCounter", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onNextQuestion, modifier = Modifier.size(24.dp).background(MaterialTheme.colorScheme.primary.copy(alpha=0.1f), CircleShape)) {
                        Icon(Icons.Default.Add, contentDescription = "Next Round", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text("QUIZ STATUS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8), letterSpacing = 1.sp)
                val statusColor = when (status) {
                    "active" -> Color(0xFF4CAF50)
                    "stopped" -> Color(0xFFEF4444)
                    else -> MaterialTheme.colorScheme.primary
                }
                Text(
                    text = status.uppercase(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = statusColor
                )

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
                        onClick = onStopQuiz, 
                        enabled = status == "active",
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                    ) {
                        Text("STOP", fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = onResetQuiz, 
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155))
                    ) {
                        Text("RESET", fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                ActiveTimerText(status, startTime)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Participants List
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { participantsExpanded = !participantsExpanded }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("PARTICIPANTS (${participants.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B), letterSpacing = 1.sp, modifier = Modifier.weight(1f))
                Icon(
                    if (participantsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (participantsExpanded) "Collapse" else "Expand",
                    tint = Color(0xFF64748B)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            if (participantsExpanded) {
                participants.sortedBy { it.userName }.forEach { p ->
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
                            Text(p.userName.take(1).uppercase(), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(p.userName, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF334155), modifier = Modifier.weight(1f))
                        
                        if (p.buzzTime != null && startTime != null) {
                            val buzzElapsed = p.buzzTime - startTime
                            val absoluteElapsed = kotlin.math.abs(buzzElapsed)
                            val bSec = (absoluteElapsed / 1000).toInt()
                            val bMillis = ((absoluteElapsed % 1000) / 10).toInt()
                            val sign = if (buzzElapsed < 0) "-" else "+"
                            Text(
                                String.format(java.util.Locale.US, "%s%02d:%02d", sign, bSec, bMillis),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4CAF50)
                            )
                        } else if (p.buzzTime != null) {
                            Icon(Icons.Default.Add, contentDescription = "Buzzed", tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { historyExpanded = !historyExpanded }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("ROUND HISTORY (${roundHistory.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B), letterSpacing = 1.sp, modifier = Modifier.weight(1f))
                Icon(
                    if (historyExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (historyExpanded) "Collapse" else "Expand",
                    tint = Color(0xFF64748B)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            if (historyExpanded) {
                roundHistory.sortedBy { it.roundNumber }.forEach { h ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Round ${h.roundNumber}", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF334155), modifier = Modifier.weight(1f))
                        Text(h.winnerName ?: "No Winner", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
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
    currentRound: Int,
    myParticipant: ParticipantDto?,
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
        
        if (status == "stopped") {
            val hasBuzzed = myParticipant?.buzzTime != null
            Text(
                text = if (hasBuzzed) "🎉 YOU BUZZED! 🎉" else "ROUND OVER",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (hasBuzzed) Color(0xFF4CAF50) else Color(0xFF64748B)
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
                        if (status == "active") "ROUND $currentRound ACTIVE" else "WAITING FOR HOST",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF312E81),
                        letterSpacing = 1.sp
                    )
                    Text(
                        if (status == "active") "Tap the buzzer now!" else "Wait for Round $currentRound to start...",
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
            val isBuzzed = myParticipant?.buzzTime != null
            val isActive = status == "active" && !isBuzzed
            
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
                            colors = if (isBuzzed) listOf(Color(0xFF4CAF50), Color(0xFF388E3C)) 
                                     else if (isActive) listOf(Color(0xFF6366F1), Color(0xFF4338CA)) 
                                     else listOf(Color.Gray, Color.DarkGray)
                        )
                    )
                    .clickable(enabled = isActive) { onBuzz() }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        if (isBuzzed) "BUZZ RECORDED" else "READY TO TAP",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        if (isBuzzed) "WAIT!" else "BUZZ!",
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

@Composable
fun ActiveTimerText(status: String, startTime: Long?) {
    var elapsedMs by remember { mutableStateOf(0L) }
    LaunchedEffect(status, startTime) {
        if (status == "active" && startTime != null) {
            while (true) {
                elapsedMs = System.currentTimeMillis() - startTime
                delay(100)
            }
        } else if (status == "waiting") {
            elapsedMs = 0L
        }
    }
    
    if (status == "active" || (status == "stopped" && elapsedMs > 0)) {
        val seconds = (elapsedMs / 1000).toInt()
        val millis = ((elapsedMs % 1000) / 10).toInt()
        Text(
            String.format(java.util.Locale.US, "%02d:%02d", seconds, millis),
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF334155)
        )
    }
}
