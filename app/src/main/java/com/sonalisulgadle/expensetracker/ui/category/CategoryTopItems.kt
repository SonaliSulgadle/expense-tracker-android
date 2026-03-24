package com.sonalisulgadle.expensetracker.ui.category

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonalisulgadle.expensetracker.ui.theme.AmberGradientEnd
import com.sonalisulgadle.expensetracker.ui.theme.AmberPrimary
import com.sonalisulgadle.expensetracker.ui.theme.Dimens
import com.sonalisulgadle.expensetracker.ui.theme.MonoTextStyle
import com.sonalisulgadle.expensetracker.util.FormatUtils

private const val BAR_ANIMATION_DURATION_MS = 600
private const val BAR_MIN_WIDTH_FRACTION = 0.05f

@Composable
fun CategoryTopItems(
    topItems: List<TopItem>,
    modifier: Modifier = Modifier
) {
    if (topItems.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
    ) {
        topItems.forEachIndexed { index, item ->
            TopItemBar(
                item = item,
                animationDelay = index * 80,
            )
        }
    }
}

@Composable
private fun TopItemBar(
    item: TopItem,
    animationDelay: Int,
    modifier: Modifier = Modifier
) {
    // To animate bar width from 0 to actual percentage
    val animatedWidth = remember { Animatable(0f) }

    LaunchedEffect(item.percentage) {
        animatedWidth.animateTo(
            targetValue = item.percentage
                .coerceAtLeast(BAR_MIN_WIDTH_FRACTION),
            animationSpec = tween(
                durationMillis = BAR_ANIMATION_DURATION_MS,
                delayMillis = animationDelay
            )
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingExtraSmall)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "₩${FormatUtils.formatAmount(item.amount)}",
                style = MonoTextStyle.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = AmberPrimary,
                modifier = Modifier.padding(start = Dimens.PaddingSmall)
            )
        }

        // Bar track + fill
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(Dimens.RadiusSmall))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedWidth.value)
                    .height(6.dp)
                    .clip(RoundedCornerShape(Dimens.RadiusSmall))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(AmberPrimary, AmberGradientEnd)
                        )
                    )
            )
        }
    }
}