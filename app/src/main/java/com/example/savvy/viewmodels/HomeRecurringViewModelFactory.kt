package com.example.savvy.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.savvy.repos.BudgetRepository
import com.example.savvy.repos.IncomeRepository

class HomeRecurringViewModelFactory(
    private val budgetRepository: BudgetRepository,
    private val incomeRepository: IncomeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeRecurringViewModel::class.java))
            return HomeRecurringViewModel(budgetRepository, incomeRepository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}