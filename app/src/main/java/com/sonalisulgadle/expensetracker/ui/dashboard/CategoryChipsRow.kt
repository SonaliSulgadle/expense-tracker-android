package com.sonalisulgadle.expensetracker.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sonalisulgadle.expensetracker.R
import com.sonalisulgadle.expensetracker.ui.theme.AmberDim
import com.sonalisulgadle.expensetracker.ui.theme.AmberPrimary
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingExtraLarge
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingSmall

@Composable
fun CategoryChipsRow(
    categories: List<String>,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val allLabel = stringResource(R.string.all)
    val allCategories = listOf(allLabel) + categories

    LazyRow(
        contentPadding = PaddingValues(horizontal = PaddingExtraLarge),
        horizontalArrangement = Arrangement.spacedBy(PaddingSmall),
        modifier = modifier
    ) {
        items(allCategories) { category ->
            val isSelected = category == (selectedCategory ?: allLabel)
            CategoryChip(
                label = category,
                isSelected = isSelected,
                onClick = {
                    selectedCategory = if (isSelected) null else category
                }
            )
        }
    }
}

@Composable
private fun CategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(PaddingExtraLarge))
            .background(
                if (isSelected) AmberDim
                else MaterialTheme.colorScheme.surface
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected) AmberPrimary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}