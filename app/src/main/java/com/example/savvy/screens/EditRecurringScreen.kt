package com.example.savvy.screens

import androidx.compose.runtime.collectAsState

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.savvy.entities.Income
import com.example.savvy.repos.IncomeRepository
import com.example.savvy.viewmodels.RecurringViewModel
import com.example.savvy.viewmodels.RecurringViewModelFactory
import com.example.savvy.widgets.SimpleTopAppBar
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun EditRecurringScreen(incomeId: Long, navController: NavHostController) {
    val db = SavvyDatabase.getDatabase(LocalContext.current, rememberCoroutineScope())
    val repo = IncomeRepository(incomeDao = db.incomeDao())
    val factory = RecurringViewModelFactory(repo)
    val vm: RecurringViewModel = viewModel(factory = factory)

    val context = LocalContext.current

    // Initiales Budget aus der Datenbank abrufen
    val income = vm.income.collectAsState(initial = emptyList()).value.find { it.incomeId == incomeId } ?: return

    var title by rememberSaveable { mutableStateOf(income.title) }
    var amount by rememberSaveable { mutableStateOf(income.amount.toString()) }
    var date by rememberSaveable { mutableStateOf(income.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))) }
    var expanded by remember { mutableStateOf(false) }
    val categories = if (income.amount < 0) Income.expensesCategories else Income.incomeCategories
    var selectedCategory by rememberSaveable { mutableStateOf(income.category) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { SimpleTopAppBar("Edit Income/Expense", navController) }
    ) { values ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(values)
        ) {
            Text(
                text = "Title",
                style = MaterialTheme.typography.bodyLarge
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                placeholder = { Text(text = "e.g. Salary") },
            )
            Text(
                text = "Amount",
                style = MaterialTheme.typography.bodyLarge
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = amount,
                onValueChange = { amount = it },
                placeholder = { Text(text = "e.g. 3000") },
            )
            DateTextField { selectedDate ->
                date = selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            }

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

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    val updatedIncome = income.copy(
                        title = title,
                        amount = amount.toInt(),
                        date = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                        category = selectedCategory
                    )
                    vm.updateIncome(updatedIncome)
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
