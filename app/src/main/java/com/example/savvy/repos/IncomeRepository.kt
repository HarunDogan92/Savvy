package com.example.savvy.repos

import com.example.savvy.data.IncomeDao
import com.example.savvy.entities.Income
import kotlinx.coroutines.flow.Flow

class IncomeRepository(private val incomeDao: IncomeDao) {

    suspend fun add(income: Income) = incomeDao.add(income)
    suspend fun update(income: Income) = incomeDao.update(income)
    suspend fun delete(income: Income) = incomeDao.delete(income)
    fun fetchAll(): Flow<List<Income>> = incomeDao.fetchAll()
}