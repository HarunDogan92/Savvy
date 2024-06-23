package com.example.savvy.models

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.savvy.R
import com.example.savvy.entities.Budget
import com.example.savvy.viewmodels.HomeViewModel
import java.time.format.DateTimeFormatter


@Composable
fun BudgetRow(budget: Budget, navController: NavHostController, viewModel: HomeViewModel){
    var showDetails by remember {
        mutableStateOf(false)
    }
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)
        .animateContentSize(
            animationSpec = tween(
                delayMillis = 300,
                easing = LinearOutSlowInEasing
            )
        )
        .clickable {
            showDetails = !showDetails
            navController.navigate("edit_budget/${budget.budgetId}")
        },
        shape = ShapeDefaults.Large,
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Amount: ${budget.amount} / ")
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            Text("Date: ${budget.date.format(formatter)}", modifier = Modifier.weight(1f))
            if (showDetails) {
                BudgetDetails(budget)
            }
            IconButton(onClick = {
                viewModel.removeBudget(budget)
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete),
                    contentDescription = "Delete"
                )
            }
        }
    }
}

@Composable
fun BudgetDetails(budget: Budget) {
    Text("TODO")
}