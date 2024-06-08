package com.example.savvy.repos

import androidx.room.Query
import com.example.savvy.data.MovieDao
import com.example.savvy.models.Movie
import com.example.savvy.models.MovieWithImages
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val movieDao: MovieDao) {

    suspend fun add(movie: Movie) = movieDao.add(movie)
    suspend fun update(movie: Movie) = movieDao.update(movie)
    suspend fun delete(movie: Movie) = movieDao.delete(movie)
    fun fetchById(movieId: String): Flow<MovieWithImages> = movieDao.fetchById(movieId)
    fun fetchAll(): Flow<List<Movie>> = movieDao.fetchAll()
    fun fetchAllWithImages(): Flow<List<MovieWithImages>> = movieDao.fetchAllWithImages()
    fun fetchAllFavorites(): Flow<List<MovieWithImages>> = movieDao.fetchAllFavorites()
}