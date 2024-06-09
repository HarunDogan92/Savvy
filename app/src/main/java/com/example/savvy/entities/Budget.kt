package com.example.savvy.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.Date

@Entity
data class Budget(
    @PrimaryKey(autoGenerate = true)
    var budgetId: Long = 0,
    var title: String,
    var date: LocalDateTime = LocalDateTime.now(),
    var amount: Int
)