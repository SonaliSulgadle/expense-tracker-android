package com.sonalisulgadle.expensetracker.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sonalisulgadle.expensetracker.ui.components.EmptyState
import com.sonalisulgadle.expensetracker.ui.expense.AddExpenseSheet
import com.sonalisulgadle.expensetracker.ui.expense.ExpenseItem
import com.sonalisulgadle.expensetracker.ui.expense.ExpenseViewModel
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingExtraLarge
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingExtraSmall
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingSection
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingSmall
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.RadiusSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToExpenseList: () -> Unit,
    onNavigateToCategory: (String) -> Unit,
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    val uiState by viewModel.expenseListState.collectAsStateWithLifecycle()
    val addExpenseState by viewModel.addExpenseState.collectAsStateWithLifecycle()
    var showAddSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { -20 }
                ) {
                    DashboardHeader(
                        uiState = uiState,
                        modifier = Modifier.padding(
                            start = PaddingExtraLarge, end = PaddingExtraLarge,
                            top = PaddingExtraLarge, bottom = PaddingSmall
                        )
                    )
                }
            }

            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { 40 }
                ) {
                    TotalSpentCard(
                        totalSpent = uiState.totalSpent,
                        modifier = Modifier.padding(
                            horizontal = PaddingExtraLarge, vertical = PaddingSmall
                        )
                    )
                }
            }

            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(600))
                ) {
                    StatsRow(
                        totalSpent = uiState.totalSpent,
                        expenseCount = uiState.expenses.size,
                        categoryCount = uiState.categoryTotals.size,
                        modifier = Modifier.padding(
                            horizontal = PaddingExtraLarge, vertical = PaddingSmall
                        )
                    )
                }
            }

            item {
                CategoryChipsRow(
                    categories = uiState.categoryTotals.map { it.category },
                    modifier = Modifier.padding(vertical = PaddingSmall)
                )
            }

            item {
                RecentExpensesHeader(
                    onSeeAllClick = onNavigateToExpenseList
                )
            }

            if (uiState.expenses.isEmpty()) {
                item { EmptyState() }
            } else {
                itemsIndexed(
                    items = uiState.expenses.take(10),
                    key = { _, expense -> expense.id }
                ) { index, expense ->
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(300 + index * 80)) +
                                slideInVertically(tween(300 + index * 80)) { 30 }
                    ) {
                        ExpenseItem(
                            expense = expense,
                            onClick = { onNavigateToCategory(expense.category) },
                            modifier = Modifier.padding(
                                horizontal = PaddingExtraLarge, vertical = PaddingExtraSmall
                            )
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            AddExpenseFab(
                onClick = { showAddSheet = true },
                modifier = Modifier.padding(
                    horizontal = PaddingExtraLarge, vertical = PaddingSection
                )
            )
        }
    }

    if (showAddSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = RadiusSheet, topEnd = RadiusSheet)
        ) {
            AddExpenseSheet(
                uiState = addExpenseState,
                onAddExpense = { description, amount ->
                    viewModel.addExpense(description, amount)
                },
                onDismiss = { showAddSheet = false }
            )
        }
    }
}
