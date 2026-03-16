package me.mudkip.moememos.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import me.mudkip.moememos.data.model.AppThemeMode
import me.mudkip.moememos.ui.designsystem.token.DarkMoeColors
import me.mudkip.moememos.ui.designsystem.token.LightMoeColors

private val DarkColorScheme = darkColorScheme(
    primary = DarkMoeColors.accentPrimary,
    onPrimary = DarkMoeColors.textOnAccent,
    primaryContainer = DarkMoeColors.accentSoft,
    onPrimaryContainer = DarkMoeColors.textPrimary,
    secondary = DarkMoeColors.textSecondary,
    onSecondary = DarkMoeColors.bgSurface,
    secondaryContainer = DarkMoeColors.bgOverlay,
    onSecondaryContainer = DarkMoeColors.textPrimary,
    tertiary = DarkMoeColors.accentInfo,
    onTertiary = DarkMoeColors.bgSurface,
    tertiaryContainer = DarkMoeColors.bgOverlay,
    onTertiaryContainer = DarkMoeColors.textPrimary,
    error = DarkMoeColors.accentDanger,
    errorContainer = DarkMoeColors.bgPressed,
    onError = DarkMoeColors.textOnAccent,
    onErrorContainer = DarkMoeColors.textPrimary,
    background = DarkMoeColors.bgApp,
    onBackground = DarkMoeColors.textPrimary,
    surface = DarkMoeColors.bgSurface,
    onSurface = DarkMoeColors.textPrimary,
    surfaceVariant = DarkMoeColors.bgOverlay,
    onSurfaceVariant = DarkMoeColors.textSecondary,
    outline = DarkMoeColors.strokeStrong,
    inverseOnSurface = LightMoeColors.textPrimary,
    inverseSurface = LightMoeColors.bgSurface,
    inversePrimary = LightMoeColors.accentPrimary,
    surfaceTint = DarkMoeColors.accentPrimary,
)

private val LightColorScheme = lightColorScheme(
    primary = LightMoeColors.accentPrimary,
    onPrimary = LightMoeColors.textOnAccent,
    primaryContainer = LightMoeColors.accentSoft,
    onPrimaryContainer = LightMoeColors.textPrimary,
    secondary = LightMoeColors.textSecondary,
    onSecondary = LightMoeColors.bgSurface,
    secondaryContainer = LightMoeColors.bgOverlay,
    onSecondaryContainer = LightMoeColors.textPrimary,
    tertiary = LightMoeColors.accentInfo,
    onTertiary = LightMoeColors.textOnAccent,
    tertiaryContainer = LightMoeColors.bgOverlay,
    onTertiaryContainer = LightMoeColors.textPrimary,
    error = LightMoeColors.accentDanger,
    errorContainer = LightMoeColors.bgPressed,
    onError = LightMoeColors.textOnAccent,
    onErrorContainer = LightMoeColors.textPrimary,
    background = LightMoeColors.bgApp,
    onBackground = LightMoeColors.textPrimary,
    surface = LightMoeColors.bgSurface,
    onSurface = LightMoeColors.textPrimary,
    surfaceVariant = LightMoeColors.bgOverlay,
    onSurfaceVariant = LightMoeColors.textSecondary,
    outline = LightMoeColors.strokeStrong,
    inverseOnSurface = DarkMoeColors.textPrimary,
    inverseSurface = DarkMoeColors.bgSurface,
    inversePrimary = DarkMoeColors.accentPrimary,
    surfaceTint = LightMoeColors.accentPrimary,
)

@Composable
fun MoeMemosTheme(
        themeMode: AppThemeMode = AppThemeMode.SYSTEM,
        dynamicColor: Boolean = false,
        content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
    }
    val colorScheme = when {
        dynamicColor -> if (darkTheme) DarkColorScheme else LightColorScheme
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
    )
}
