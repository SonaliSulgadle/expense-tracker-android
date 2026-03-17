package com.sonalisulgadle.expensetracker.ui.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonalisulgadle.expensetracker.R
import com.sonalisulgadle.expensetracker.ui.theme.AmberPrimary
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingLarge
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingMedium

@Composable
fun AiCategorizingIndicator(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(PaddingMedium))
            .background(AmberPrimary.copy(alpha = 0.08f))
            .padding(PaddingMedium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CircularProgressIndicator(
            color = AmberPrimary,
            modifier = Modifier.size(PaddingLarge),
            strokeWidth = 2.dp
        )
        androidx.compose.foundation.layout.Column {
            Text(
                text = stringResource(R.string.ai_categorizing),
                style = MaterialTheme.typography.labelMedium,
                color = AmberPrimary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = stringResource(R.string.ai_categorizing_sub),
                style = MaterialTheme.typography.labelSmall,
                color = AmberPrimary.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(PaddingMedium))
            .background(MaterialTheme.colorScheme.error.copy(alpha = 0.08f))
            .padding(PaddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "⚠️", fontSize = 14.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}