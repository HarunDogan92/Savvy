package com.example.savvy.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.savvy.data.MovieDatabase
import com.example.savvy.models.HomeViewModel
import com.example.savvy.models.HomeViewModelFactory
import com.example.savvy.models.Movie
import com.example.savvy.models.MovieRow
import com.example.savvy.models.MovieWithImages
import com.example.savvy.navigation.Screen
import com.example.savvy.repos.MovieRepository
import com.example.savvy.widgets.SimpleBottomAppBar
import com.example.savvy.widgets.SimpleTopAppBar

@Composable
fun HomeScreen(navController: NavHostController) {
    val db = MovieDatabase.getDatabase(LocalContext.current, rememberCoroutineScope())
    val repo = MovieRepository(movieDao = db.movieDao())
    val factory = HomeViewModelFactory(repo)
    val viewModel: HomeViewModel = viewModel(factory = factory)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { SimpleTopAppBar(title = "Movie App") },
        bottomBar = { SimpleBottomAppBar(navController) }
    ) {
            values -> MovieList(movies = viewModel.movieList.collectAsState().value, values = values,
                navController = navController,
                onFavoriteClick = { movie -> viewModel.toggleFavoriteMovie(movie) }
                )
    }
}

@Composable
fun MovieList(
    movies: List<MovieWithImages>,
    values: PaddingValues,
    navController: NavHostController,
    onFavoriteClick: (Movie) -> Unit = {},
    ) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(values)
    ) {
        items(movies) {
                movie -> MovieRow(
                    movie = movie,
                    onItemClick = {movieId -> navController.navigate(route = Screen.Detail.route + "/$movieId") },
                    onFavoriteClick = onFavoriteClick
                )
        }
    }
}