package com.sonalisulgadle.expensetracker.ui.onboarding

sealed class OnboardingState {
    object Loading : OnboardingState()
    object ShowOnboarding : OnboardingState()
    object ShowDashboard : OnboardingState()
}