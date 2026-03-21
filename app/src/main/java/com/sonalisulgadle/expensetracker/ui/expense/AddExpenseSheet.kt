package com.sonalisulgadle.expensetracker.ui.expense

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sonalisulgadle.expensetracker.R
import com.sonalisulgadle.expensetracker.ui.theme.AmberGradientEnd
import com.sonalisulgadle.expensetracker.ui.theme.AmberPrimary
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingExtraLarge
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingExtraSmall
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingLarge
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingSection
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingSmall
import com.sonalisulgadle.expensetracker.ui.theme.OnPrimaryDark

@Composable
fun AddExpenseSheet(
    uiState: ExpenseUiState,
    onAddExpense: (String, Double) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberAddExpenseSheetState()

    val isLoading = uiState is ExpenseUiState.Categorizing
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(uiState) {
        if (uiState is ExpenseUiState.Success) onDismiss()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingSection, vertical = PaddingSmall)
            .padding(bottom = 32.dp)
    ) {
        // Handle bar
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.outline)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(PaddingExtraLarge))

        // Title
        Text(
            text = stringResource(R.string.add_expense),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(PaddingExtraSmall))
        Text(
            text = stringResource(R.string.add_expense_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(PaddingSection))

        // Fields — extracted to AddExpenseFields
        AddExpenseFields(state = sheetState)
        Spacer(modifier = Modifier.height(PaddingLarge))

        // AI state — extracted to AiStateIndicator
        AnimatedContent(
            targetState = uiState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = stringResource(R.string.label_ai_state_indicator)
        ) { state ->
            when (state) {
                is ExpenseUiState.Categorizing -> {
                    AiCategorizingIndicator()
                    Spacer(modifier = Modifier.height(PaddingLarge))
                }

                is ExpenseUiState.Error -> {
                    ErrorMessage(message = stringResource(state.messageRes))
                    Spacer(modifier = Modifier.height(PaddingLarge))
                }

                else -> Spacer(modifier = Modifier.height(0.dp))
            }
        }


        // Submit button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(PaddingLarge))
                .background(
                    Brush.linearGradient(
                        colors = if (isLoading) listOf(
                            AmberPrimary.copy(alpha = 0.5f),
                            AmberGradientEnd.copy(alpha = 0.5f)
                        ) else listOf(AmberPrimary, AmberGradientEnd)
                    )
                )
                .clickable(enabled = !isLoading) {
                    keyboardController?.hide()
                    if (sheetState.validate()) {
                        sheetState.parsedAmount()?.let {
                            onAddExpense(sheetState.description, it)
                        }
                    }
                }
                .padding(PaddingLarge),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = OnPrimaryDark,
                    modifier = Modifier.size(PaddingExtraLarge),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = stringResource(R.string.add_expense),
                    style = MaterialTheme.typography.titleMedium,
                    color = OnPrimaryDark,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}