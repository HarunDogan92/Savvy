package com.example.savvy.repos

import com.example.savvy.data.IncomeDao
import com.example.savvy.entities.Income
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class IncomeRepository(private val incomeDao: IncomeDao) {

    suspend fun add(income: Income) = incomeDao.add(income)
    suspend fun update(income: Income) = incomeDao.update(income)
    suspend fun delete(income: Income) = incomeDao.delete(income)
    fun fetchAll(): Flow<List<Income>> = incomeDao.fetchAll()
    suspend fun findByDay(date: LocalDate): List<Income> {
        val day = date.dayOfMonth.toString().padStart(2, '0')
        val incomes = incomeDao.findByDay(day)

        val lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth()).dayOfMonth

        if (date.dayOfMonth == lastDayOfMonth && date.monthValue != LocalDate.now().monthValue) {
            val endOfMonthIncomes = findByMonthAndDay(date)
            return incomes + endOfMonthIncomes
        }

        return incomes
    }
    private suspend fun findByMonthAndDay(date: LocalDate): List<Income> {
        val lastDayIncomes = mutableListOf<Income>()
        for (month in java.time.Month.values()) {
            val lastDayOfMonth = LocalDate.of(date.year, month, month.length(date.isLeapYear)).dayOfMonth
            lastDayIncomes.addAll(
                incomeDao.findByMonthAndDay(
                    month.value.toString().padStart(2, '0'),
                    lastDayOfMonth.toString().padStart(2, '0')
                )
            )
        }
        return lastDayIncomes.distinct()
    }
}