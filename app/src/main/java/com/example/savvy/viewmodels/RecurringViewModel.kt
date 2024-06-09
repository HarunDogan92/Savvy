package com.example.savvy.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savvy.entities.Income
import com.example.savvy.repos.IncomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class RecurringViewModel(private val repository: IncomeRepository) : ViewModel() {
    private val _income = MutableStateFlow(listOf<Income>())
    val income: StateFlow<List<Income>> = _income.asStateFlow()

    init {
        viewModelScope.launch {
            repository.fetchAll().distinctUntilChanged().collect { income ->
                _income.value = income
            }
        }
    }

}