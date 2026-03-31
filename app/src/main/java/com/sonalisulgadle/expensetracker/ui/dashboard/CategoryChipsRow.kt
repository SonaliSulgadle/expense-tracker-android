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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.sonalisulgadle.expensetracker.R
import com.sonalisulgadle.expensetracker.data.local.CategoryTotal
import com.sonalisulgadle.expensetracker.ui.theme.AmberDim
import com.sonalisulgadle.expensetracker.ui.theme.AmberPrimary
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingExtraLarge
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingSmall

@Composable
fun CategoryChipsRow(
    modifier: Modifier = Modifier,
    categoryTotals: List<CategoryTotal>,
    onCategoryClick: ((String, String) -> Unit)? = null
) {
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val allLabel = stringResource(R.string.all)

    LazyRow(
        contentPadding = PaddingValues(horizontal = PaddingExtraLarge),
        horizontalArrangement = Arrangement.spacedBy(PaddingSmall),
        modifier = modifier
    ) {
        item {
            CategoryChip(
                label = allLabel,
                isSelected = selectedCategory == null,
                onClick = { selectedCategory = null }
            )
        }
        items(
            items = categoryTotals,
            key = { it.category }
        ) { total ->
            CategoryChip(
                label = total.category,
                isSelected = selectedCategory == total.category,
                onClick = {
                    selectedCategory = total.category
                    onCategoryClick?.invoke(total.category, total.categoryEmoji)
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
            .clickable(
                onClickLabel = if (isSelected)
                    "Clear $label filter"
                else
                    "Filter by $label",
                role = Role.Button,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 7.dp)
            .semantics {
                selected = isSelected
            }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected) AmberPrimary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}