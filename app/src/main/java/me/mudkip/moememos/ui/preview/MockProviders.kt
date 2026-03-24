package me.mudkip.moememos.ui.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.mudkip.moememos.data.local.entity.MemoEntity
import me.mudkip.moememos.data.model.SyncStatus
import me.mudkip.moememos.ui.component.MemoContent
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.page.common.LocalRootNavController
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Mock Preview Providers
 *
 * 提供简化的预览支持。由于 ViewModel 是 final 类，无法直接 mock，
 * 这里提供简化的 Composable 组件用于预览页面内容。
 */

// ==================== Preview States ====================

/**
 * 常用的预览状态配置
 */
object PreviewStates {
    val emptyMemos = emptyList<MemoEntity>()
    val fiveMemos: List<MemoEntity> get() = List(5) { index ->
        PreviewData.sampleMemo.copy(
            identifier = "memo-$index",
            content = "This is sample memo #$index with some content to display.",
            pinned = index == 0
        )
    }

    val syncing = SyncStatus(syncing = true, unsyncedCount = 0)
    val synced = SyncStatus(syncing = false, unsyncedCount = 0)
    val hasUnsynced = SyncStatus(syncing = false, unsyncedCount = 3)
}

// ==================== Simplified Content Previews ====================

/**
 * 简化的备忘录卡片内容预览
 *
 * 展示单个备忘录卡片的完整样式，不依赖 ViewModel
 */
@Composable
fun SimpleMemoCardPreview(
    memo: MemoEntity = PreviewData.sampleMemo
) {
    val colors = MoeDesignTokens.colors

    MoeCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MoeSpacing.lg, vertical = MoeSpacing.sm),
        contentPadding = PaddingValues(MoeSpacing.lg),
        containerColor = colors.bgElevated
    ) {
        Column {
            // 时间戳
            Text(
                text = remember(memo.date) {
                    DateTimeFormatter
                        .ofLocalizedDateTime(FormatStyle.MEDIUM)
                        .format(memo.date.atZone(ZoneId.systemDefault()))
                },
                style = MoeTypography.caption,
                color = colors.textTertiary
            )

            Spacer(modifier = Modifier.height(MoeSpacing.sm))

            // 内容
            MemoContent(
                memo = memo,
                previewMode = true
            )
        }
    }
}

/**
 * 简化的备忘录列表内容预览
 *
 * 展示多个备忘录卡片的列表样式
 */
@Composable
fun SimpleMemoListPreview(
    memos: List<MemoEntity> = PreviewStates.fiveMemos
) {
    val colors = MoeDesignTokens.colors

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = MoeSpacing.sm)
    ) {
        items(memos) { memo ->
            MoeCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MoeSpacing.lg, vertical = MoeSpacing.sm),
                contentPadding = PaddingValues(MoeSpacing.lg),
                containerColor = colors.bgElevated
            ) {
                Column {
                    Text(
                        text = remember(memo.date) {
                            DateTimeFormatter
                                .ofLocalizedDateTime(FormatStyle.MEDIUM)
                                .format(memo.date.atZone(ZoneId.systemDefault()))
                        },
                        style = MoeTypography.caption,
                        color = colors.textTertiary
                    )

                    Spacer(modifier = Modifier.height(MoeSpacing.sm))

                    MemoContent(
                        memo = memo,
                        previewMode = true
                    )
                }
            }
        }
    }
}

// ==================== Empty State Preview ====================

/**
 * 空状态预览
 */
@Composable
fun EmptyStatePreview(
    message: String = "No memos yet"
) {
    val colors = MoeDesignTokens.colors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MoeSpacing.xl),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MoeTypography.body,
            color = colors.textSecondary
        )
    }
}
