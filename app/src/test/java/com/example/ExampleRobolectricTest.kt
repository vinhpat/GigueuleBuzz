package com.example

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.core.app.ApplicationProvider
import com.example.network.ParticipantDto
import com.example.network.SessionDto
import com.example.ui.screens.SessionScreen
import com.example.viewmodel.BuzzerUiState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("PaddyBuzz", appName)
  }

  @Test
  fun testSessionScreenParticipantRendering() {
      val session = SessionDto(
          sessionId = "XJ99",
          sessionName = "PaddyBuzz",
          status = "waiting",
          questionCounter = 1,
          participants = listOf(ParticipantDto("Alice"), ParticipantDto("Bob"))
      )
      val uiState = BuzzerUiState(
          session = session,
          isMaster = false,
          userName = "Alice"
      )
      composeTestRule.setContent {
          SessionScreen(
              uiState = uiState,
              onStartQuiz = {},
              onStopQuiz = {},
              onResetQuiz = {},
              onBuzz = {},
              onNextQuestion = {},
              onLeave = {}
          )
      }
      composeTestRule.onRoot().assertExists()
  }

  @Test
  fun testSessionScreenMasterRendering() {
      val session = SessionDto(
          sessionId = "XJ99",
          sessionName = "PaddyBuzz",
          status = "active",
          questionCounter = 1,
          startTime = System.currentTimeMillis() - 5000,
          participants = listOf(
              ParticipantDto("Alice", buzzTime = System.currentTimeMillis() - 3000), 
              ParticipantDto("Bob")
          )
      )
      val uiState = BuzzerUiState(
          session = session,
          isMaster = true,
          userName = "Master"
      )
      composeTestRule.setContent {
          SessionScreen(
              uiState = uiState,
              onStartQuiz = {},
              onStopQuiz = {},
              onResetQuiz = {},
              onBuzz = {},
              onNextQuestion = {},
              onLeave = {}
          )
      }
      composeTestRule.onRoot().assertExists()
  }
}

