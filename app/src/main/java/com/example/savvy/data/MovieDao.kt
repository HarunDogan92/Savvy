package com.example.savvy.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.savvy.models.Movie
import com.example.savvy.models.MovieImage
import com.example.savvy.models.MovieWithImages
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert
    suspend fun add(movie: Movie)

    @Insert
    suspend fun addAll(movie: List<Movie>)

    @Update
    suspend fun update(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Insert
    suspend fun addImages(image: List<MovieImage>)

    @Query("SELECT * FROM movie where id =:movieId")
    fun fetchById(movieId: String): Flow<MovieWithImages>

    @Query("SELECT * from movie")
    fun fetchAll(): Flow<List<Movie>>

    @Transaction
    @Query("SELECT * FROM movie")
    fun fetchAllWithImages(): Flow<List<MovieWithImages>>

    @Query("SELECT * FROM movie where is_favorite = 1")
    fun fetchAllFavorites(): Flow<List<MovieWithImages>>

}