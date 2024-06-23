package com.example.savvy.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savvy.entities.Budget
import com.example.savvy.repos.BudgetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: BudgetRepository) : ViewModel() {
    private val _budget = MutableStateFlow(listOf<Budget>())
    val budget: StateFlow<List<Budget>> = _budget.asStateFlow()

    init {
        viewModelScope.launch {
            repository.fetchAll().distinctUntilChanged().collect { budget ->
                _budget.value = budget
            }
        }
    }

    fun calculateSum(budgetList: List<Budget>): Int{
        return budgetList.sumOf { it.amount }
    }

    fun addNewBudget(budget: Budget) {
        viewModelScope.launch {
            repository.add(budget)
        }
    }

    fun addNewExpense(budget: Budget) {
        viewModelScope.launch {
            budget.amount = budget.amount.unaryMinus()
            repository.add(budget)
        }
    }
}