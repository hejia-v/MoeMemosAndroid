package me.mudkip.moememos.ui.designsystem.token

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

object MoeMotion {
    val fast: Duration = 120.milliseconds
    val normal: Duration = 220.milliseconds
    val slow: Duration = 320.milliseconds
    val emphasized: Duration = 420.milliseconds

    val standard: Easing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
    val decelerate: Easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
    val accelerate: Easing = CubicBezierEasing(0.3f, 0.0f, 1.0f, 1.0f)
    val springSoft = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow,
    )
}
