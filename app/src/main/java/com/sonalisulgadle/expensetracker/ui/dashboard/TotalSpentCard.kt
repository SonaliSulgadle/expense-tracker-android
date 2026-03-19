package com.sonalisulgadle.expensetracker.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonalisulgadle.expensetracker.R
import com.sonalisulgadle.expensetracker.ui.theme.AmberPrimary
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingExtraLarge
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingExtraSmall
import com.sonalisulgadle.expensetracker.ui.theme.DmMono
import com.sonalisulgadle.expensetracker.util.FormatUtils

@Composable
fun TotalSpentCard(
    totalSpent: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(PaddingExtraLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.TopEnd)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                AmberPrimary.copy(alpha = 0.08f),
                                Color.Transparent
                            )
                        )
                    )
            )
            Column(modifier = Modifier.padding(PaddingExtraLarge)) {
                Text(
                    text = stringResource(R.string.total_spent),
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 0.08.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = stringResource(R.string.symbol_currency_won),
                        style = MaterialTheme.typography.titleMedium,
                        color = AmberPrimary,
                        modifier = Modifier.padding(bottom = PaddingExtraSmall)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = FormatUtils.formatAmount(totalSpent),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontFamily = DmMono,
                            letterSpacing = (-1).sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(PaddingExtraSmall))
                Text(
                    text = stringResource(R.string.this_month),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}