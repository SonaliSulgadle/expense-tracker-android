package com.sonalisulgadle.expensetracker.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.sp
import com.sonalisulgadle.expensetracker.R
import com.sonalisulgadle.expensetracker.ui.theme.AmberGradientEnd
import com.sonalisulgadle.expensetracker.ui.theme.AmberPrimary
import com.sonalisulgadle.expensetracker.ui.theme.Dimens
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingSection
import com.sonalisulgadle.expensetracker.ui.theme.OnPrimaryDark

@Composable
fun OnboardingScreen(
    onNameSaved: (String) -> Unit,
    isSaving: Boolean = false
) {
    var name by remember { mutableStateOf("") }
    val isValid = name.isNotBlank()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingSection),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "💸",
                fontSize = 64.sp
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

            Text(
                text = stringResource(R.string.onboarding_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingSmall))

            Text(
                text = stringResource(R.string.onboarding_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(PaddingSection))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = {
                    Text(stringResource(R.string.onboarding_name_hint))
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.onboarding_name_placeholder),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                            .copy(alpha = 0.5f)
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (isValid) onNameSaved(name.trim())
                    }
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(Dimens.RadiusLarge),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmberPrimary,
                    focusedLabelColor = AmberPrimary,
                    cursorColor = AmberPrimary
                )
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Dimens.RadiusExtraLarge))
                    .background(
                        Brush.linearGradient(
                            colors = if (isValid)
                                listOf(AmberPrimary, AmberGradientEnd)
                            else
                                listOf(
                                    AmberPrimary.copy(alpha = 0.4f),
                                    AmberGradientEnd.copy(alpha = 0.4f)
                                )
                        )
                    )
                    .clickable(enabled = isValid && !isSaving) {
                        onNameSaved(name.trim())
                    }
                    .padding(Dimens.PaddingLarge),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isSaving)
                        stringResource(R.string.onboarding_saving)
                    else
                        stringResource(R.string.onboarding_continue),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isValid) OnPrimaryDark
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}