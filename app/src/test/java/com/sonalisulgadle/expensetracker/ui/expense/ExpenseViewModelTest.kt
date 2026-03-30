package com.sonalisulgadle.expensetracker.ui.expense

import app.cash.turbine.test
import com.sonalisulgadle.expensetracker.InstantTaskExecutorExtension
import com.sonalisulgadle.expensetracker.data.local.CategoryTotal
import com.sonalisulgadle.expensetracker.domain.model.Expense
import com.sonalisulgadle.expensetracker.domain.model.ExpenseError
import com.sonalisulgadle.expensetracker.domain.usecase.AddExpenseUseCase
import com.sonalisulgadle.expensetracker.domain.usecase.DeleteExpenseUseCase
import com.sonalisulgadle.expensetracker.domain.usecase.GetCategoryTotalsUseCase
import com.sonalisulgadle.expensetracker.domain.usecase.GetExpensesUseCase
import com.sonalisulgadle.expensetracker.domain.usecase.GetUserNameUseCase
import com.sonalisulgadle.expensetracker.domain.usecase.RestoreExpenseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@DisplayName("ExpenseViewModel")
class ExpenseViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val instantExecutorExtension = InstantTaskExecutorExtension()
    }

    private val testDispatcher = StandardTestDispatcher()
    private val addExpenseUseCase: AddExpenseUseCase = mock()
    private val getExpensesUseCase: GetExpensesUseCase = mock()
    private val deleteExpenseUseCase: DeleteExpenseUseCase = mock()
    private val getCategoryTotalsUseCase: GetCategoryTotalsUseCase = mock()
    private val restoreExpenseUseCase: RestoreExpenseUseCase = mock()
    private val getUserNameUseCase: GetUserNameUseCase = mock()

    private lateinit var viewModel: ExpenseViewModel

    private val fakeExpense = Expense(
        id = 1L,
        description = "burger",
        amount = 8500.0,
        category = "Food & Drink",
        categoryEmoji = "🍔",
        timestamp = System.currentTimeMillis(),
        isAiCategorized = true
    )

    private val fakeCategoryTotal = CategoryTotal(
        category = "Food & Drink",
        categoryEmoji = "🍔",
        total = 8500.0
    )

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        whenever(getExpensesUseCase()).thenReturn(flowOf(listOf(fakeExpense)))
        whenever(getCategoryTotalsUseCase()).thenReturn(flowOf(listOf(fakeCategoryTotal)))
        whenever(getUserNameUseCase()).thenReturn(flowOf("Sonali"))
        viewModel = ExpenseViewModel(
            addExpenseUseCase,
            getExpensesUseCase,
            deleteExpenseUseCase,
            getCategoryTotalsUseCase,
            restoreExpenseUseCase,
            getUserNameUseCase
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("Initial state")
    inner class InitialState {

        @Test
        @DisplayName("addExpenseState is Idle")
        fun addExpenseStateIsIdle() = runTest {
            viewModel.addExpenseState.test {
                assertEquals(ExpenseUiState.Idle, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Nested
    @DisplayName("Username in list state")
    inner class UsernameInListState {

        @Test
        @DisplayName("expenseListState contains username from use case")
        fun containsUsername() = runTest {
            testDispatcher.scheduler.advanceUntilIdle()
            viewModel.expenseListState.test {
                val state = awaitItem()
                assertEquals("Sonali", state.userName)
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        @DisplayName("expenseListState contains correct user initial")
        fun containsCorrectInitial() = runTest {
            testDispatcher.scheduler.advanceUntilIdle()
            viewModel.expenseListState.test {
                val state = awaitItem()
                assertEquals("S", state.userInitial)
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        @DisplayName("empty username produces empty initial")
        fun emptyUsernameProducesEmptyInitial() = runTest {
            whenever(getUserNameUseCase()).thenReturn(flowOf(""))
            viewModel = ExpenseViewModel(
                addExpenseUseCase, getExpensesUseCase,
                deleteExpenseUseCase, getCategoryTotalsUseCase,
                restoreExpenseUseCase, getUserNameUseCase
            )
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.expenseListState.test {
                val state = awaitItem()
                assertEquals("", state.userInitial)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Nested
    @DisplayName("Add expense")
    inner class AddExpense {

        @Test
        @DisplayName("transitions through Categorizing to Success")
        fun transitionsThroughCategorizingToSuccess() = runTest {
            whenever(addExpenseUseCase(any(), any()))
                .thenReturn(Result.success(fakeExpense))

            viewModel.addExpenseState.test {
                assertEquals(ExpenseUiState.Idle, awaitItem())
                viewModel.addExpense("burger", 8500.0)
                testDispatcher.scheduler.advanceUntilIdle()
                assertEquals(ExpenseUiState.Categorizing, awaitItem())
                val success = awaitItem()
                assertTrue(success is ExpenseUiState.Success)
                assertEquals(fakeExpense, (success as ExpenseUiState.Success).expense)
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        @DisplayName("transitions to Error on failure")
        fun transitionsToErrorOnFailure() = runTest {
            whenever(addExpenseUseCase(any(), any()))
                .thenReturn(Result.failure(ExpenseError.EmptyDescription()))

            viewModel.addExpenseState.test {
                assertEquals(ExpenseUiState.Idle, awaitItem())
                viewModel.addExpense("", 8500.0)
                testDispatcher.scheduler.advanceUntilIdle()
                awaitItem() // Categorizing
                assertTrue(awaitItem() is ExpenseUiState.Error)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Nested
    @DisplayName("Delete and undo")
    inner class DeleteAndUndo {

        @Test
        @DisplayName("deleteExpense calls DeleteExpenseUseCase")
        fun deleteCallsUseCase() = runTest {
            viewModel.deleteExpense(fakeExpense)
            testDispatcher.scheduler.advanceUntilIdle()
            verify(deleteExpenseUseCase).invoke(fakeExpense)
        }

        @Test
        @DisplayName("undoDelete calls RestoreExpenseUseCase")
        fun undoCallsRestoreUseCase() = runTest {
            viewModel.undoDelete(fakeExpense)
            testDispatcher.scheduler.advanceUntilIdle()
            verify(restoreExpenseUseCase).invoke(fakeExpense)
        }
    }

    @Nested
    @DisplayName("Expense list state")
    inner class ExpenseListState {

        @Test
        @DisplayName("contains expenses from use case")
        fun containsExpenses() = runTest {
            testDispatcher.scheduler.advanceUntilIdle()
            viewModel.expenseListState.test {
                val state = awaitItem()
                assertEquals(1, state.expenses.size)
                assertEquals("burger", state.expenses[0].description)
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        @DisplayName("computes correct total spent")
        fun computesCorrectTotal() = runTest {
            testDispatcher.scheduler.advanceUntilIdle()
            viewModel.expenseListState.test {
                assertEquals(8500.0, awaitItem().totalSpent, 0.01)
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        @DisplayName("groups expenses by month")
        fun groupsExpensesByMonth() = runTest {
            testDispatcher.scheduler.advanceUntilIdle()
            viewModel.expenseListState.test {
                val state = awaitItem()
                assertTrue(state.groupedExpenses.isNotEmpty())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Nested
    @DisplayName("Clear error")
    inner class ClearError {

        @Test
        @DisplayName("clearError resets state to Idle")
        fun clearErrorResetsToIdle() = runTest {
            whenever(addExpenseUseCase(any(), any()))
                .thenReturn(Result.failure(ExpenseError.EmptyDescription()))

            viewModel.addExpense("", 0.0)
            testDispatcher.scheduler.advanceUntilIdle()
            viewModel.clearError()

            viewModel.addExpenseState.test {
                assertEquals(ExpenseUiState.Idle, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}