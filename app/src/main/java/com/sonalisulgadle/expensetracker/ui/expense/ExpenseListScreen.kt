package com.sonalisulgadle.expensetracker.ui.expense

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sonalisulgadle.expensetracker.R
import com.sonalisulgadle.expensetracker.ui.components.EmptyState
import com.sonalisulgadle.expensetracker.ui.components.SwipeToDeleteExpenseItem
import com.sonalisulgadle.expensetracker.ui.dashboard.CategoryChipsRow
import com.sonalisulgadle.expensetracker.ui.theme.Dimens
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    onBackClick: () -> Unit,
    onNavigateToCategory: (String, String) -> Unit,
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    val uiState by viewModel.expenseListState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {

            // ---- Header ----
            item {
                ExpenseListHeader(
                    currentMonth = uiState.currentMonth,
                    expenseCount = uiState.expenses.size,
                    onBackClick = onBackClick,
                    modifier = Modifier.padding(
                        start = Dimens.PaddingExtraLarge,
                        end = Dimens.PaddingExtraLarge,
                        top = Dimens.PaddingExtraLarge,
                        bottom = Dimens.PaddingSmall
                    )
                )
            }

            // ---- Summary card ----
            item {
                ExpenseSummaryCard(
                    totalSpent = uiState.totalSpent,
                    expenseCount = uiState.expenses.size,
                    avgPerDay = uiState.avgPerDay,
                    modifier = Modifier.padding(
                        horizontal = Dimens.PaddingExtraLarge,
                        vertical = Dimens.PaddingSmall
                    )
                )
            }

            // ---- Category filter chips ----
            item {
                CategoryChipsRow(
                    categoryTotals = uiState.categoryTotals,
                    modifier = Modifier.padding(vertical = Dimens.PaddingSmall)
                )
            }

            // ---- Empty state ----
            if (uiState.expenses.isEmpty()) {
                item { EmptyState() }
                return@LazyColumn
            }

            // ---- Grouped expenses ----
            uiState.groupedExpenses.forEach { (month, expenses) ->

                // Month header
                item(key = "header_$month") {
                    MonthHeader(
                        month = month,
                        modifier = Modifier.padding(
                            horizontal = Dimens.PaddingExtraLarge,
                            vertical = Dimens.PaddingSmall
                        )
                    )
                }

                // Expense items with swipe to delete
                itemsIndexed(
                    items = expenses,
                    key = { _, expense -> expense.id }
                ) { _, expense ->
                    val deletedMessage = stringResource(R.string.snackbar_expense_deleted)
                    val undoLabel = stringResource(R.string.snackbar_undo)

                    SwipeToDeleteExpenseItem(
                        expense = expense,
                        onDelete = { deletedExpense ->
                            viewModel.deleteExpense(deletedExpense)
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = deletedMessage,
                                    actionLabel = undoLabel,
                                    duration = SnackbarDuration.Short
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.undoDelete(deletedExpense)
                                }
                            }
                        },
                        onItemClick = {
                            onNavigateToCategory(
                                expense.category,
                                expense.categoryEmoji
                            )
                        },
                        modifier = Modifier.padding(
                            horizontal = Dimens.PaddingExtraLarge,
                            vertical = Dimens.PaddingExtraSmall
                        )
                    )
                }

                // Spacer between months
                item(key = "spacer_$month") {
                    Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                }
            }
        }
    }
}

// ---- Month section header ----
@Composable
private fun MonthHeader(
    month: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = month.uppercase(),
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.08.sp,
            fontFamily = com.sonalisulgadle.expensetracker.ui.theme.DmMono
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        modifier = modifier
    )
}
