package com.example.savvy.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.savvy.entities.Budget
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Insert
    suspend fun add(budget: Budget)

    @Insert
    suspend fun addAll(budget: List<Budget>)

    @Update
    suspend fun update(budget: Budget)

    @Delete
    suspend fun delete(budget: Budget)

    @Query("SELECT * from budget order by date desc")
    fun fetchAll(): Flow<List<Budget>>

}