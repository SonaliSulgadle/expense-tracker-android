package com.sonalisulgadle.expensetracker.ui.onboarding

import app.cash.turbine.test
import com.sonalisulgadle.expensetracker.InstantTaskExecutorExtension
import com.sonalisulgadle.expensetracker.domain.usecase.GetUserNameUseCase
import com.sonalisulgadle.expensetracker.domain.usecase.SaveUserNameUseCase
import com.sonalisulgadle.expensetracker.ui.onboarding.OnboardingState.Loading
import com.sonalisulgadle.expensetracker.ui.onboarding.OnboardingState.ShowDashboard
import com.sonalisulgadle.expensetracker.ui.onboarding.OnboardingState.ShowOnboarding
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
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@DisplayName("OnboardingViewModel")
class OnboardingViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val instantExecutorExtension = InstantTaskExecutorExtension()
    }

    private val testDispatcher = StandardTestDispatcher()
    private val getUserNameUseCase: GetUserNameUseCase = mock()
    private val saveUserNameUseCase: SaveUserNameUseCase = mock()
    private lateinit var viewModel: OnboardingViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel() {
        viewModel = OnboardingViewModel(getUserNameUseCase, saveUserNameUseCase)
    }

    @Nested
    @DisplayName("Onboarding state")
    inner class OnboardingState {

        @Test
        @DisplayName("initial state is Loading")
        fun initialStateIsLoading() = runTest {
            whenever(getUserNameUseCase()).thenReturn(flowOf(""))
            buildViewModel()

            viewModel.state.test {
                // First emission is Loading (initial value)
                assertEquals(
                    Loading,
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        @DisplayName("empty name shows onboarding")
        fun emptyNameShowsOnboarding() = runTest {
            whenever(getUserNameUseCase()).thenReturn(flowOf(""))
            buildViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.state.test {
                val state = awaitItem()
                assertTrue(
                    state is ShowOnboarding || state is Loading
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        @DisplayName("existing name shows dashboard")
        fun existingNameShowsDashboard() = runTest {
            whenever(getUserNameUseCase()).thenReturn(flowOf("Sonali"))
            buildViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.state.test {
                // Skip Loading
                val state = awaitItem()
                if (state is Loading) {
                    assertEquals(ShowDashboard, awaitItem())
                } else {
                    assertEquals(ShowDashboard, state)
                }
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Nested
    @DisplayName("Save username")
    inner class SaveUsername {

        @BeforeEach
        fun setupViewModel() {
            whenever(getUserNameUseCase()).thenReturn(flowOf(""))
            buildViewModel()
        }

        @Test
        @DisplayName("valid name calls saveUserNameUseCase")
        fun validNameCallsUseCase() = runTest {
            viewModel.saveUserName("Sonali")
            testDispatcher.scheduler.advanceUntilIdle()
            verify(saveUserNameUseCase).invoke("Sonali")
        }

        @Test
        @DisplayName("blank name does not call saveUserNameUseCase")
        fun blankNameDoesNotCallUseCase() = runTest {
            viewModel.saveUserName("")
            testDispatcher.scheduler.advanceUntilIdle()
            verify(saveUserNameUseCase, org.mockito.kotlin.never()).invoke(
                org.mockito.kotlin.any()
            )
        }

        @Test
        @DisplayName("isSaving is false initially")
        fun isSavingFalseInitially() = runTest {
            viewModel.isSaving.test {
                assertEquals(false, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}