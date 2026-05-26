package com.example.repository

import com.example.network.BuzzRequest
import com.example.network.BuzzerApiService
import com.example.network.CreateSessionRequest
import com.example.network.GenericSessionRequest
import com.example.network.JoinRequest
import com.example.network.SessionDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BuzzerRepository(private val apiService: BuzzerApiService) {

    suspend fun createSession(sessionName: String, description: String, maxParticipants: Int): Result<SessionDto> {
        return try {
            Result.success(apiService.createSession(CreateSessionRequest(sessionName, description, maxParticipants)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun joinSession(sessionId: String, userName: String): Result<SessionDto> {
        return try {
            Result.success(apiService.joinSession(JoinRequest(sessionId, userName)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun startSession(sessionId: String): Result<SessionDto> {
        return try {
            Result.success(apiService.startSession(GenericSessionRequest(sessionId)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun stopSession(sessionId: String): Result<SessionDto> {
        return try {
            Result.success(apiService.stopSession(GenericSessionRequest(sessionId)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun nextQuestion(sessionId: String): Result<SessionDto> {
        return try {
            Result.success(apiService.nextQuestion(GenericSessionRequest(sessionId)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun buzz(sessionId: String, userName: String): Result<SessionDto> {
        return try {
            Result.success(apiService.buzz(BuzzRequest(sessionId, userName)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetSession(sessionId: String): Result<SessionDto> {
        return try {
            Result.success(apiService.resetSession(GenericSessionRequest(sessionId)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSession(sessionId: String): Result<SessionDto> {
        return try {
            Result.success(apiService.getSession(sessionId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Polling mechanism
    fun pollSession(sessionId: String, intervalMs: Long = 1000L): Flow<Result<SessionDto>> = flow {
        while (true) {
            try {
                val session = apiService.getSession(sessionId)
                emit(Result.success(session))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
            delay(intervalMs)
        }
    }
}
