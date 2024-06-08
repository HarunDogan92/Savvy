package com.example.savvy.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.savvy.data.MovieDatabase
import com.example.savvy.models.DetailViewModel
import com.example.savvy.models.DetailViewModelFactory
import com.example.savvy.models.Movie
import com.example.savvy.models.MovieImage
import com.example.savvy.models.MovieRow
import com.example.savvy.repos.MovieRepository
import com.example.savvy.widgets.SimpleTopAppBar

@Composable
fun DetailScreen(backStackEntry: NavBackStackEntry, navController: NavHostController) {
    val movieId = backStackEntry.arguments?.getString("movieId")
    if (movieId != null) {
        val db = MovieDatabase  .getDatabase(LocalContext.current, rememberCoroutineScope())
        val repo = MovieRepository(movieDao = db.movieDao())
        val factory = DetailViewModelFactory(repo, movieId)
        val vm: DetailViewModel = viewModel(factory = factory)

        val movie = vm.movieDetail.collectAsState().value
        if (movie != null) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { SimpleTopAppBar(movie.movie.title, navController) }
            ) { values ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(values)
                ) {
                    MovieRow(
                        movie,
                        onFavoriteClick = { movie -> vm.toggleFavoriteMovie(movie) })
                    MoviePlayer(movie.movie)
                    MovieImages(movie.images)
                }
            }
        } else {
            Text("Movie not found")
        }
    } else {
        Text("MovieId not found")
    }
}

@Composable
fun MoviePlayer(movie: Movie) {
    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val context = LocalContext.current

    // The getIdentifier function is marked as "discouraged" but I couldn't find any other easier way
    val mediaItem = MediaItem.fromUri("android.resource://${context.packageName}/" +
            context.resources.getIdentifier(movie.trailer, "raw", context.packageName))

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            exoPlayer.release()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f),
        factory = {
            PlayerView(context).also { playerView ->
                playerView.player = exoPlayer
            }
        },
        update = {
            when(lifecycle) {
                Lifecycle.Event.ON_RESUME -> {
                    it.onPause()
                    it.player?.pause()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    it.onResume()
                }
                else -> Unit
            }
        }
    )
}

@Composable
fun MovieImages(movieImages: List<MovieImage>) {
    LazyRow(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(movieImages) { image ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = ShapeDefaults.Large,
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                AsyncImage(
                    model = image.url,
                    contentScale = ContentScale.Fit,
                    contentDescription = "movie images"
                )
            }
        }
    }
}