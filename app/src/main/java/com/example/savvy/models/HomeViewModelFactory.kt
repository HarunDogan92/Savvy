package com.example.savvy.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.savvy.repos.MovieRepository

class HomeViewModelFactory(private val repository: MovieRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java))
            return HomeViewModel(repository = repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}