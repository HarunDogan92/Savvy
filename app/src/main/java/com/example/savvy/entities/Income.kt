package com.example.savvy.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
data class Income(
    @PrimaryKey(autoGenerate = true)
    var incomeId: Long = 0,
    var title: String,
    var date: LocalDate  = LocalDate.now(),
    var amount: Int
)