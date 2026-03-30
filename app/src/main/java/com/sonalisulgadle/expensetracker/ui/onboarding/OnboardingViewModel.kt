package com.sonalisulgadle.expensetracker.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonalisulgadle.expensetracker.domain.usecase.GetUserNameUseCase
import com.sonalisulgadle.expensetracker.domain.usecase.SaveUserNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getUserNameUseCase: GetUserNameUseCase,
    private val saveUserNameUseCase: SaveUserNameUseCase
) : ViewModel() {

    val state: StateFlow<OnboardingState> =
        getUserNameUseCase()
            .map { name ->
                if (name.isBlank()) OnboardingState.ShowOnboarding
                else OnboardingState.ShowDashboard
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = OnboardingState.Loading
            )

    val isSaving = MutableStateFlow(false)

    fun saveUserName(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            isSaving.value = true
            try {
                saveUserNameUseCase(name)
            } catch (e: Exception) {
                Timber.e(e, "Failed to save username")
            } finally {
                isSaving.value = false
            }
        }
    }
}
