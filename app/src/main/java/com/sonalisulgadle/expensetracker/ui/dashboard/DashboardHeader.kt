package com.sonalisulgadle.expensetracker.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import com.sonalisulgadle.expensetracker.R
import com.sonalisulgadle.expensetracker.ui.expense.ExpenseListUiState
import com.sonalisulgadle.expensetracker.ui.theme.AmberGradientEnd
import com.sonalisulgadle.expensetracker.ui.theme.AmberPrimary
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.AvatarSize
import com.sonalisulgadle.expensetracker.ui.theme.OnPrimaryDark

@Composable
fun DashboardHeader(
    modifier: Modifier = Modifier,
    userName: String,
    userInitial: String,
    uiState: ExpenseListUiState,
    onAvatarClick: () -> Unit = {},
) {
    val avatarDescription = stringResource(
        R.string.cd_profile_button, userInitial
    )
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = if (userName.isNotBlank())
                    "${stringResource(R.string.greeting)}, $userName"
                else
                    stringResource(R.string.greeting),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = uiState.currentMonth,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Box(
            modifier = Modifier
                .size(AvatarSize)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(AmberPrimary, AmberGradientEnd)
                    )
                )
                .semantics {
                    contentDescription = avatarDescription
                    role = Role.Button
                }
                .clickable(onClick = onAvatarClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = userInitial.ifBlank { "?" },
                style = MaterialTheme.typography.labelMedium,
                color = OnPrimaryDark,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
