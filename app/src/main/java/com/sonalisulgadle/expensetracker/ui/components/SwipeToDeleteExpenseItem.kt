package com.sonalisulgadle.expensetracker.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.sonalisulgadle.expensetracker.domain.model.Expense

private const val SWIPE_DISMISS_THRESHOLD = 0.4f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteExpenseItem(
    expense: Expense,
    onDelete: (Expense) -> Unit,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Track if item has been dismissed to avoid re-triggering
    var isDismissed by remember { mutableStateOf(false) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart && !isDismissed) {
                isDismissed = true
                onDelete(expense)
                true
            } else {
                false
            }
        },
        // Require swipe past 40% of width to trigger delete
        positionalThreshold = { totalDistance -> totalDistance * SWIPE_DISMISS_THRESHOLD }
    )

    // Reset dismissed state when expense changes
    LaunchedEffect(expense.id) {
        isDismissed = false
    }

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            DeleteBackground(dismissState.targetValue)
        }
    ) {
        ExpenseItemWithDate(expense = expense, onClick = onItemClick)
    }
}
