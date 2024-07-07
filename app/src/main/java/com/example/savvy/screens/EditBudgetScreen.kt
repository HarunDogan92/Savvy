package com.example.savvy.screens

import androidx.compose.runtime.collectAsState

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.savvy.data.SavvyDatabase
import com.example.savvy.entities.Budget
import com.example.savvy.repos.BudgetRepository
import com.example.savvy.viewmodels.HomeViewModel
import com.example.savvy.viewmodels.HomeViewModelFactory
import com.example.savvy.widgets.SimpleTopAppBar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun EditBudgetScreen(budgetId: Long, navController: NavHostController) {
    val db = SavvyDatabase.getDatabase(LocalContext.current, rememberCoroutineScope())
    val repo = BudgetRepository(budgetDao = db.budgetDao())
    val factory = HomeViewModelFactory(repo)
    val vm: HomeViewModel = viewModel(factory = factory)

    val context = LocalContext.current

    // Initiales Budget aus der Datenbank abrufen
    val budget = vm.budget.collectAsState(initial = emptyList()).value.find { it.budgetId == budgetId } ?: return

    var title by rememberSaveable { mutableStateOf(budget.title) }
    var amount by rememberSaveable { mutableStateOf(budget.amount.toString()) }
    var date by rememberSaveable { mutableStateOf(budget.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))) }
    var expanded by remember { mutableStateOf(false) }
    val categories = if (budget.amount < 0) Budget.expensesCategories else Budget.budgetCategories
    var selectedCategory by rememberSaveable { mutableStateOf(budget.category) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { SimpleTopAppBar("Edit Budget/Expense", navController) }
    ) { values ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(values),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = "Title",
                style = MaterialTheme.typography.bodyLarge
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                placeholder = { Text(text = "e.g. Krypto") },
            )
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                text = "Amount",
                style = MaterialTheme.typography.bodyLarge
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = amount,
                onValueChange = { amount = it },
                placeholder = { Text(text = "e.g. 500") },
            )
            Spacer(modifier = Modifier.size(20.dp))
            DateTextField { selectedDate ->
                date = selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
            }
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                text = "Category",
                style = MaterialTheme.typography.bodyLarge
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Button(onClick = { expanded = true }) {
                        Text("Select")
                    }
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            selectedCategory = category
                            expanded = false
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.size(20.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    val updatedBudget = budget.copy(
                        title = title,
                        amount = amount.toInt(),
                        date = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")).plusHours(2),
                        category = selectedCategory
                    )
                    vm.updateBudget(updatedBudget)
                    navController.navigateUp()
                    Toast.makeText(
                        context,
                        "Updated successfully",
                        Toast.LENGTH_LONG
                    ).show()
                },
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(
                    text = "Save",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
