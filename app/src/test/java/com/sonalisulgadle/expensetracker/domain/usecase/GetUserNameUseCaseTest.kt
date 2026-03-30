package com.sonalisulgadle.expensetracker.domain.usecase

import app.cash.turbine.test
import com.sonalisulgadle.expensetracker.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@DisplayName("GetUserNameUseCase")
class GetUserNameUseCaseTest {

    private val repository: UserPreferencesRepository = mock()
    private lateinit var useCase: GetUserNameUseCase

    @BeforeEach
    fun setup() {
        useCase = GetUserNameUseCase(repository)
    }

    @Nested
    @DisplayName("Name retrieval")
    inner class NameRetrieval {

        @Test
        @DisplayName("returns name from repository")
        fun returnsNameFromRepository() = runTest {
            whenever(repository.userName).thenReturn(flowOf("Sonali"))

            useCase().test {
                assertEquals("Sonali", awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        @DisplayName("returns empty string when no name set")
        fun returnsEmptyWhenNoName() = runTest {
            whenever(repository.userName).thenReturn(flowOf(""))

            useCase().test {
                assertEquals("", awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        @DisplayName("emits updated name when repository changes")
        fun emitsUpdatedName() = runTest {
            val names = listOf("", "Sonali")
            whenever(repository.userName).thenReturn(
                kotlinx.coroutines.flow.flow {
                    names.forEach { emit(it) }
                }
            )

            useCase().test {
                assertEquals("", awaitItem())
                assertEquals("Sonali", awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}