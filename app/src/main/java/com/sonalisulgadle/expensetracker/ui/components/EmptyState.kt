package com.sonalisulgadle.expensetracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonalisulgadle.expensetracker.R
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingExtraSmall
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingMedium

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.empty_icon_text), fontSize = 48.sp)
        Spacer(modifier = Modifier.height(PaddingMedium))
        Text(
            text = stringResource(R.string.no_expenses_yet),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(PaddingExtraSmall))
        Text(
            text = stringResource(R.string.add_first_expense),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}