package com.example.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// DTOs
data class SessionDto(
    val sessionId: String,
    val sessionName: String? = null,
    val description: String? = null,
    val maxParticipants: Int? = null,
    val status: String, // "waiting", "active", "finished"
    val winner: String?,
    val participants: List<String>
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

    @POST("api/session/buzz")
    suspend fun buzz(@Body request: BuzzRequest): SessionDto

    @POST("api/session/reset")
    suspend fun resetSession(@Body request: GenericSessionRequest): SessionDto

    @GET("api/session/{id}")
    suspend fun getSession(@Path("id") sessionId: String): SessionDto
}
