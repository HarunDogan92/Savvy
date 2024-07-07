package com.example.savvy.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.savvy.data.SavvyDatabase
import com.example.savvy.entities.Budget
import com.example.savvy.models.BudgetRow
import com.example.savvy.navigation.Screen
import com.example.savvy.repos.BudgetRepository
import com.example.savvy.repos.IncomeRepository
import com.example.savvy.viewmodels.HomeRecurringViewModel
import com.example.savvy.viewmodels.HomeRecurringViewModelFactory
import com.example.savvy.widgets.SimpleBottomAppBar
import com.example.savvy.widgets.SimpleTopAppBar
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun HomeScreen(navController: NavHostController) {
    val db = SavvyDatabase.getDatabase(LocalContext.current, rememberCoroutineScope())
    val budgetRepo = BudgetRepository(budgetDao = db.budgetDao())
    val incomeRepo = IncomeRepository(incomeDao = db.incomeDao())
    val factory = HomeRecurringViewModelFactory(budgetRepo, incomeRepo)
    val viewModel: HomeRecurringViewModel = viewModel(factory = factory)

    val context = LocalContext.current
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    val currentDate by viewModel.currentDate.collectAsState()
    val lastCheckedDate by viewModel.lastCheckedDate.collectAsState()

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                viewModel.setCurrentDate(LocalDate.of(year, month + 1, dayOfMonth))
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setOnDismissListener { showDatePicker = false }
        datePickerDialog.show()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { SimpleTopAppBar(title = "Savvy") },
        bottomBar = {
            Column {
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.SpaceAround
                ){
                    Button(
                        onClick = { navController.navigate(route = Screen.AddBudget.route) },
                        shape = MaterialTheme.shapes.extraLarge) {
                        Text(text = "Add Budget")
                    }
                    Button(
                        onClick = { navController.navigate(route = Screen.AddExpense.route) }) {
                        Text(text = "Add Expense")
                    }
                }
                Spacer(modifier = Modifier.size(10.dp))
                SimpleBottomAppBar(navController)
            }
        }
    ) { values ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(values)
        ) {
            Text(
                text = "Simuliertes heutiges Datum: $currentDate",
                style = MaterialTheme.typography.bodyLarge
            )
            lastCheckedDate?.let {
                Text(
                    text = "Zuletzt überprüft: ${it.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Button(
                onClick = { showDatePicker = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "simulate current Date")
            }
            Button(
                onClick = { viewModel.checkIncomeAndAddBudget() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Ist Gehalt schon da?")
            }
            Text(
                text = "Budget",
                fontSize = 24.sp,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
            Text(
                text = "${viewModel.calculateSum(viewModel.budget.collectAsState().value)}",
                fontSize = 30.sp,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
            BudgetList(
                budgets = viewModel.budget.collectAsState().value,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun BudgetList(
    budgets: List<Budget>,
    navController: NavHostController,
    viewModel: HomeRecurringViewModel
    ) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(budgets) {
                budget -> BudgetRow(
                    budget = budget, navController = navController, viewModel = viewModel
                )
        }
    }
}