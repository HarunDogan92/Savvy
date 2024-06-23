package com.example.savvy.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.savvy.data.SavvyDatabase
import com.example.savvy.viewmodels.HomeViewModel
import com.example.savvy.viewmodels.HomeViewModelFactory
import com.example.savvy.entities.Budget
import com.example.savvy.models.BudgetRow
import com.example.savvy.navigation.Screen
import com.example.savvy.repos.BudgetRepository
import com.example.savvy.widgets.SimpleBottomAppBar
import com.example.savvy.widgets.SimpleTopAppBar

@Composable
fun HomeScreen(navController: NavHostController) {
    val db = SavvyDatabase.getDatabase(LocalContext.current, rememberCoroutineScope())
    val repo = BudgetRepository(budgetDao = db.budgetDao())
    val factory = HomeViewModelFactory(repo)
    val viewModel: HomeViewModel = viewModel(factory = factory)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { SimpleTopAppBar(title = "Savvy") },
        bottomBar = { SimpleBottomAppBar(navController) }
    ) { values ->
        Column(
            modifier = Modifier
            .fillMaxSize()
            .padding(values)
        ) {
            Text(
                text = "Budget: ${viewModel.calculateSum(viewModel.budget.collectAsState().value)}",
                fontSize = 24.sp
            )
            Button(onClick = { navController.navigate(route = Screen.AddBudget.route) }) {
                Text(text = "Add Budget")
            }
            Button(onClick = { navController.navigate(route = Screen.AddExpense.route) }) {
                Text(text = "Add Expense")
            }
            BudgetList(
                budgets = viewModel.budget.collectAsState().value,
                values = values,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun BudgetList(
    budgets: List<Budget>,
    values: PaddingValues,
    navController: NavHostController,
    viewModel: HomeViewModel
    ) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(values)
    ) {
        items(budgets) {
                budget -> BudgetRow(
                    budget = budget, navController = navController, viewModel = viewModel
                )
        }
    }
}