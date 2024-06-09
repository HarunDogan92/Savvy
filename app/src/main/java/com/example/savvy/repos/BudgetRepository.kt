package com.example.savvy.repos

import com.example.savvy.data.BudgetDao
import com.example.savvy.entities.Budget
import kotlinx.coroutines.flow.Flow

class BudgetRepository(private val budgetDao: BudgetDao) {

    suspend fun add(budget: Budget) = budgetDao.add(budget)
    suspend fun update(budget: Budget) = budgetDao.update(budget)
    suspend fun delete(budget: Budget) = budgetDao.delete(budget)
    fun fetchAll(): Flow<List<Budget>> = budgetDao.fetchAll()
}