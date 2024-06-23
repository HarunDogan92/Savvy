package com.example.savvy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.savvy.screens.AddBudgetScreen
import com.example.savvy.screens.AddExpensesScreen
import com.example.savvy.screens.EditBudgetScreen
import com.example.savvy.screens.HomeScreen
import com.example.savvy.screens.RecurringScreen

@Composable
fun Navigation() {
    val navController = rememberNavController() // create a NavController instance

    NavHost(navController = navController, // pass the NavController to NavHost
        startDestination = Screen.Home.route) { // pass a start destination
        composable(route = Screen.Home.route){
            HomeScreen(navController)
        }
        composable(route = Screen.AddBudget.route){ backStackEntry ->
            AddBudgetScreen(backStackEntry, navController)
        }
        composable(route = Screen.AddExpense.route){ backStackEntry ->
            AddExpensesScreen(backStackEntry, navController)
        }
        composable(route = "edit_budget/{budgetId}") { backStackEntry ->
            val budgetId = backStackEntry.arguments?.getString("budgetId")?.toLongOrNull() ?: return@composable
            EditBudgetScreen(budgetId, navController)
        }
        composable(route = Screen.Recurring.route) {
            RecurringScreen(navController)
        }
    }
}