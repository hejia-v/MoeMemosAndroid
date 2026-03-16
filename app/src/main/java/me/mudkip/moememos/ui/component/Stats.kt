package me.mudkip.moememos.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import me.mudkip.moememos.R
import me.mudkip.moememos.ext.string
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.viewmodel.LocalMemos
import me.mudkip.moememos.viewmodel.LocalUserState
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

@Composable
fun Stats(modifier: Modifier = Modifier) {
    val memosViewModel = LocalMemos.current
    val userStateViewModel = LocalUserState.current
    val colors = MoeDesignTokens.colors
    val days = remember(userStateViewModel.currentUser, LocalDate.now()) {
        userStateViewModel.currentUser?.let { currentUser ->
                ChronoUnit.DAYS.between(currentUser.startDate.atZone(OffsetDateTime.now().offset).toLocalDate(), LocalDate.now())
        } ?: 0
    }

    MoeCard(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            horizontal = MoeSpacing.lg,
            vertical = MoeSpacing.lg,
        ),
        containerColor = colors.bgSurface,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatsMetric(
                value = memosViewModel.memos.count().toString(),
                label = R.string.memo.string.uppercase(),
            )
            StatsMetric(
                value = memosViewModel.tags.count().toString(),
                label = R.string.tag.string.uppercase(),
            )
            StatsMetric(
                value = days.toString(),
                label = R.string.day.string.uppercase(),
            )
        }
    }
}

@Composable
private fun StatsMetric(
    value: String,
    label: String,
) {
    val colors = MoeDesignTokens.colors

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MoeTypography.headline,
            color = colors.textPrimary,
        )
        Text(
            text = label,
            style = MoeTypography.caption,
            color = colors.textTertiary,
        )
    }
}
