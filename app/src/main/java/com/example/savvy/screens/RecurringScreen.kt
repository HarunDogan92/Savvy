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
import com.example.savvy.data.SavvyDatabase
import com.example.savvy.entities.Budget
import com.example.savvy.entities.Income
import com.example.savvy.models.RecurringRow
import com.example.savvy.models.SpendingRow
import com.example.savvy.viewmodels.RecurringViewModel
import com.example.savvy.viewmodels.RecurringViewModelFactory
import com.example.savvy.repos.IncomeRepository
import com.example.savvy.widgets.SimpleBottomAppBar
import com.example.savvy.widgets.SimpleTopAppBar

@Composable
fun RecurringScreen(navController: NavHostController) {
    val db = SavvyDatabase.getDatabase(LocalContext.current, rememberCoroutineScope())
    val repo = IncomeRepository(incomeDao = db.incomeDao())
    val factory = RecurringViewModelFactory(repo)
    val viewModel: RecurringViewModel = viewModel(factory = factory)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { SimpleTopAppBar(title = "Recurring Income/Expenses") },
        bottomBar = { SimpleBottomAppBar(navController) }
    ) {
            values -> RecurringList(income = viewModel.income.collectAsState().value,
                values = values)
    }
}

@Composable
fun RecurringList(
    income: List<Income>,
    values: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(values)
    ) {
        items(income) {
                income -> RecurringRow(
            income = income
        )
        }
    }
}