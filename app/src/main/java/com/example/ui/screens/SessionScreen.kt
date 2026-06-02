package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.BuzzerUiState
import com.example.network.ParticipantDto
import com.example.network.RoundHistoryDto
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.activity.compose.BackHandler
import com.example.ui.theme.*
import kotlinx.coroutines.delay

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
                    .background(CuteVanillaCream)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "⚠️ CONNECTION RE-ROUTE",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CuteCherryRed,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .cartoonShadow(shadowColor = CuteCocoaCharcoal, offsetX = 5.dp, offsetY = 5.dp, shapeRadius = 24.dp)
                            .border(3.dp, CuteCocoaCharcoal, RoundedCornerShape(24.dp)),
                        colors = CardDefaults.cardColors(containerColor = CuteCloudWhite),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                "SIGNAL LOST // ROOM ARCHIVED",
                                fontSize = 11.sp,
                                color = CuteBubblegumPink,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            val isMasterRole = if (uiState.isMaster) "👑 MASTER ADMIN" else "🎮 PLAYER CLIENT"
                            val currentUserId = uiState.userName ?: "SWEET_NICKNAME"
                            
                            Text(
                                "• ACTIVE NODE: $currentUserId ($isMasterRole)",
                                fontSize = 13.sp,
                                color = CuteCocoaCharcoal,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "• PADDY ERROR REPORT:",
                                fontSize = 13.sp,
                                color = CuteCocoaCharcoal,
                                fontWeight = FontWeight.ExtraBold
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(CuteVanillaCream, RoundedCornerShape(12.dp))
                                    .border(2.dp, CuteCocoaCharcoal.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = uiState.errorMessage ?: "CRITICAL DISCORDANCE IN STATE SYNC",
                                    fontSize = 12.sp,
                                    color = CuteCocoaCharcoal,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 16.sp
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                "COZY TROUBLESHOOTING TIPS:",
                                fontSize = 12.sp,
                                color = CuteSkyBlue,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "1. RESTART SESSION: Idle rooms clean up automatically. Ask your host to open a fresh room!\n" +
                                "2. INTERNET FLUTTER: Keep high Wi-Fi bars for lightning-fast buzzing responses.",
                                fontSize = 11.sp,
                                color = CuteCocoaCharcoal.copy(alpha = 0.7f),
                                lineHeight = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .cartoonShadow(shadowColor = CuteCocoaCharcoal, offsetX = 4.dp, offsetY = 4.dp, shapeRadius = 20.dp)
                            .background(CuteSunnyYellow, CartoonButtonShape)
                            .border(3.dp, CuteCocoaCharcoal, CartoonButtonShape)
                            .bouncyClickable(onClick = onLeave),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🎈 RETURN TO DASHBOARD", 
                            fontWeight = FontWeight.ExtraBold,
                            color = CuteCocoaCharcoal
                        )
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize().background(CuteVanillaCream), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = CuteSunnyYellow, strokeWidth = 4.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("CONNECTING COZILY...", color = CuteCocoaCharcoal, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                }
            }
        }
        return
    }

    if (uiState.isMaster) {
        MasterDashboard(
            sessionId = session.sessionId,
            sessionName = session.sessionName ?: "PaddyBuzz",
            status = session.status ?: "waiting",
            questionCounter = session.questionCounter ?: 1,
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
            status = session.status ?: "waiting",
            currentRound = session.questionCounter ?: 1,
            myParticipant = (session.participants ?: emptyList()).filterNotNull().find { (it.userName as? String) == uiState.userName },
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
    
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CuteVanillaCream)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- COZY APP BAR ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .cartoonShadow(shadowColor = CuteCocoaCharcoal, offsetX = 4.dp, offsetY = 4.dp, shapeRadius = 24.dp)
                .border(3.dp, CuteCocoaCharcoal, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = CuteCloudWhite),
            shape = RoundedCornerShape(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .border(2.dp, CuteCocoaCharcoal, CircleShape)
                            .background(CuteSunnyYellow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👑", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = sessionName, 
                            fontSize = 16.sp, 
                            fontWeight = FontWeight.ExtraBold, 
                            color = CuteCocoaCharcoal
                        )
                        Text(
                            text = "MASTER LOBBY",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CuteBubblegumPink
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "ROOM CODE", 
                        fontSize = 9.sp, 
                        color = CuteCocoaCharcoal.copy(alpha = 0.5f), 
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = sessionId, 
                        fontSize = 20.sp, 
                        fontWeight = FontWeight.ExtraBold, 
                        color = CuteCocoaCharcoal
                    )
                }
            }
        }

        // --- GENERAL STATUS MODULE ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .cartoonShadow(shadowColor = CuteCocoaCharcoal, offsetX = 5.dp, offsetY = 5.dp, shapeRadius = 24.dp)
                .border(3.dp, CuteCocoaCharcoal, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = CuteCloudWhite),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .background(CuteVanillaCream, RoundedCornerShape(12.dp))
                        .border(2.dp, CuteCocoaCharcoal, RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "⭐ ROUND $questionCounter ⭐", 
                        fontSize = 13.sp, 
                        fontWeight = FontWeight.ExtraBold, 
                        color = CuteCocoaCharcoal
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(CuteSunnyYellow, CircleShape)
                            .border(1.5.dp, CuteCocoaCharcoal, CircleShape)
                            .clip(CircleShape)
                            .bouncyClickable(onClick = onNextQuestion),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Next Round", tint = CuteCocoaCharcoal, modifier = Modifier.size(14.dp))
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "BUZZER SYSTEM STATUS", 
                    fontSize = 10.sp, 
                    fontWeight = FontWeight.ExtraBold, 
                    color = CuteCocoaCharcoal.copy(alpha = 0.5f)
                )
                
                val statusText: String
                val statusBg: Color
                val statusTextCol: Color
                
                when (status) {
                    "active" -> {
                        statusText = "📢 BUZZERS OPEN!"
                        statusBg = CuteLimeSoda
                        statusTextCol = CuteCocoaCharcoal
                    }
                    "stopped" -> {
                        statusText = "🔒 ROUND LOCKED/STOPPED"
                        statusBg = CuteCherryRed
                        statusTextCol = CuteCloudWhite
                    }
                    else -> {
                        statusText = "💤 WAITING FOR HOST"
                        statusBg = CuteSkyBlue
                        statusTextCol = CuteCocoaCharcoal
                    }
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .background(statusBg, RoundedCornerShape(12.dp))
                        .border(2.dp, CuteCocoaCharcoal, RoundedCornerShape(12.dp))
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = statusText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = statusTextCol
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- MASTER ACTIONS GRID ---
                Row(
                    modifier = Modifier.fillMaxWidth(), 
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // START
                    Box(
                        modifier = Modifier
                            .weight(1.0f)
                            .height(52.dp)
                            .cartoonShadow(
                                shadowColor = if (status != "active") CuteCocoaCharcoal else Color.Transparent, 
                                offsetX = if (status != "active") 4.dp else 0.dp, 
                                offsetY = if (status != "active") 4.dp else 0.dp, 
                                shapeRadius = 16.dp
                            )
                            .background(
                                if (status != "active") CuteLimeSoda else CuteCocoaCharcoal.copy(alpha = 0.05f), 
                                RoundedCornerShape(16.dp)
                            )
                            .border(
                                width = if (status != "active") 2.5.dp else 1.5.dp, 
                                color = if (status != "active") CuteCocoaCharcoal else CuteCocoaCharcoal.copy(alpha = 0.15f), 
                                shape = RoundedCornerShape(16.dp)
                            )
                            .bouncyClickable(enabled = status != "active", onClick = onStartQuiz),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("▶️ START", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = CuteCocoaCharcoal)
                    }

                    // STOP
                    Box(
                        modifier = Modifier
                            .weight(1.0f)
                            .height(52.dp)
                            .cartoonShadow(
                                shadowColor = if (status == "active") CuteCocoaCharcoal else Color.Transparent, 
                                offsetX = if (status == "active") 4.dp else 0.dp, 
                                offsetY = if (status == "active") 4.dp else 0.dp, 
                                shapeRadius = 16.dp
                            )
                            .background(
                                if (status == "active") CuteCherryRed else CuteCocoaCharcoal.copy(alpha = 0.05f), 
                                RoundedCornerShape(16.dp)
                            )
                            .border(
                                width = if (status == "active") 2.5.dp else 1.5.dp, 
                                color = if (status == "active") CuteCocoaCharcoal else CuteCocoaCharcoal.copy(alpha = 0.15f), 
                                shape = RoundedCornerShape(16.dp)
                            )
                            .bouncyClickable(enabled = status == "active", onClick = onStopQuiz),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🛑 STOP", 
                            fontWeight = FontWeight.ExtraBold, 
                            fontSize = 12.sp, 
                            color = if (status == "active") CuteCloudWhite else CuteCocoaCharcoal.copy(alpha = 0.3f)
                        )
                    }

                    // RESET
                    Box(
                        modifier = Modifier
                            .weight(1.0f)
                            .height(52.dp)
                            .cartoonShadow(shadowColor = CuteCocoaCharcoal, offsetX = 4.dp, offsetY = 4.dp, shapeRadius = 16.dp)
                            .background(CuteSunnyYellow, RoundedCornerShape(16.dp))
                            .border(2.5.dp, CuteCocoaCharcoal, RoundedCornerShape(16.dp))
                            .bouncyClickable(onClick = onResetQuiz),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🔄 RESET", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = CuteCocoaCharcoal)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                ActiveTimerText(status, startTime)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- MODULES ---
        val sanitizedParticipants = remember(participants) {
            participants.filterNotNull().filter { (it.userName as? Any) != null }
        }
        val sanitizedHistory = remember(roundHistory) {
            roundHistory.filterNotNull()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            // --- COLLAPSIBLE CLIENT DIRECTORY ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .cartoonShadow(shadowColor = CuteCocoaCharcoal, offsetX = 4.dp, offsetY = 4.dp, shapeRadius = 16.dp)
                    .background(CuteCloudWhite, RoundedCornerShape(16.dp))
                    .border(3.dp, CuteCocoaCharcoal, RoundedCornerShape(16.dp))
                    .clickable { participantsExpanded = !participantsExpanded }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "👥 ACTIVE PLAYERS (${sanitizedParticipants.size})", 
                    fontSize = 13.sp, 
                    fontWeight = FontWeight.ExtraBold, 
                    color = CuteCocoaCharcoal, 
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (participantsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (participantsExpanded) "Collapse" else "Expand",
                    tint = CuteCocoaCharcoal
                )
            }
            
            if (participantsExpanded) {
                Spacer(modifier = Modifier.height(10.dp))
                if (sanitizedParticipants.isEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, CuteCocoaCharcoal.copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = CuteCloudWhite.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "No players joined yet. Show them Room Code \"$sessionId\"!", 
                            fontSize = 12.sp, 
                            fontWeight = FontWeight.Bold, 
                            color = CuteCocoaCharcoal.copy(alpha = 0.6f),
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    val colors = listOf(CuteSunnyYellow, CuteBubblegumPink, CuteSkyBlue, CuteLimeSoda, CuteMangoOrange)
                    sanitizedParticipants.sortedBy { (it.userName as? String).orEmpty() }.forEachIndexed { idx, p ->
                        val pName = (p.userName as? String).orEmpty()
                        val avatarColor = colors[idx % colors.size]
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .border(2.dp, CuteCocoaCharcoal, RoundedCornerShape(16.dp))
                                .background(CuteCloudWhite, RoundedCornerShape(16.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .border(1.5.dp, CuteCocoaCharcoal, CircleShape)
                                    .background(avatarColor, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                val initial = pName.take(1).uppercase().ifEmpty { "?" }
                                Text(
                                    text = initial, 
                                    color = CuteCocoaCharcoal, 
                                    fontWeight = FontWeight.ExtraBold, 
                                    fontSize = 13.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                pName, 
                                fontSize = 14.sp, 
                                fontWeight = FontWeight.ExtraBold, 
                                color = CuteCocoaCharcoal, 
                                modifier = Modifier.weight(1f)
                            )
                            
                            if (p.buzzTime != null && startTime != null) {
                                val buzzElapsed = p.buzzTime - startTime
                                val absoluteElapsed = kotlin.math.abs(buzzElapsed)
                                val bSec = (absoluteElapsed / 1000).toInt()
                                val bMillis = ((absoluteElapsed % 1000) / 10).toInt()
                                val sign = if (buzzElapsed < 0) "-" else "+"
                                Text(
                                    text = String.format(java.util.Locale.US, "⚡ %s%02d.%02ds", sign, bSec, bMillis),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = CuteCocoaCharcoal,
                                    modifier = Modifier
                                        .background(CuteLimeSoda, RoundedCornerShape(8.dp))
                                        .border(1.5.dp, CuteCocoaCharcoal, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            } else if (p.buzzTime != null) {
                                Text(
                                    text = "⚡ BUZZED!",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = CuteCloudWhite,
                                    modifier = Modifier
                                        .background(CuteBubblegumPink, RoundedCornerShape(8.dp))
                                        .border(1.5.dp, CuteCocoaCharcoal, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // --- COLLAPSIBLE SCOREBOARD / HISTORY ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .cartoonShadow(shadowColor = CuteCocoaCharcoal, offsetX = 4.dp, offsetY = 4.dp, shapeRadius = 16.dp)
                    .background(CuteCloudWhite, RoundedCornerShape(16.dp))
                    .border(3.dp, CuteCocoaCharcoal, RoundedCornerShape(16.dp))
                    .clickable { historyExpanded = !historyExpanded }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🏆 ROUND WINNERS INDEX (${sanitizedHistory.size})", 
                    fontSize = 13.sp, 
                    fontWeight = FontWeight.ExtraBold, 
                    color = CuteCocoaCharcoal, 
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (historyExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (historyExpanded) "Collapse" else "Expand",
                    tint = CuteCocoaCharcoal
                )
            }
            
            if (historyExpanded) {
                Spacer(modifier = Modifier.height(10.dp))
                if (sanitizedHistory.isEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, CuteCocoaCharcoal.copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = CuteCloudWhite.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "No rounds complete yet. Crack open those buzzers!", 
                            fontSize = 12.sp, 
                            fontWeight = FontWeight.Bold, 
                            color = CuteCocoaCharcoal.copy(alpha = 0.6f),
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    sanitizedHistory.sortedBy { (it.roundNumber as? Int) ?: 0 }.forEach { h ->
                        val roundNum = (h.roundNumber as? Int) ?: 0
                        val winnerName = (h.winnerName as? String) ?: "EMPTY_WINNER"
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .border(2.dp, CuteCocoaCharcoal, RoundedCornerShape(16.dp))
                                .background(CuteCloudWhite, RoundedCornerShape(16.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "👑 ROUND $roundNum:", 
                                fontSize = 13.sp, 
                                fontWeight = FontWeight.ExtraBold, 
                                color = CuteCocoaCharcoal, 
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                winnerName, 
                                fontSize = 14.sp, 
                                fontWeight = FontWeight.ExtraBold, 
                                color = CuteBubblegumPink
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
        
        TextButton(onClick = onLeave, modifier = Modifier.padding(bottom = 24.dp)) {
            Text("🛑 LEAVE & CLOSE GAME ROOM", color = CuteCherryRed, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
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
    var tickColor by remember { mutableStateOf(false) }
    LaunchedEffect(status) {
        while (true) {
            delay(500L)
            tickColor = !tickColor
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CuteVanillaCream),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- COZY PLAYER HEADER ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .cartoonShadow(shadowColor = CuteCocoaCharcoal, offsetX = 4.dp, offsetY = 4.dp, shapeRadius = 24.dp)
                .border(3.dp, CuteCocoaCharcoal, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = CuteCloudWhite),
            shape = RoundedCornerShape(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .border(1.5.dp, CuteCocoaCharcoal, CircleShape)
                            .background(CuteBubblegumPink, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🎮", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = sessionName, 
                            fontSize = 14.sp, 
                            fontWeight = FontWeight.ExtraBold, 
                            color = CuteCocoaCharcoal
                        )
                        Text(
                            text = "STATUS: ${status.uppercase()}",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (status == "active") CuteLimeSoda else CuteCocoaCharcoal.copy(alpha = 0.5f)
                        )
                    }
                }
                TextButton(onClick = onLeave) {
                    Text("🎈 DISCONNECT", color = CuteCherryRed, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        // --- STATUS TELEMETRY ---
        if (status == "stopped") {
            val hasBuzzed = myParticipant?.buzzTime != null
            Box(
                modifier = Modifier
                    .background(if (hasBuzzed) CuteLimeSoda else CuteCherryRed, RoundedCornerShape(16.dp))
                    .border(2.5.dp, CuteCocoaCharcoal, RoundedCornerShape(16.dp))
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (hasBuzzed) "🎉 YOUR BUZZ RECORDED! ✅" else "ROUND IS COMPLETED!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (hasBuzzed) CuteCocoaCharcoal else CuteCloudWhite
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .border(2.5.dp, CuteCocoaCharcoal, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = CuteCloudWhite)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val indicatorBg = if (status == "active") CuteLimeSoda else CuteSunnyYellow
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .border(1.5.dp, CuteCocoaCharcoal, CircleShape)
                            .background(if (tickColor && status == "active") indicatorBg else Color.Transparent, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "⭐ ROUND $currentRound ⭐",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CuteCocoaCharcoal
                        )
                        Text(
                            text = if (status == "active") "TAP THE GIANT NOSE NOW! GO!" else "Wait cozy... host will open the buzzers!",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CuteCocoaCharcoal.copy(alpha = 0.6f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // --- ENORMOUS GOOFY 3D CARTOON BUZZER BUTTON ---
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            val isBuzzed = myParticipant?.buzzTime != null
            val isActive = status == "active" && !isBuzzed
            
            // Giant decorative visual orbits
            Box(modifier = Modifier
                .size(290.dp)
                .border(2.dp, CuteCocoaCharcoal.copy(alpha = 0.08f), CircleShape))
            Box(modifier = Modifier
                .size(260.dp)
                .border(3.dp, CuteCocoaCharcoal.copy(alpha = 0.12f), CircleShape))

            // Massive Bouncy Buzzer Dome Button
            val domeColor = if (isBuzzed) CuteLimeSoda else if (isActive) CuteBubblegumPink else CuteCocoaCharcoal.copy(alpha = 0.1f)
            val shadowColor = if (isActive || isBuzzed) CuteCocoaCharcoal else Color.Transparent
            
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .cartoonShadow(shadowColor = shadowColor, offsetX = 10.dp, offsetY = 10.dp, shapeRadius = 110.dp)
                    .background(domeColor, CircleShape)
                    .border(5.dp, CuteCocoaCharcoal, CircleShape)
                    .clip(CircleShape)
                    .bouncyClickable(enabled = isActive) { onBuzz() },
                contentAlignment = Alignment.Center
            ) {
                // Interactive inner gradient layer
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp)
                        .background(domeColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isBuzzed) "SMASHED!" else "READY",
                            color = CuteCocoaCharcoal.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isBuzzed) "😊 BUZZ!" else "🚨 TAP!",
                            color = CuteCocoaCharcoal,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
        
        // --- NICKNAME BADGE ID ---
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .cartoonShadow(shadowColor = CuteCocoaCharcoal, offsetX = 4.dp, offsetY = 4.dp, shapeRadius = 20.dp)
                .border(3.dp, CuteCocoaCharcoal, RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = CuteCloudWhite)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .border(1.5.dp, CuteCocoaCharcoal, CircleShape)
                        .background(CuteSunnyYellow, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👋", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "PLAYING IN GAME AS", 
                        fontSize = 9.sp, 
                        fontWeight = FontWeight.ExtraBold, 
                        color = CuteCocoaCharcoal.copy(alpha = 0.5f)
                    )
                    Text(
                        text = userName, 
                        fontSize = 16.sp, 
                        fontWeight = FontWeight.ExtraBold, 
                        color = CuteCocoaCharcoal
                    )
                }
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
                delay(80)
            }
        } else if (status == "waiting") {
            elapsedMs = 0L
        }
    }
    
    if (status == "active" || (status == "stopped" && elapsedMs > 0)) {
        val seconds = (elapsedMs / 1000).toInt()
        val millis = ((elapsedMs % 1000) / 10).toInt()
        
        Box(
            modifier = Modifier
                .padding(top = 12.dp)
                .background(CuteVanillaCream, RoundedCornerShape(12.dp))
                .border(2.dp, CuteCocoaCharcoal, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = String.format(java.util.Locale.US, "%02d:%02d SEC", seconds, millis),
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CuteCherryRed,
                fontFamily = FontFamily.Monospace // Keep monospace for clean ticking clock alignment, but styled with rich goofy sizes
            )
        }
    }
}
