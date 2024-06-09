package com.example.savvy.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.savvy.repos.BudgetRepository

class HomeViewModelFactory(private val repository: BudgetRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java))
            return HomeViewModel(repository = repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}