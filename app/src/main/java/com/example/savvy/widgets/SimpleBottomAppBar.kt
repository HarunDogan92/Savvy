package com.example.savvy.widgets

import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.savvy.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleBottomAppBar(navController: NavHostController) {
    val items = listOf(
        Screen.Home,
        Screen.Watchlist
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any {
                    it.route == item.route
                } == true,
                onClick = { navController.navigate(item.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                } },
                label = {
                    Text(text = item.title)
                },
                icon = { BadgedBox(badge = {}) {
                    Icon(
                        imageVector =
                            if (currentDestination?.hierarchy?.any {
                                    it.route == item.route } == true) {
                                item.selIcon
                            } else {
                                item.unselIcon
                            },
                        contentDescription = item.title)
                    }
                }
            )
        }
    }
}