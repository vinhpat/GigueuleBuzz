package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.SessionDto
import com.example.repository.BuzzerRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BuzzerUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val session: SessionDto? = null,
    val isMaster: Boolean = false,
    val userName: String? = null
)

class BuzzerViewModel(
    private val repository: BuzzerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BuzzerUiState())
    val uiState: StateFlow<BuzzerUiState> = _uiState.asStateFlow()

    private var pollJob: Job? = null

    fun createSession(sessionName: String, description: String, maxParticipants: Int) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            repository.createSession(sessionName, description, maxParticipants).onSuccess { session ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        session = session,
                        isMaster = true,
                        userName = "Master"
                    )
                }
                startPolling(session.sessionId)
            }.onFailure { error ->
                val getErrorMessage = if (error is retrofit2.HttpException && error.code() == 404) {
                    "Session not found."
                } else if (error is retrofit2.HttpException && error.code() == 401) {
                    "API Access Denied (HTTP 401). If using Vercel, please disable 'Vercel Authentication' for preview deployments."
                } else if (error is java.net.UnknownHostException || error is java.net.ConnectException || error is java.net.SocketTimeoutException) {
                    "Unable to connect to the server."
                } else {
                    error.message ?: "An unknown error occurred"
                }
                _uiState.update { it.copy(isLoading = false, errorMessage = getErrorMessage) }
            }
        }
    }

    fun resumeSessionHost(sessionId: String) {
        if (sessionId.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Session ID cannot be empty") }
            return
        }
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            repository.getSession(sessionId.uppercase()).onSuccess { session ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        session = session,
                        isMaster = true,
                        userName = "Master"
                    )
                }
                startPolling(session.sessionId)
            }.onFailure { error ->
                val getErrorMessage = if (error is retrofit2.HttpException && error.code() == 404) {
                    "Session not found."
                } else if (error is retrofit2.HttpException && error.code() == 401) {
                    "API Access Denied (HTTP 401). If using Vercel, please disable 'Vercel Authentication' for preview deployments."
                } else if (error is java.net.UnknownHostException || error is java.net.ConnectException || error is java.net.SocketTimeoutException) {
                    "Unable to connect to the server."
                } else {
                    error.message ?: "An unknown error occurred"
                }
                _uiState.update { it.copy(isLoading = false, errorMessage = getErrorMessage) }
            }
        }
    }
    
    fun joinSession(sessionId: String, userName: String) {
        if (sessionId.isBlank() || userName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Session ID and Name cannot be empty") }
            return
        }
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            repository.joinSession(sessionId.uppercase(), userName).onSuccess { session ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        session = session,
                        isMaster = false,
                        userName = userName
                    )
                }
                startPolling(session.sessionId)
            }.onFailure { error ->
                val getErrorMessage = if (error is retrofit2.HttpException && error.code() == 404) {
                    "Session not found."
                } else if (error is retrofit2.HttpException && error.code() == 401) {
                    "API Access Denied (HTTP 401). If using Vercel, please disable 'Vercel Authentication' for preview deployments."
                } else if (error is java.net.UnknownHostException || error is java.net.ConnectException || error is java.net.SocketTimeoutException) {
                    "Unable to connect to the server."
                } else {
                    error.message ?: "An unknown error occurred"
                }
                _uiState.update { it.copy(isLoading = false, errorMessage = getErrorMessage, userName = null) }
            }
        }
    }

    private fun startPolling(sessionId: String) {
        pollJob?.cancel()
        pollJob = viewModelScope.launch {
            repository.pollSession(sessionId, 1000L).collect { result ->
                result.onSuccess { session ->
                    _uiState.update { it.copy(session = session, errorMessage = null) }
                }.onFailure { error ->
                    val errorDetails = if (error is retrofit2.HttpException) {
                        try {
                            error.response()?.errorBody()?.string() ?: "No error body"
                        } catch (e: Exception) {
                            "Could not read error body"
                        }
                    } else {
                        "Not an HTTP exception"
                    }
                    android.util.Log.e("BuzzerViewModel", "Polling failed for sessionId=$sessionId. Error: ${error.message}. Details: $errorDetails", error)
                    
                    if (error is retrofit2.HttpException && error.code() == 404) {
                        _uiState.update { it.copy(session = null, errorMessage = "Session '$sessionId' was ended or no longer exists. Server responded 404: $errorDetails") }
                        pollJob?.cancel()
                    } else if (error is java.net.UnknownHostException || error is java.net.ConnectException || error is java.net.SocketTimeoutException) {
                        _uiState.update { it.copy(errorMessage = "Connection issues... retrying context.") }
                    } else {
                        _uiState.update { it.copy(errorMessage = "Connection error. Session '$sessionId'. " + (error.message ?: "")) }
                    }
                }
            }
        }
    }

    fun startQuiz() {
        val sessionId = _uiState.value.session?.sessionId ?: return
        viewModelScope.launch {
            repository.startSession(sessionId).onFailure { error ->
                _uiState.update { it.copy(errorMessage = error.message) }
            }
        }
    }

    fun stopQuiz() {
        val sessionId = _uiState.value.session?.sessionId ?: return
        viewModelScope.launch {
            repository.stopSession(sessionId).onFailure { error ->
                _uiState.update { it.copy(errorMessage = error.message) }
            }
        }
    }

    fun nextQuestion() {
        val sessionId = _uiState.value.session?.sessionId ?: return
        viewModelScope.launch {
            repository.nextQuestion(sessionId).onFailure { error ->
                _uiState.update { it.copy(errorMessage = error.message) }
            }
        }
    }

    fun buzz() {
        val sessionId = _uiState.value.session?.sessionId ?: return
        val userName = _uiState.value.userName ?: return
        
        // Optimistic update for responsive UI optionally.
        viewModelScope.launch {
            repository.buzz(sessionId, userName).onFailure { error ->
                _uiState.update { it.copy(errorMessage = error.message) }
            }
        }
    }

    fun resetQuiz() {
        val sessionId = _uiState.value.session?.sessionId ?: return
        viewModelScope.launch {
            repository.resetSession(sessionId).onFailure { error ->
                _uiState.update { it.copy(errorMessage = error.message) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
    
    fun leaveSession() {
        android.util.Log.d("BuzzerViewModel", "Leaving session, canceling poll job.")
        pollJob?.cancel()
        pollJob = null
        _uiState.update { it.copy(session = null, errorMessage = null, userName = null, isMaster = false) }
    }
    
    override fun onCleared() {
        super.onCleared()
        pollJob?.cancel()
    }
}
