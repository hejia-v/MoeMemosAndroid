package me.mudkip.moememos.ui.designsystem.token

import androidx.compose.ui.graphics.Color

data class MoeColors(
    val bgApp: Color,
    val bgSurface: Color,
    val bgElevated: Color,
    val bgOverlay: Color,
    val bgPressed: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textOnAccent: Color,
    val strokeSubtle: Color,
    val strokeStrong: Color,
    val accentPrimary: Color,
    val accentSoft: Color,
    val accentDanger: Color,
    val accentWarning: Color,
    val accentInfo: Color,
)

val LightMoeColors = MoeColors(
    bgApp = Color(0xFFF6F4EF),
    bgSurface = Color(0xFFFFFCF7),
    bgElevated = Color(0xFFFFFFFF),
    bgOverlay = Color(0xFFFFF8EF),
    bgPressed = Color(0xFFECE6DC),
    textPrimary = Color(0xFF171A1C),
    textSecondary = Color(0xFF596068),
    textTertiary = Color(0xFF808891),
    textOnAccent = Color(0xFFFFFFFF),
    strokeSubtle = Color(0xFFE5DED2),
    strokeStrong = Color(0xFFCFC6B9),
    accentPrimary = Color(0xFF17643E),
    accentSoft = Color(0xFFDCEFE3),
    accentDanger = Color(0xFFC44D42),
    accentWarning = Color(0xFFB97818),
    accentInfo = Color(0xFF2D6CA3),
)

val DarkMoeColors = MoeColors(
    bgApp = Color(0xFF121417),
    bgSurface = Color(0xFF181C20),
    bgElevated = Color(0xFF20252A),
    bgOverlay = Color(0xFF252B31),
    bgPressed = Color(0xFF2B3239),
    textPrimary = Color(0xFFE7E2D8),
    textSecondary = Color(0xFFB1B8BF),
    textTertiary = Color(0xFF88919A),
    textOnAccent = Color(0xFF0B2015),
    strokeSubtle = Color(0xFF303740),
    strokeStrong = Color(0xFF46505A),
    accentPrimary = Color(0xFF6FC18F),
    accentSoft = Color(0xFF1D4631),
    accentDanger = Color(0xFFF28C84),
    accentWarning = Color(0xFFE1AF5F),
    accentInfo = Color(0xFF7FB7E8),
)
