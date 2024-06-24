package com.example.savvy.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savvy.entities.Budget
import com.example.savvy.entities.Income
import com.example.savvy.repos.BudgetRepository
import com.example.savvy.repos.IncomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class HomeRecurringViewModel(
    private val budgetRepository: BudgetRepository,
    private val incomeRepository: IncomeRepository
) : ViewModel() {
    private val _budget = MutableStateFlow(listOf<Budget>())
    val budget: StateFlow<List<Budget>> = _budget.asStateFlow()

    private val _income = MutableStateFlow(listOf<Income>())
    val income: StateFlow<List<Income>> = _income.asStateFlow()

    private val _currentDate = MutableStateFlow(LocalDate.now())
    val currentDate: StateFlow<LocalDate> = _currentDate.asStateFlow()

    private val _lastCheckedDate = MutableStateFlow<ZonedDateTime?>(null)
    val lastCheckedDate: StateFlow<ZonedDateTime?> = _lastCheckedDate.asStateFlow()

    init {
        viewModelScope.launch {
            budgetRepository.fetchAll().distinctUntilChanged().collect { budget ->
                _budget.value = budget
            }
            incomeRepository.fetchAll().distinctUntilChanged().collect { income ->
                _income.value = income
            }
        }
    }

    fun calculateSum(budgetList: List<Budget>): Int{
        return budgetList.sumOf { it.amount }
    }

    fun removeBudget(budget: Budget) {
        viewModelScope.launch {
            budgetRepository.delete(budget)
        }
    }

    fun setCurrentDate(date: LocalDate) {
        _currentDate.value = date
    }

    fun checkIncomeAndAddBudget() {
        val today = _currentDate.value

        viewModelScope.launch {
            val incomesToday = incomeRepository.findByDay(today)
            _lastCheckedDate.value = ZonedDateTime.now(ZoneId.of("Europe/Berlin"))
            if (incomesToday.isNotEmpty()) {
                incomesToday.forEach { income ->
                    val incomeDay = income.date.dayOfMonth
                    val lastDayOfMonth = today.lengthOfMonth()
                    val adjustedDate = if (incomeDay > lastDayOfMonth) {
                        today.withDayOfMonth(lastDayOfMonth)
                    } else {
                        today.withDayOfMonth(incomeDay)
                    }
                    val newBudget = Budget(
                        title = income.title,
                        date = LocalDateTime.of(adjustedDate , LocalTime.MIDNIGHT),
                        amount = income.amount,
                        category = income.category
                    )
                    budgetRepository.add(newBudget)
                }
            }
        }
    }
}