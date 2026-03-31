package com.sonalisulgadle.expensetracker.ui.dashboard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonalisulgadle.expensetracker.data.local.CategoryTotal
import com.sonalisulgadle.expensetracker.ui.theme.AmberGradientEnd
import com.sonalisulgadle.expensetracker.ui.theme.AmberPrimary
import com.sonalisulgadle.expensetracker.ui.theme.Dimens
import com.sonalisulgadle.expensetracker.ui.theme.ExpenseTrackerTheme
import com.sonalisulgadle.expensetracker.ui.theme.MonoTextStyle
import com.sonalisulgadle.expensetracker.util.FormatUtils

private const val CHART_BAR_HEIGHT_DP = 160
private const val BAR_ANIMATION_DURATION_MS = 700
private const val BAR_ANIMATION_DELAY_MS = 80
private const val BAR_CORNER_RADIUS_DP = 6f
private const val MAX_CATEGORIES_SHOWN = 6
private val BAR_MAX_WIDTH_DP = 48.dp
private val BAR_MIN_WIDTH_DP = 24.dp

@Composable
fun SpendingBarChart(
    categoryTotals: List<CategoryTotal>,
    modifier: Modifier = Modifier
) {
    if (categoryTotals.isEmpty()) return

    // Take top N categories by total spend
    val displayTotals = categoryTotals
        .sortedByDescending { it.total }
        .take(MAX_CATEGORIES_SHOWN)

    val maxTotal = displayTotals.maxOf { it.total }

    // Calculate bar width dynamically based on count
    val barWidth = when (displayTotals.size) {
        1 -> BAR_MAX_WIDTH_DP
        2 -> 72.dp
        3 -> 56.dp
        4 -> BAR_MAX_WIDTH_DP
        5 -> 40.dp
        else -> BAR_MIN_WIDTH_DP
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = buildString {
                    append("Spending by category chart. ")
                    categoryTotals.forEach { total ->
                        append("${total.category}: ₩${FormatUtils.formatAmount(total.total)}. ")
                    }
                }
            },
        shape = RoundedCornerShape(Dimens.RadiusCard),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            Dimens.CardBorderWidth,
            MaterialTheme.colorScheme.outline
        )
    ) {
        Column(
            modifier = Modifier.padding(Dimens.PaddingLarge)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = Dimens.PaddingSmall,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.Bottom
            ) {
                displayTotals.forEachIndexed { index, categoryTotal ->
                    ChartBar(
                        categoryTotal = categoryTotal,
                        maxTotal = maxTotal,
                        barWidth = barWidth,
                        chartHeight = CHART_BAR_HEIGHT_DP.dp,
                        animationDelay = index * BAR_ANIMATION_DELAY_MS
                    )
                }
            }

            // Baseline
            Spacer(modifier = Modifier.height(Dimens.PaddingExtraSmall))
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            ) {
                drawLine(
                    color = Color.White.copy(alpha = 0.08f),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }
    }
}

@Composable
private fun ChartBar(
    categoryTotal: CategoryTotal,
    maxTotal: Double,
    barWidth: Dp,
    chartHeight: Dp,
    animationDelay: Int,
    modifier: Modifier = Modifier
) {
    val heightFraction = remember { Animatable(0f) }

    LaunchedEffect(categoryTotal.total, maxTotal) {
        heightFraction.animateTo(
            targetValue = if (maxTotal > 0)
                (categoryTotal.total / maxTotal).toFloat()
            else 0f,
            animationSpec = tween(
                durationMillis = BAR_ANIMATION_DURATION_MS,
                delayMillis = animationDelay
            )
        )
    }

    Column(
        modifier = modifier.width(barWidth),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        // Amount label on top of bar
        Text(
            text = "₩${FormatUtils.formatCompactAmount(categoryTotal.total)}",
            style = MonoTextStyle.copy(
                fontSize = 8.sp
            ),
            color = AmberPrimary,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Animated bar drawn with Canvas
        Canvas(
            modifier = Modifier
                .width(barWidth)
                .height(chartHeight)
        ) {
            val barHeight = size.height * heightFraction.value
            val barTop = size.height - barHeight

            if (barHeight > 0f) {
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(AmberPrimary, AmberGradientEnd),
                        startY = barTop,
                        endY = size.height
                    ),
                    topLeft = Offset(0f, barTop),
                    size = Size(size.width, barHeight),
                    cornerRadius = CornerRadius(
                        BAR_CORNER_RADIUS_DP.dp.toPx()
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.PaddingExtraSmall))

        // Emoji label
        Text(
            text = categoryTotal.categoryEmoji,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(barWidth)
        )
    }
}

@Composable
@PreviewLightDark
fun PreviewSpendingBarChart() {
    ExpenseTrackerTheme {
        SpendingBarChart(categoryTotals = categoryTotal)
    }
}

val categoryTotal = listOf(
    CategoryTotal("Food & Drink", "🍔", 12000.0),
    CategoryTotal("Shopping", "👕", 20000.0),
    CategoryTotal("Transport", "🚗", 15000.0)
)