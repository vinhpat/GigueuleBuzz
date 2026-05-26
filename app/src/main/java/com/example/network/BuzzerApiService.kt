package com.example.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// DTOs
data class ParticipantDto(
    val userName: String,
    val buzzTime: Long? = null
)

data class RoundHistoryDto(
    val roundNumber: Int,
    val winnerName: String? = null
)

data class SessionDto(
    val sessionId: String,
    val sessionName: String? = null,
    val description: String? = null,
    val maxParticipants: Int? = null,
    val status: String, // "waiting", "active", "stopped"
    val questionCounter: Int = 1,
    val startTime: Long? = null,
    val participants: List<ParticipantDto>,
    val roundHistory: List<RoundHistoryDto> = emptyList()
)

data class CreateSessionRequest(
    val sessionName: String,
    val description: String,
    val maxParticipants: Int
)

data class JoinRequest(
    val sessionId: String,
    val userName: String
)

data class GenericSessionRequest(
    val sessionId: String
)

data class BuzzRequest(
    val sessionId: String,
    val userName: String
)

interface BuzzerApiService {
    @POST("api/session/create")
    suspend fun createSession(@Body request: CreateSessionRequest): SessionDto

    @POST("api/session/join")
    suspend fun joinSession(@Body request: JoinRequest): SessionDto

    @POST("api/session/start")
    suspend fun startSession(@Body request: GenericSessionRequest): SessionDto

    @POST("api/session/stop")
    suspend fun stopSession(@Body request: GenericSessionRequest): SessionDto

    @POST("api/session/next-question")
    suspend fun nextQuestion(@Body request: GenericSessionRequest): SessionDto

    @POST("api/session/buzz")
    suspend fun buzz(@Body request: BuzzRequest): SessionDto

    @POST("api/session/reset")
    suspend fun resetSession(@Body request: GenericSessionRequest): SessionDto

    @GET("api/session/{id}")
    suspend fun getSession(@Path("id") sessionId: String): SessionDto
}
