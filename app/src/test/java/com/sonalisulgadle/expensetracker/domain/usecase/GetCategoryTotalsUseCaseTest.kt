package com.sonalisulgadle.expensetracker.domain.usecase

import app.cash.turbine.test
import com.sonalisulgadle.expensetracker.data.local.CategoryTotal
import com.sonalisulgadle.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@DisplayName("GetCategoryTotalsUseCase")
class GetCategoryTotalsUseCaseTest {

    private val repository: ExpenseRepository = mock()
    private lateinit var useCase: GetCategoryTotalsUseCase

    private val fakeTotals = listOf(
        CategoryTotal("Food & Drink", "🍔", 124500.0),
        CategoryTotal("Transport", "🚕", 45000.0)
    )

    @BeforeEach
    fun setup() {
        useCase = GetCategoryTotalsUseCase(repository)
    }

    @Nested
    @DisplayName("Category totals retrieval")
    inner class CategoryTotalsRetrieval {

        @Test
        @DisplayName("returns category totals from repository")
        fun returnsCategoryTotals() = runTest {
            whenever(repository.getCategoryTotals())
                .thenReturn(flowOf(fakeTotals))

            useCase().test {
                val items = awaitItem()
                assertEquals(2, items.size)
                assertEquals("Food & Drink", items[0].category)
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        @DisplayName("returns empty list when no expenses")
        fun returnsEmptyList() = runTest {
            whenever(repository.getCategoryTotals())
                .thenReturn(flowOf(emptyList()))

            useCase().test {
                assertTrue(awaitItem().isEmpty())
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        @DisplayName("delegates to repository")
        fun delegatesToRepository() = runTest {
            whenever(repository.getCategoryTotals())
                .thenReturn(flowOf(emptyList()))

            useCase()
            verify(repository).getCategoryTotals()
        }
    }
}