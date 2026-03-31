package com.sonalisulgadle.expensetracker.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.sonalisulgadle.expensetracker.R
import com.sonalisulgadle.expensetracker.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(
    targetValue: SwipeToDismissBoxValue
) {
    val isActive = targetValue == SwipeToDismissBoxValue.EndToStart

    val deleteExpenseDescription = stringResource(R.string.delete_expense)
    val backgroundColor by animateColorAsState(
        targetValue = if (isActive) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
        animationSpec = tween(durationMillis = 300),
        label = "delete_bg_color"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(Dimens.RadiusLarge))
            .background(backgroundColor)
            .semantics {
                contentDescription = deleteExpenseDescription
            },
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(R.string.delete_expense),
            tint = Color.White,
            modifier = Modifier
                .padding(end = Dimens.PaddingExtraLarge)
                .size(Dimens.IconLarge)
        )
    }
}