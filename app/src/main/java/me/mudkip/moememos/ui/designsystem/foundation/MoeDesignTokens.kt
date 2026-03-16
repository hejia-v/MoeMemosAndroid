package me.mudkip.moememos.ui.designsystem.foundation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import me.mudkip.moememos.ui.designsystem.token.DarkMoeColors
import me.mudkip.moememos.ui.designsystem.token.LightMoeColors
import me.mudkip.moememos.ui.designsystem.token.MoeColors

object MoeDesignTokens {
    val colors: MoeColors
        @Composable get() = if (isSystemInDarkTheme()) DarkMoeColors else LightMoeColors
}
