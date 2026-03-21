package com.sonalisulgadle.expensetracker.ui.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonalisulgadle.expensetracker.R
import com.sonalisulgadle.expensetracker.domain.model.Expense
import com.sonalisulgadle.expensetracker.domain.model.ExpenseError
import com.sonalisulgadle.expensetracker.domain.usecase.AddExpenseUseCase
import com.sonalisulgadle.expensetracker.domain.usecase.DeleteExpenseUseCase
import com.sonalisulgadle.expensetracker.domain.usecase.GetCategoryTotalsUseCase
import com.sonalisulgadle.expensetracker.domain.usecase.GetExpensesUseCase
import com.sonalisulgadle.expensetracker.domain.usecase.RestoreExpenseUseCase
import com.sonalisulgadle.expensetracker.util.Constants
import com.sonalisulgadle.expensetracker.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val addExpenseUseCase: AddExpenseUseCase,
    private val getExpensesUseCase: GetExpensesUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    private val getCategoryTotalsUseCase: GetCategoryTotalsUseCase,
    private val restoreExpenseUseCase: RestoreExpenseUseCase
) : ViewModel() {

    private var lastDeletedExpense: Expense? = null

    // UI state for add expense action (Idle, Categorizing, Success, Error)
    private val _addExpenseState = MutableStateFlow<ExpenseUiState>(ExpenseUiState.Idle)
    val addExpenseState: StateFlow<ExpenseUiState> = _addExpenseState.asStateFlow()

    // Combined UI state for the expense list screen
    val expenseListState: StateFlow<ExpenseListUiState> =
        combine(
            getExpensesUseCase(),
            getCategoryTotalsUseCase()
        ) { expenses, categoryTotals ->
            val total = expenses.sumOf { it.amount }
            ExpenseListUiState(
                expenses = expenses,
                groupedExpenses = groupExpensesByMonth(expenses),
                categoryTotals = categoryTotals,
                totalSpent = total,
                avgPerDay = total / DateUtils.getDaysOfMonth(),
                currentMonth = DateUtils.formatCurrentMonth(),
                isLoading = false
            )
        }.catch { e ->
            Timber.e(e, "Error loading expenses")
            emit(ExpenseListUiState(error = R.string.error_loading_expenses))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Constants.FLOW_STOP_TIMEOUT_MS),
            initialValue = ExpenseListUiState(isLoading = true)
        )

    private fun groupExpensesByMonth(expenses: List<Expense>): Map<String, List<Expense>> {
        return expenses.groupBy { DateUtils.formatMonthYear(it.timestamp) }
    }

    fun addExpense(description: String, amount: Double) {
        viewModelScope.launch {
            _addExpenseState.value = ExpenseUiState.Categorizing

            val result = addExpenseUseCase(description, amount)

            result.onSuccess { expense ->
                _addExpenseState.value = ExpenseUiState.Success(expense)
                resetAddExpenseState()
            }

            result.onFailure { error ->
                val message = when (error) {
                    is ExpenseError.EmptyDescription -> R.string.error_empty_description
                    is ExpenseError.InvalidAmount -> R.string.error_invalid_amount
                    is ExpenseError.AiFailed -> R.string.error_ai_failed
                    is ExpenseError.DatabaseError -> R.string.error_database
                    else -> R.string.error_unknown
                }
                _addExpenseState.value = ExpenseUiState.Error(message)
            }
        }
    }

    fun deleteExpense(expense: Expense) {
        lastDeletedExpense = expense
        viewModelScope.launch {
            deleteExpenseUseCase(expense)
        }
    }

    fun undoDelete(expense: Expense) {
        viewModelScope.launch {
            restoreExpenseUseCase(expense)
        }
    }

    private fun resetAddExpenseState() {
        viewModelScope.launch {
            // Small delay so UI can show success briefly before resetting
            kotlinx.coroutines.delay(Constants.AI_RESET_DELAY_MS)
            _addExpenseState.value = ExpenseUiState.Idle
        }
    }

    fun clearError() {
        _addExpenseState.value = ExpenseUiState.Idle
    }
}