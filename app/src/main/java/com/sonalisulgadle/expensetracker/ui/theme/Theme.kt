package com.sonalisulgadle.expensetracker.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ---- Dark color scheme ----
private val DarkColorScheme = darkColorScheme(
    primary = AmberPrimary,
    onPrimary = DarkBackground,
    primaryContainer = DarkSurface,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVar,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,
    onSurfaceVariant = DarkTextSecond,
    outline = DarkBorder,
    error = ErrorDark,
    onError = DarkBackground
)

// ---- Light color scheme ----
private val LightColorScheme = lightColorScheme(
    primary = AmberDeep,
    onPrimary = LightSurface,
    primaryContainer = LightSurfaceVar,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVar,
    onBackground = LightTextPrimary,
    onSurface = LightTextPrimary,
    onSurfaceVariant = LightTextSecond,
    outline = LightBorder,
    error = ErrorLight,
    onError = LightSurface
)

// ---- Custom colors not in Material 3 scheme ----
// (success, category tints, mono text — accessed via CompositionLocal)
data class ExtendedColors(
    val success: Color,
    val amberDim: Color,
    val amberLight: Color,
    val textTertiary: Color,
    val borderLight: Color,
    val surfaceElevated: Color
)

val DarkExtendedColors = ExtendedColors(
    success = SuccessDark,
    amberDim = AmberDim,
    amberLight = AmberLight,
    textTertiary = DarkTextTertiary,
    borderLight = DarkBorderLight,
    surfaceElevated = DarkSurfaceVar
)

val LightExtendedColors = ExtendedColors(
    success = SuccessLight,
    amberDim = Color(0x1FE8920A),
    amberLight = AmberDeep,
    textTertiary = Color(0xFF9CA3AF),
    borderLight = Color(0xFFD1C9B8),
    surfaceElevated = LightSurfaceVar
)

val LocalExtendedColors = staticCompositionLocalOf { DarkExtendedColors }

@Composable
fun ExpenseTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Make status bar transparent — edge to edge
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    androidx.compose.runtime.CompositionLocalProvider(
        LocalExtendedColors provides extendedColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}

// Convenience accessor — use this in any Composable
// MaterialTheme.extendedColors.success
val MaterialTheme.extendedColors: ExtendedColors
    @Composable get() = LocalExtendedColors.current