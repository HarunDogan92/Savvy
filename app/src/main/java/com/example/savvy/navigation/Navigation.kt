package com.example.savvy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.savvy.screens.DetailScreen
import com.example.savvy.screens.HomeScreen
import com.example.savvy.screens.WatchlistScreen

@Composable
fun Navigation() {
    val navController = rememberNavController() // create a NavController instance

    NavHost(navController = navController, // pass the NavController to NavHost
        startDestination = Screen.Home.route) { // pass a start destination
        composable(route = Screen.Home.route){
            HomeScreen(navController)
        }
        composable(route = Screen.Detail.route + "/{movieId}",
                arguments = listOf(
                    navArgument(name = "movieId") {type = NavType.StringType})
        ){ backStackEntry ->
            DetailScreen(backStackEntry,
                navController)
        }
        composable(route = Screen.Watchlist.route) {
            WatchlistScreen(navController)
        }
    }
}