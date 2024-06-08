package com.example.savvy.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val selIcon: ImageVector, val unselIcon: ImageVector) {
    data object Home: Screen("homescreen", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    data object Detail: Screen("detailscreen", "Detail", Icons.Filled.Info, Icons.Outlined.Info)
    data object Watchlist: Screen("watchlistscreen", "Watchlist", Icons.Filled.List, Icons.Outlined.List)
}