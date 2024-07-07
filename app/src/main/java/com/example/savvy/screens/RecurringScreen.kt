package com.example.savvy.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.savvy.data.SavvyDatabase
import com.example.savvy.entities.Income
import com.example.savvy.models.RecurringRow
import com.example.savvy.navigation.Screen
import com.example.savvy.repos.IncomeRepository
import com.example.savvy.viewmodels.RecurringViewModel
import com.example.savvy.viewmodels.RecurringViewModelFactory
import com.example.savvy.widgets.SimpleBottomAppBar
import com.example.savvy.widgets.SimpleTopAppBar

@Composable
fun RecurringScreen(navController: NavHostController) {
    val db = SavvyDatabase.getDatabase(LocalContext.current, rememberCoroutineScope())
    val repo = IncomeRepository(incomeDao = db.incomeDao())
    val factory = RecurringViewModelFactory(repo)
    val viewModel: RecurringViewModel = viewModel(factory = factory)

    var hasNotificationPermission = false
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { hasNotificationPermission = it }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { SimpleTopAppBar(title = "Recurring Income/Expenses") },
        bottomBar = {
            Column {
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.SpaceAround
                ) {
                    Button(onClick = {
                        if (!hasNotificationPermission) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        }
                        navController.navigate(route = Screen.AddIncome.route) }) {
                        Text(text = "Add Income")
                    }
                    Button(onClick = { navController.navigate(route = Screen.AddRecurringExpense.route) }) {
                        Text(text = "Add Bill")
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
            RecurringList(
                income = viewModel.income.collectAsState().value,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun RecurringList(
    income: List<Income>,
    navController: NavHostController,
    viewModel: RecurringViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(income) {
                income -> RecurringRow(
            income = income, navController = navController, viewModel = viewModel
        )
        }
    }
}