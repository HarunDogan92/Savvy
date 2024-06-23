package com.example.savvy.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.savvy.entities.Income
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface IncomeDao {
    @Insert
    suspend fun add(income: Income)

    @Insert
    suspend fun addAll(income: List<Income>)

    @Update
    suspend fun update(income: Income)

    @Delete
    suspend fun delete(income: Income)

    @Query("SELECT * from income")
    fun fetchAll(): Flow<List<Income>>

    @Query("SELECT * FROM Income WHERE date = :date")
    suspend fun findByDate(date: LocalDate): List<Income>
}