package com.sonalisulgadle.expensetracker.ui.category

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sonalisulgadle.expensetracker.R
import com.sonalisulgadle.expensetracker.ui.theme.AmberPrimary
import com.sonalisulgadle.expensetracker.ui.theme.Dimens
import com.sonalisulgadle.expensetracker.ui.theme.MonoTextStyle
import com.sonalisulgadle.expensetracker.util.FormatUtils

data class CategoryStatItem(
    val label: String,
    val value: String,
    val isAccented: Boolean = false
)

@Composable
fun CategoryStatsRow(
    averageExpense: Double,
    highestExpense: Double,
    percentageOfTotal: Double,
    modifier: Modifier = Modifier
) {
    val stats = listOf(
        CategoryStatItem(
            label = stringResource(R.string.stat_avg_expense),
            value = "₩${FormatUtils.formatAmount(averageExpense)}",
            isAccented = true
        ),
        CategoryStatItem(
            label = stringResource(R.string.stat_highest),
            value = "₩${FormatUtils.formatAmount(highestExpense)}"
        ),
        CategoryStatItem(
            label = stringResource(R.string.stat_of_total),
            value = "${FormatUtils.formatPercentage(percentageOfTotal)}%"
        )
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)
    ) {
        stats.forEach { stat ->
            CategoryStatCard(
                stat = stat,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CategoryStatCard(
    stat: CategoryStatItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.RadiusLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            Dimens.CardBorderWidth,
            MaterialTheme.colorScheme.outline
        )
    ) {
        Column(
            modifier = Modifier.padding(Dimens.PaddingMedium),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingExtraSmall)
        ) {
            Text(
                text = stat.label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stat.value,
                style = MonoTextStyle.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = if (stat.isAccented) AmberPrimary
                else MaterialTheme.colorScheme.onBackground
            )
        }
    }
}