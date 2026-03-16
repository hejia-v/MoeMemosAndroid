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
    bgApp = Color(0xFF14171A),
    bgSurface = Color(0xFF1B2024),
    bgElevated = Color(0xFF22282D),
    bgOverlay = Color(0xFF2A3036),
    bgPressed = Color(0xFF313941),
    textPrimary = Color(0xFFF4F3EF),
    textSecondary = Color(0xFFB3BCC3),
    textTertiary = Color(0xFF8A949D),
    textOnAccent = Color(0xFF062413),
    strokeSubtle = Color(0xFF343B42),
    strokeStrong = Color(0xFF4B555E),
    accentPrimary = Color(0xFF79D7A5),
    accentSoft = Color(0xFF1D4A33),
    accentDanger = Color(0xFFFF8F84),
    accentWarning = Color(0xFFF0B457),
    accentInfo = Color(0xFF87BFF1),
)
