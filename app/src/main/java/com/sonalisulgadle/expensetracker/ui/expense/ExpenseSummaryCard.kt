package com.sonalisulgadle.expensetracker.ui.expense

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonalisulgadle.expensetracker.R
import com.sonalisulgadle.expensetracker.ui.theme.AmberPrimary
import com.sonalisulgadle.expensetracker.ui.theme.Dimens
import com.sonalisulgadle.expensetracker.ui.theme.MonoTextStyle
import com.sonalisulgadle.expensetracker.util.FormatUtils

@Composable
fun ExpenseSummaryCard(
    totalSpent: Double,
    expenseCount: Int,
    avgPerDay: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.RadiusCard),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(Dimens.CardBorderWidth, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.PaddingLarge),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SummaryItem(
                label = stringResource(R.string.total_spent),
                value = "₩${FormatUtils.formatAmount(totalSpent)}",
                valueColor = AmberPrimary,
                modifier = Modifier.weight(1f)
            )
            SummaryDivider()
            SummaryItem(
                label = stringResource(R.string.total_expenses),
                value = expenseCount.toString(),
                modifier = Modifier.weight(1f)
            )
            SummaryDivider()
            SummaryItem(
                label = stringResource(R.string.avg_per_day),
                value = "₩${FormatUtils.formatAmount(avgPerDay)}",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SummaryItem(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color =
        MaterialTheme.colorScheme.onBackground,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingExtraSmall)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MonoTextStyle.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            ),
            color = valueColor
        )
    }
}

@Composable
private fun SummaryDivider() {
    Box(
        modifier = Modifier
            .width(Dimens.CardBorderWidth)
            .height(32.dp)
            .padding(horizontal = Dimens.PaddingExtraSmall)
    ) {
        VerticalDivider(color = MaterialTheme.colorScheme.outline)
    }
}