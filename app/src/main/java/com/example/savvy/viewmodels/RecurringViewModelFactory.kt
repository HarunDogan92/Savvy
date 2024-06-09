package com.example.savvy.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.savvy.repos.IncomeRepository

class RecurringViewModelFactory(private val repository: IncomeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecurringViewModel::class.java))
            return RecurringViewModel(repository = repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}