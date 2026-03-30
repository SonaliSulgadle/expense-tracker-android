package com.sonalisulgadle.expensetracker.domain.usecase

import com.sonalisulgadle.expensetracker.ai.CategoryResult
import com.sonalisulgadle.expensetracker.domain.model.ExpenseError
import com.sonalisulgadle.expensetracker.domain.repository.CategoryRepository
import com.sonalisulgadle.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@DisplayName("AddExpenseUseCase")
class AddExpenseUseCaseTest {

    private val expenseRepository: ExpenseRepository = mock()
    private val categoryRepository: CategoryRepository = mock()
    private lateinit var useCase: AddExpenseUseCase

    private val fakeCategoryResult = CategoryResult(
        category = "Food & Drink",
        emoji = "🍔",
        confidence = 0.95
    )

    @BeforeEach
    fun setup() {
        useCase = AddExpenseUseCase(expenseRepository, categoryRepository)
    }

    @Nested
    @DisplayName("Input validation")
    inner class InputValidation {

        @Test
        @DisplayName("empty description returns EmptyDescription error")
        fun emptyDescriptionReturnsError() = runTest {
            val result = useCase("", 5000.0)
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is ExpenseError.EmptyDescription)
        }

        @Test
        @DisplayName("blank description returns EmptyDescription error")
        fun blankDescriptionReturnsError() = runTest {
            val result = useCase("   ", 5000.0)
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is ExpenseError.EmptyDescription)
        }

        @Test
        @DisplayName("zero amount returns InvalidAmount error")
        fun zeroAmountReturnsError() = runTest {
            val result = useCase("burger", 0.0)
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is ExpenseError.InvalidAmount)
        }

        @Test
        @DisplayName("negative amount returns InvalidAmount error")
        fun negativeAmountReturnsError() = runTest {
            val result = useCase("burger", -100.0)
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is ExpenseError.InvalidAmount)
        }

        @Test
        @DisplayName("empty description does not call Gemini")
        fun emptyDescriptionDoesNotCallGemini() = runTest {
            useCase("", 5000.0)
            verify(categoryRepository, never()).categorize(any())
        }
    }

    @Nested
    @DisplayName("Success path")
    inner class SuccessPath {

        @BeforeEach
        fun setupMocks() = runTest {
            whenever(categoryRepository.categorize(any()))
                .thenReturn(fakeCategoryResult)
            whenever(expenseRepository.addExpense(any()))
                .thenReturn(1L)
        }

        @Test
        fun validInputReturnsSuccess() = runTest {
            val result = useCase("burger", 8500.0)
            assertTrue(result.isSuccess)
        }

        @Test
        fun descriptionIsTrimmed() = runTest {
            val result = useCase("  burger  ", 8500.0)
            assertEquals("burger", result.getOrNull()?.description)
        }

        @Test
        fun categoryFromGeminiIsUsed() = runTest {
            val result = useCase("burger", 8500.0)
            assertEquals("Food & Drink", result.getOrNull()?.category)
        }

        @Test
        fun emojiFromGeminiIsUsed() = runTest {
            val result = useCase("burger", 8500.0)
            assertEquals("🍔", result.getOrNull()?.categoryEmoji)
        }

        @Test
        fun highConfidenceMarksAsAiCategorized() = runTest {
            val result = useCase("burger", 8500.0)
            assertTrue(result.getOrNull()?.isAiCategorized == true)
        }

        @Test
        fun zeroConfidenceNotMarkedAsAiCategorized() = runTest {
            whenever(categoryRepository.categorize(any()))
                .thenReturn(fakeCategoryResult.copy(confidence = 0.0))
            val result = useCase("burger", 8500.0)
            assertTrue(result.getOrNull()?.isAiCategorized == false)
        }

        @Test
        fun timestampIsSet() = runTest {
            val before = System.currentTimeMillis()
            val result = useCase("burger", 8500.0)
            val after = System.currentTimeMillis()
            val timestamp = result.getOrNull()?.timestamp ?: 0L
            assertTrue(timestamp in before..after)
        }
    }

    @Nested
    @DisplayName("Error handling")
    inner class ErrorHandling {

        @Test
        @DisplayName("repository failure returns DatabaseError")
        fun repositoryFailureReturnsDatabaseError() = runTest {
            whenever(categoryRepository.categorize(any()))
                .thenReturn(fakeCategoryResult)
            whenever(expenseRepository.addExpense(any()))
                .thenThrow(RuntimeException("DB error"))

            val result = useCase("burger", 8500.0)
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is ExpenseError.DatabaseError)
        }

        @Test
        @DisplayName("Gemini failure still saves with fallback category")
        fun geminiFailureStillSavesExpense() = runTest {
            whenever(categoryRepository.categorize(any()))
                .thenReturn(CategoryResult("Other", "💸", 0.0))
            whenever(expenseRepository.addExpense(any()))
                .thenReturn(1L)

            val result = useCase("burger", 8500.0)
            assertTrue(result.isSuccess)
        }
    }
}