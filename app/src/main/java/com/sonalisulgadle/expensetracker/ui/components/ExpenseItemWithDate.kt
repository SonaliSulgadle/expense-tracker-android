package com.sonalisulgadle.expensetracker.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.sonalisulgadle.expensetracker.domain.model.Expense
import com.sonalisulgadle.expensetracker.ui.expense.ExpenseItem
import com.sonalisulgadle.expensetracker.ui.theme.MonoTextStyle
import com.sonalisulgadle.expensetracker.util.DateUtils

@Composable
fun ExpenseItemWithDate(
    expense: Expense,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        ExpenseItem(
            expense = expense,
            onClick = onClick,
            trailingContent = {
                Text(
                    text = formatExpenseDate(expense.timestamp),
                    style = MonoTextStyle.copy(fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )
    }
}

private fun formatExpenseDate(timestamp: Long): String = DateUtils.formatShortDate(timestamp)
