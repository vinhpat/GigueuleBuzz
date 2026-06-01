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

  @Test
  fun testMoshiJsonParsingSafety() {
      val moshi = com.squareup.moshi.Moshi.Builder()
          .add(com.example.network.ParticipantAdapter())
          .add(com.example.network.SafeLongAdapter)
          .add(com.example.network.SafeIntAdapter)
          .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
          .build()

      val json = """
          {
              "sessionId": "TEST1",
              "sessionName": "Diagnostic Test",
              "status": "waiting",
              "questionCounter": 1,
              "participants": [
                  {"userName": "Alice", "buzzTime": null},
                  {"userName": "Bob", "buzzTime": 1717257917719}
              ],
              "roundHistory": []
          }
      """.trimIndent()

      val adapter = moshi.adapter(com.example.network.SessionDto::class.java)
      val session = adapter.fromJson(json)
      org.junit.Assert.assertNotNull(session)
      assertEquals("TEST1", session?.sessionId)
      assertEquals("Alice", session?.participants?.get(0)?.userName)
      assertEquals(null, session?.participants?.get(0)?.buzzTime)
      assertEquals(1717257917719L, session?.participants?.get(1)?.buzzTime)
  }
}

