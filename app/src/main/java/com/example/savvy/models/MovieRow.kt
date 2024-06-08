package com.example.savvy.models

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


@Composable
fun MovieRow(movie: MovieWithImages, onItemClick: (String) -> Unit = {}, onFavoriteClick: (Movie) -> Unit = {}){
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)
        .animateContentSize(
            animationSpec = tween(
                delayMillis = 300,
                easing = LinearOutSlowInEasing
            )
        )
        .clickable {
            onItemClick(movie.movie.id)
        },
        shape = ShapeDefaults.Large,
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column {
            MovieImageBox(movie, onFavoriteClick)
            MovieTextRow(movie.movie)
        }
    }
}

@Composable
fun MovieImageBox(movie: MovieWithImages, onFavoriteClick: (Movie) -> Unit) {
    var isFavorite by remember {
        mutableStateOf(movie.movie.isFavorite)
    }
    Box(
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model =  movie.images.firstOrNull()?.url,
            contentScale = ContentScale.Crop,
            contentDescription = "placeholder image")
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            contentAlignment = Alignment.TopEnd
        ){
            Icon(
                modifier = Modifier.clickable {
                    onFavoriteClick(movie.movie)
                    isFavorite = !isFavorite
                },
                imageVector = if (isFavorite) Icons.Default.Favorite
                else Icons.Default.FavoriteBorder,
                contentDescription = "Add to favorites")
        }
    }
}

@Composable
fun MovieTextRow(movie: Movie) {
    var showDetails by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = movie.title)
        Icon(modifier = Modifier
            .clickable {
                showDetails = !showDetails
            },
            imageVector =
            if (showDetails) Icons.Filled.KeyboardArrowDown
            else Icons.Default.KeyboardArrowUp, contentDescription = "show more")
    }
    if (showDetails) {
        MovieDetails(movie)
    }
}

@Composable
fun MovieDetails(movie: Movie) {
    Text("Director: ${movie.director}")
    Text("Released: ${movie.year}")
    Text("Genre: ${movie.genre}")
    Text("Actors: ${movie.actors}")
    Text("Rating: ${movie.rating}")
    Divider(color = Color.Gray, thickness = 1.dp)
    Text("Plot: ${movie.plot}")
}