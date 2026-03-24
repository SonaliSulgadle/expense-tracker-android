package com.sonalisulgadle.expensetracker.ui.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonalisulgadle.expensetracker.domain.model.Expense
import com.sonalisulgadle.expensetracker.domain.usecase.GetExpensesByCategoryUseCase
import com.sonalisulgadle.expensetracker.domain.usecase.GetExpensesUseCase
import com.sonalisulgadle.expensetracker.util.Constants
import com.sonalisulgadle.expensetracker.util.Constants.DEFAULT_EMOJI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.math.RoundingMode
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

private const val MAX_TOP_ITEMS = 5

@HiltViewModel
class CategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getExpensesByCategory: GetExpensesByCategoryUseCase,
    private val getAllExpenses: GetExpensesUseCase
) : ViewModel() {

    private val categoryName: String =
        savedStateHandle.get<String>(Constants.NAV_ARG_CATEGORY_NAME)
            ?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
            .orEmpty()

    private val categoryEmoji: String =
        savedStateHandle.get<String>(Constants.NAV_ARG_CATEGORY_EMOJI)
            ?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
            .orEmpty()

    val uiState: StateFlow<CategoryDetailUiState> = combine(
        getExpensesByCategory(categoryName),
        getAllExpenses()
    ) { categoryExpenses, allExpenses ->

        val totalSpent = categoryExpenses.sumOf { it.amount }
        val totalAllExpenses = allExpenses.sumOf { it.amount }
        val highest = categoryExpenses.maxOfOrNull { it.amount } ?: 0.0
        val emoji = categoryExpenses.firstOrNull()?.categoryEmoji ?: DEFAULT_EMOJI

        CategoryDetailUiState(
            categoryName = categoryName,
            categoryEmoji = categoryEmoji,
            totalSpent = totalSpent,
            expenseCount = categoryExpenses.size,
            averageExpense = if (categoryExpenses.isEmpty()) 0.0
            else totalSpent / categoryExpenses.size,
            highestExpense = highest,
            percentageOfTotal = getPercentageOfTotal(totalAllExpenses, totalSpent),
            topItems = buildTopItems(categoryExpenses, highest),
            expenses = categoryExpenses,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(Constants.FLOW_STOP_TIMEOUT_MS),
        initialValue = CategoryDetailUiState(isLoading = true)
    )

    private fun getPercentageOfTotal(totalAllExpenses: Double, totalSpent: Double): Double =
        if (totalAllExpenses == 0.0) 0.0
        else ((totalSpent / totalAllExpenses) * 100)
            .toBigDecimal()
            .setScale(1, RoundingMode.HALF_UP)
            .toDouble()

    private fun buildTopItems(
        expenses: List<Expense>,
        highest: Double
    ): List<TopItem> {
        if (highest == 0.0) return emptyList()
        return expenses
            .sortedByDescending { it.amount }
            .take(MAX_TOP_ITEMS)
            .map { expense ->
                TopItem(
                    description = expense.description,
                    amount = expense.amount,
                    percentage = (expense.amount / highest).toFloat()
                )
            }
    }
}