package com.sonalisulgadle.expensetracker.ui.expense

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.sonalisulgadle.expensetracker.R
import com.sonalisulgadle.expensetracker.ui.theme.AmberPrimary
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingMedium
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.RadiusLarge
import com.sonalisulgadle.expensetracker.ui.theme.MonoTextStyle

@Composable
fun AddExpenseFields(
    state: AddExpenseSheetState,
    modifier: Modifier = Modifier
) {
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = AmberPrimary,
        focusedLabelColor = AmberPrimary,
        cursorColor = AmberPrimary
    )

    Column(modifier = modifier) {
        OutlinedTextField(
            value = state.description,
            onValueChange = state::onDescriptionChange,
            label = { Text(stringResource(R.string.description_hint)) },
            placeholder = {
                Text(
                    text = stringResource(R.string.description_placeholder),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            },
            isError = state.descriptionError != null,
            supportingText = state.descriptionError?.let {
                { Text(stringResource(it), color = MaterialTheme.colorScheme.error) }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(RadiusLarge),
            colors = fieldColors,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(PaddingMedium))
        OutlinedTextField(
            value = state.amount,
            onValueChange = state::onAmountChange,
            label = { Text(stringResource(R.string.amount_hint)) },
            placeholder = {
                Text(
                    text = stringResource(R.string.label_default_amount),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    style = MonoTextStyle
                )
            },
            prefix = {
                Text(
                    text = stringResource(R.string.symbol_currency_won),
                    color = AmberPrimary,
                    style = MonoTextStyle
                )
            },
            isError = state.amountError != null,
            supportingText = state.amountError?.let {
                { Text(stringResource(it), color = MaterialTheme.colorScheme.error) }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(RadiusLarge),
            colors = fieldColors,
            textStyle = MonoTextStyle,
            singleLine = true
        )
    }
}