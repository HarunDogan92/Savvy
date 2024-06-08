package com.example.savvy.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savvy.repos.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _movieList = MutableStateFlow(listOf<MovieWithImages>())
    val movieList: StateFlow<List<MovieWithImages>> = _movieList.asStateFlow()

    init {
        viewModelScope.launch {
            repository.fetchAllWithImages().distinctUntilChanged().collect { movieList ->
                _movieList.value = movieList
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