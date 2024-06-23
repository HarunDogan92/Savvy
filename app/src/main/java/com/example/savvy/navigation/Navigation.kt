package com.example.savvy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.savvy.screens.AddBudgetScreen
import com.example.savvy.screens.AddExpensesScreen
import com.example.savvy.screens.AddIncomeScreen
import com.example.savvy.screens.AddRecurringExpensesScreen
import com.example.savvy.screens.EditBudgetScreen
import com.example.savvy.screens.EditRecurringScreen
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
        composable(route = Screen.AddIncome.route){ backStackEntry ->
            AddIncomeScreen(backStackEntry, navController)
        }
        composable(route = Screen.AddRecurringExpense.route){ backStackEntry ->
            AddRecurringExpensesScreen(backStackEntry, navController)
        }
        composable(route = "edit_recurring/{incomeId}") { backStackEntry ->
            val incomeId = backStackEntry.arguments?.getString("incomeId")?.toLongOrNull() ?: return@composable
            EditRecurringScreen(incomeId, navController)
        }
    }
}