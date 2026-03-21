package com.sonalisulgadle.expensetracker.ui.expense

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sonalisulgadle.expensetracker.R
import com.sonalisulgadle.expensetracker.domain.model.Expense
import com.sonalisulgadle.expensetracker.ui.components.EmptyState
import com.sonalisulgadle.expensetracker.ui.dashboard.CategoryChipsRow
import com.sonalisulgadle.expensetracker.ui.theme.Dimens
import com.sonalisulgadle.expensetracker.ui.theme.MonoTextStyle
import com.sonalisulgadle.expensetracker.util.DateUtils
import kotlinx.coroutines.launch

private const val SWIPE_DISMISS_THRESHOLD = 0.4f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    onBackClick: () -> Unit,
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
                    categories = uiState.categoryTotals.map { it.category },
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

// ---- Swipe to delete wrapper ----
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDeleteExpenseItem(
    expense: Expense,
    onDelete: (Expense) -> Unit,
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
        ExpenseItemWithDate(expense = expense)
    }
}

// ---- Red delete background revealed on swipe ----
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteBackground(
    targetValue: SwipeToDismissBoxValue
) {
    val isActive = targetValue == SwipeToDismissBoxValue.EndToStart

    val backgroundColor by animateColorAsState(
        targetValue = if (isActive) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
        animationSpec = tween(durationMillis = 300),
        label = "delete_bg_color"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(Dimens.RadiusLarge))
            .background(backgroundColor),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(R.string.delete_expense),
            tint = Color.White,
            modifier = Modifier
                .padding(end = Dimens.PaddingExtraLarge)
                .size(Dimens.IconLarge)
        )
    }
}

// ---- ExpenseItem extended with date ----
@Composable
private fun ExpenseItemWithDate(
    expense: Expense,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        ExpenseItem(
            expense = expense,
            onClick = {},
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
