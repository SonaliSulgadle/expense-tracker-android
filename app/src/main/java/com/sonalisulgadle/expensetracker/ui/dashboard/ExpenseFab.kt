package com.sonalisulgadle.expensetracker.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonalisulgadle.expensetracker.R
import com.sonalisulgadle.expensetracker.ui.theme.AmberGradientEnd
import com.sonalisulgadle.expensetracker.ui.theme.AmberPrimary
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.FabIconSize
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.PaddingLarge
import com.sonalisulgadle.expensetracker.ui.theme.Dimens.RadiusSmall
import com.sonalisulgadle.expensetracker.ui.theme.OnPrimaryDark

@Composable
fun AddExpenseFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(PaddingLarge))
            .background(
                Brush.linearGradient(
                    colors = listOf(AmberPrimary, AmberGradientEnd)
                )
            )
            .clickable(
                role = Role.Button,
                onClickLabel = stringResource(R.string.add_expense)
            ) { onClick() }
            .defaultMinSize(minHeight = 48.dp)
            .padding(PaddingLarge),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(FabIconSize)
                    .clip(RoundedCornerShape(RadiusSmall))
                    .background(OnPrimaryDark),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.symbol_add),
                    color = AmberPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 16.sp
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.add_expense),
                style = MaterialTheme.typography.titleMedium,
                color = OnPrimaryDark,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}