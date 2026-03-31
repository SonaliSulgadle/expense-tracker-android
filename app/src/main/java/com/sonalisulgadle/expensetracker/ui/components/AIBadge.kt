package com.sonalisulgadle.expensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonalisulgadle.expensetracker.R
import com.sonalisulgadle.expensetracker.ui.theme.AmberPrimary
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingExtraSmall
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingSmall
import com.sonalisulgadle.expensetracker.ui.theme.DmMono
import com.sonalisulgadle.expensetracker.ui.theme.ExpenseTrackerTheme

@Composable
fun AiBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(PaddingExtraSmall))
            .background(AmberPrimary.copy(alpha = 0.12f))
            .padding(horizontal = PaddingSmall, vertical = 1.dp)
            .semantics {
                contentDescription = ""
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.label_ai_badge),
            fontFamily = DmMono,
            fontSize = 9.sp,
            lineHeight = 9.sp,
            fontWeight = FontWeight.Medium,
            color = AmberPrimary
        )
    }
}

@Composable
@PreviewLightDark
fun PreviewAiBadge() {
    ExpenseTrackerTheme {
        AiBadge()
    }
}