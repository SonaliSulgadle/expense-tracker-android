package com.sonalisulgadle.expensetracker.ui.category

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sonalisulgadle.expensetracker.R
import com.sonalisulgadle.expensetracker.ui.components.EmptyState
import com.sonalisulgadle.expensetracker.ui.components.SwipeToDeleteExpenseItem
import com.sonalisulgadle.expensetracker.ui.theme.AmberPrimary
import com.sonalisulgadle.expensetracker.ui.theme.Dimens
import kotlinx.coroutines.launch

@Composable
fun CategoryDetailScreen(
    onBackClick: () -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var visible by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val deletedMessage = stringResource(R.string.snackbar_expense_deleted)
    val undoLabel = stringResource(R.string.snackbar_undo)

    LaunchedEffect(Unit) { visible = true }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AmberPrimary)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                horizontal = Dimens.PaddingExtraLarge,
                vertical = Dimens.PaddingExtraLarge
            ),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement
                .spacedBy(Dimens.PaddingSmall)
        ) {

            // ---- Header ----
            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(400)) +
                            slideInVertically(tween(400)) { -20 }
                ) {
                    CategoryDetailHeader(
                        onBackClick = onBackClick
                    )
                }
            }

            // ---- Hero card ----
            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(500)) +
                            slideInVertically(tween(500)) { 40 }
                ) {
                    CategoryHeroCard(
                        categoryName = uiState.categoryName,
                        categoryEmoji = uiState.categoryEmoji,
                        totalSpent = uiState.totalSpent,
                        expenseCount = uiState.expenseCount
                    )
                }
            }

            // ---- Stats row ----
            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(600))
                ) {
                    CategoryStatsRow(
                        averageExpense = uiState.averageExpense,
                        highestExpense = uiState.highestExpense,
                        percentageOfTotal = uiState.percentageOfTotal
                    )
                }
            }

            // ---- Top items section header ----

            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(700))
                ) {
                    CategorySectionHeader(
                        title = stringResource(R.string.top_items),
                        modifier = Modifier.padding(
                            top = Dimens.PaddingSmall
                        )
                    )
                }
            }

            // ---- Top items bar chart ----
            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(800))
                ) {
                    CategoryTopItems(
                        topItems = uiState.topItems
                    )
                }
            }

            // ---- All expenses section header ----
            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(900))
                ) {
                    CategorySectionHeader(
                        title = stringResource(R.string.all_expenses),
                        subtitle = stringResource(
                            R.string.category_expense_count,
                            uiState.expenseCount
                        ),
                        modifier = Modifier.padding(top = Dimens.PaddingSmall)
                    )
                }
            }

            // ---- Empty state ----
            if (uiState.expenses.isEmpty()) {
                item { EmptyState() }
            }

            // ---- Expense list with swipe to delete ----
            itemsIndexed(
                items = uiState.expenses,
                key = { _, expense -> expense.id }
            ) { index, expense ->
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(900 + index * 60))
                ) {
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
                        onItemClick = {},  // no navigation from category detail
                        modifier = Modifier.padding(
                            vertical = Dimens.PaddingExtraSmall
                        )
                    )
                }
            }
        }

    }
}