package me.mudkip.moememos.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import me.mudkip.moememos.data.model.DailyUsageStat
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.preview.MoeMemosPreviewTheme
import java.time.LocalDate

@Composable
fun HeatmapStat(day: DailyUsageStat) {
    val colors = MoeDesignTokens.colors
    val borderWidth = if (day.date == LocalDate.now()) 1.dp else 0.dp
    val color = when (day.count) {
        0 -> colors.bgPressed
        1 -> colors.accentSoft
        2 -> colors.accentPrimary.copy(alpha = 0.45f)
        in 3..4 -> colors.accentPrimary.copy(alpha = 0.7f)
        else -> colors.accentPrimary
    }
    var modifier = Modifier
        .fillMaxSize()
        .aspectRatio(1F, true)
        .clip(RoundedCornerShape(2.dp))
        .background(color = color)
    if (day.date == LocalDate.now()) {
        modifier = modifier.border(
            borderWidth,
            colors.textPrimary,
            shape = RoundedCornerShape(2.dp)
        )
    }

    Box(modifier = modifier)
}

// ==================== Previews ====================

@PreviewLightDark
@Composable
private fun HeatmapStatPreview() {
    MoeMemosPreviewTheme {
        Row(
            modifier = Modifier.padding(MoeSpacing.lg)
        ) {
            // No activity
            Box(modifier = Modifier.size(20.dp)) {
                HeatmapStat(day = DailyUsageStat(date = LocalDate.now().minusDays(6), count = 0))
            }
            // 1 memo
            Box(modifier = Modifier.size(20.dp)) {
                HeatmapStat(day = DailyUsageStat(date = LocalDate.now().minusDays(5), count = 1))
            }
            // 2 memos
            Box(modifier = Modifier.size(20.dp)) {
                HeatmapStat(day = DailyUsageStat(date = LocalDate.now().minusDays(4), count = 2))
            }
            // 3 memos
            Box(modifier = Modifier.size(20.dp)) {
                HeatmapStat(day = DailyUsageStat(date = LocalDate.now().minusDays(3), count = 3))
            }
            // 5 memos
            Box(modifier = Modifier.size(20.dp)) {
                HeatmapStat(day = DailyUsageStat(date = LocalDate.now().minusDays(2), count = 5))
            }
            // Today
            Box(modifier = Modifier.size(20.dp)) {
                HeatmapStat(day = DailyUsageStat(date = LocalDate.now(), count = 2))
            }
        }
    }
}
