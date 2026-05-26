package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.network.NetworkModule
import com.example.repository.BuzzerRepository

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BuzzerViewModel::class.java)) {
            val repository = BuzzerRepository(NetworkModule.apiService)
            @Suppress("UNCHECKED_CAST")
            return BuzzerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
