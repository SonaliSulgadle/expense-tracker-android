package com.sonalisulgadle.expensetracker.ui.category

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonalisulgadle.expensetracker.R
import com.sonalisulgadle.expensetracker.ui.theme.AmberPrimary
import com.sonalisulgadle.expensetracker.ui.theme.Dimens
import com.sonalisulgadle.expensetracker.ui.theme.DmMono
import com.sonalisulgadle.expensetracker.util.FormatUtils

@Composable
fun CategoryHeroCard(
    categoryName: String,
    categoryEmoji: String,
    totalSpent: Double,
    expenseCount: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.RadiusCard),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            Dimens.CardBorderWidth,
            MaterialTheme.colorScheme.outline
        )
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
            Column(
                modifier = Modifier.padding(Dimens.PaddingExtraLarge)
            ) {
                Text(
                    text = categoryEmoji,
                    fontSize = 36.sp
                )
                Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(Dimens.PaddingExtraSmall))
                Text(
                    text = "₩${FormatUtils.formatAmount(totalSpent)}",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontFamily = DmMono,
                        letterSpacing = (-1).sp
                    ),
                    color = AmberPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(Dimens.PaddingExtraSmall))
                Text(
                    text = stringResource(
                        R.string.category_expense_count,
                        expenseCount
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}