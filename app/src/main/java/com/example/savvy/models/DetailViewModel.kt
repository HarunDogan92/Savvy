package com.example.savvy.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savvy.repos.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: MovieRepository, private val movieId: String) : ViewModel()  {
    private val _movieDetail = MutableStateFlow<MovieWithImages?>(null)
    val movieDetail: StateFlow<MovieWithImages?> = _movieDetail.asStateFlow()

    init {
        viewModelScope.launch {
            repository.fetchById(movieId).distinctUntilChanged().collect { movieList ->
                _movieDetail.value = movieList
            }
        }
    }

    fun toggleFavoriteMovie(movie: Movie){
        movie.isFavorite = !movie.isFavorite
        viewModelScope.launch {
            repository.update(movie)
        }
    }
}