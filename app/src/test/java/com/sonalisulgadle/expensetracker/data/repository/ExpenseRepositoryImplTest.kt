package com.sonalisulgadle.expensetracker.data.repository

import app.cash.turbine.test
import com.sonalisulgadle.expensetracker.data.local.CategoryTotal
import com.sonalisulgadle.expensetracker.data.local.ExpenseDao
import com.sonalisulgadle.expensetracker.data.local.ExpenseEntity
import com.sonalisulgadle.expensetracker.domain.model.Expense
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@DisplayName("ExpenseRepositoryImpl")
class ExpenseRepositoryImplTest {

    private val dao: ExpenseDao = mock()
    private lateinit var repository: ExpenseRepositoryImpl

    private val timestamp = System.currentTimeMillis()

    private val fakeEntity = ExpenseEntity(
        id = 1L,
        description = "burger",
        amount = 8500.0,
        category = "Food & Drink",
        categoryEmoji = "🍔",
        timestamp = timestamp,
        isAiCategorized = true
    )

    private val fakeDomain = Expense(
        id = 1L,
        description = "burger",
        amount = 8500.0,
        category = "Food & Drink",
        categoryEmoji = "🍔",
        timestamp = timestamp,
        isAiCategorized = true
    )

    @BeforeEach
    fun setup() {
        repository = ExpenseRepositoryImpl(dao)
    }

    @Nested
    @DisplayName("Get all expenses")
    inner class GetAllExpenses {

        @Test
        @DisplayName("maps entities to domain objects")
        fun mapsEntitiesToDomain() = runTest {
            whenever(dao.getAllExpenses())
                .thenReturn(flowOf(listOf(fakeEntity)))

            repository.getAllExpenses().test {
                val items = awaitItem()
                assertEquals(1, items.size)
                assertEquals("burger", items[0].description)
                assertEquals("Food & Drink", items[0].category)
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        @DisplayName("returns empty list when dao is empty")
        fun returnsEmptyList() = runTest {
            whenever(dao.getAllExpenses())
                .thenReturn(flowOf(emptyList()))

            repository.getAllExpenses().test {
                assertTrue(awaitItem().isEmpty())
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        @DisplayName("maps all entity fields to domain correctly")
        fun mapsAllFields() = runTest {
            whenever(dao.getAllExpenses())
                .thenReturn(flowOf(listOf(fakeEntity)))

            repository.getAllExpenses().test {
                val expense = awaitItem().first()
                assertEquals(fakeDomain.id, expense.id)
                assertEquals(fakeDomain.amount, expense.amount, 0.01)
                assertEquals(fakeDomain.categoryEmoji, expense.categoryEmoji)
                assertEquals(fakeDomain.isAiCategorized, expense.isAiCategorized)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Nested
    @DisplayName("Add expense")
    inner class AddExpense {

        @Test
        @DisplayName("maps domain to entity before inserting")
        fun mapsDomainToEntity() = runTest {
            whenever(dao.insert(any())).thenReturn(1L)

            repository.addExpense(fakeDomain)

            verify(dao).insert(fakeEntity)
        }

        @Test
        @DisplayName("returns id from dao")
        fun returnsIdFromDao() = runTest {
            whenever(dao.insert(any())).thenReturn(42L)

            val result = repository.addExpense(fakeDomain)

            assertEquals(42L, result)
        }
    }

    @Nested
    @DisplayName("Delete expense")
    inner class DeleteExpense {

        @Test
        @DisplayName("maps domain to entity before deleting")
        fun mapsDomainToEntityBeforeDeleting() = runTest {
            repository.deleteExpense(fakeDomain)
            verify(dao).delete(fakeEntity)
        }
    }

    @Nested
    @DisplayName("Category totals")
    inner class CategoryTotals {

        @Test
        @DisplayName("returns category totals from dao")
        fun returnsCategoryTotals() = runTest {
            val fakeTotals = listOf(
                CategoryTotal("Food & Drink", "🍔", 8500.0)
            )
            whenever(dao.getCategoryTotals())
                .thenReturn(flowOf(fakeTotals))

            repository.getCategoryTotals().test {
                val items = awaitItem()
                assertEquals(1, items.size)
                assertEquals("Food & Drink", items[0].category)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}