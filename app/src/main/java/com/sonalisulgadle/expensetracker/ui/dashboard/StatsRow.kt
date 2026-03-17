package com.sonalisulgadle.expensetracker.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonalisulgadle.expensetracker.R
import com.sonalisulgadle.expensetracker.ui.theme.AmberPrimary
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingMedium
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.RadiusLarge
import com.sonalisulgadle.expensetracker.ui.theme.MonoTextStyle
import com.sonalisulgadle.expensetracker.util.formatAmount
import com.sonalisulgadle.expensetracker.util.getDaysOfMonth

data class StatCardData(
    val label: String,
    val value: String,
    val valueColor: Color? = null  // null = use default onBackground
)

@Composable
fun StatsRow(
    totalSpent: Double,
    expenseCount: Int,
    categoryCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatCard(
            label = stringResource(R.string.total_expenses),
            value = expenseCount.toString(),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            label = stringResource(R.string.avg_per_day),
            value = "₩${formatAmount(totalSpent / getDaysOfMonth())}",
            valueColor = AmberPrimary,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            label = stringResource(R.string.categories),
            value = categoryCount.toString(),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(RadiusLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(PaddingMedium)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                style = MonoTextStyle.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = valueColor
            )
        }
    }
}