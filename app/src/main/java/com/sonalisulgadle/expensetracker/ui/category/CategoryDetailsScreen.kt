package com.sonalisulgadle.expensetracker.ui.category

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sonalisulgadle.expensetracker.ui.theme.AmberPrimary
import com.sonalisulgadle.expensetracker.ui.theme.Dimens

@Composable
fun CategoryDetailScreen(
    onBackClick: () -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AmberPrimary)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                horizontal = Dimens.PaddingExtraLarge,
                vertical = Dimens.PaddingExtraLarge
            ),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement
                .spacedBy(Dimens.PaddingSmall)
        ) {

            // ---- Header ----
            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(400)) +
                            slideInVertically(tween(400)) { -20 }
                ) {
                    CategoryDetailHeader(
                        onBackClick = onBackClick
                    )
                }
            }

            // ---- Hero card ----
            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(500)) +
                            slideInVertically(tween(500)) { 40 }
                ) {
                    CategoryHeroCard(
                        categoryName = uiState.categoryName,
                        categoryEmoji = uiState.categoryEmoji,
                        totalSpent = uiState.totalSpent,
                        expenseCount = uiState.expenseCount
                    )
                }
            }

            // ---- Stats row ----
            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(600))
                ) {
                    CategoryStatsRow(
                        averageExpense = uiState.averageExpense,
                        highestExpense = uiState.highestExpense,
                        percentageOfTotal = uiState.percentageOfTotal
                    )
                }
            }

            // TODO - Chart and expense list
        }
    }
}