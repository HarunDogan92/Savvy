package com.example.savvy.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieImage(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var movieId: String,
    var url: String
)