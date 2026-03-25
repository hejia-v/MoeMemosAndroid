package me.mudkip.moememos.ui.component

import android.text.TextUtils
import android.text.format.DateUtils
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import me.mudkip.moememos.data.model.Memo
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography

@Composable
fun ExploreMemoCard(
    memo: Memo
) {
    val colors = MoeDesignTokens.colors

    MoeCard(
        modifier = Modifier
            .padding(horizontal = MoeSpacing.xl, vertical = MoeSpacing.sm)
            .fillMaxWidth(),
        containerColor = colors.bgSurface,
    ) {
        Column(
            modifier = Modifier.padding(bottom = MoeSpacing.md)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = MoeSpacing.lg, top = MoeSpacing.lg, bottom = MoeSpacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    DateUtils.getRelativeTimeSpanString(memo.date.toEpochMilli(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString(),
                    style = MoeTypography.caption,
                    color = colors.textTertiary
                )

                if (memo.creator != null && !TextUtils.isEmpty(memo.creator.name)) {
                    Text(
                        "@${memo.creator.name}",
                        modifier = Modifier
                            .padding(start = MoeSpacing.md)
                            .weight(1f),
                        style = MoeTypography.caption,
                        color = colors.textSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            MemoContent(memo, previewMode = false)
        }
    }
}
