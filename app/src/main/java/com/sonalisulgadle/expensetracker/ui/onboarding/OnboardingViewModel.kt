package com.sonalisulgadle.expensetracker.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonalisulgadle.expensetracker.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val state: StateFlow<OnboardingState> =
        userPreferencesRepository.userName
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
            userPreferencesRepository.saveUserName(name)
            isSaving.value = false
        }
    }
}
