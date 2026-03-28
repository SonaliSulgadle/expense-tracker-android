package com.sonalisulgadle.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sonalisulgadle.expensetracker.domain.repository.UserPreferencesRepository
import com.sonalisulgadle.expensetracker.ui.navigation.AppNavGraph
import com.sonalisulgadle.expensetracker.ui.onboarding.OnboardingScreen
import com.sonalisulgadle.expensetracker.ui.onboarding.OnboardingState
import com.sonalisulgadle.expensetracker.ui.onboarding.OnboardingViewModel
import com.sonalisulgadle.expensetracker.ui.theme.ExpenseTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerTheme {
                val onboardingViewModel: OnboardingViewModel = hiltViewModel()
                val state by onboardingViewModel.state
                    .collectAsStateWithLifecycle()
                val isSaving by onboardingViewModel.isSaving
                    .collectAsStateWithLifecycle()

                when (state) {
                    OnboardingState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                        )
                    }

                    OnboardingState.ShowOnboarding -> {
                        OnboardingScreen(
                            onNameSaved = { name ->
                                onboardingViewModel.saveUserName(name)
                            },
                            isSaving = isSaving
                        )
                    }

                    OnboardingState.ShowDashboard -> {
                        AppNavGraph()
                    }
                }
            }
        }
    }
}