package me.mudkip.moememos.ui.preview.pages

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import me.mudkip.moememos.R
import me.mudkip.moememos.data.model.MemoVisibility
import me.mudkip.moememos.ext.string
import me.mudkip.moememos.ext.titleResource
import androidx.compose.ui.res.stringResource
import me.mudkip.moememos.ui.component.MemoContent
import me.mudkip.moememos.ui.designsystem.component.MoeAppBar
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeRadius
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.preview.MoeMemosPreviewTheme
import me.mudkip.moememos.ui.preview.PreviewData

/**
 * Memo Detail Page Preview
 *
 * 展示备忘录详情页面的完整内容，包括：
 * - 顶部栏（返回按钮、标题、更多操作）
 * - 备忘录元信息卡片（时间、可见性）
 * - 备忘录内容卡片
 *
 * 预览状态：
 * - Normal: 正常有数据状态
 * - SyncPending: 待同步状态
 * - LongContent: 长内容状态
 * - NotFound: 备忘录未找到状态
 */

// ==================== Preview States ====================

private val normalMemo get() = PreviewData.sampleMemo
private val syncPendingMemo get() = PreviewData.sampleMemo.copy(needsSync = true)
private val longContentMemo get() = PreviewData.sampleMemo.copy(
    content = """
        # Long Memo Title

        This is a long memo with multiple sections:

        ## Section 1
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.

        ## Section 2
        - Item 1
        - Item 2
        - Item 3

        ## Code Block
        ```kotlin
        fun main() {
            println("Hello, World!")
        }
        ```

        ## Tasks
        - [x] Task 1
        - [ ] Task 2
        - [ ] Task 3

        This is the end of the long content memo.
    """.trimIndent()
)

// ==================== Main Previews ====================

/**
 * 详情页预览 - 正常状态
 */
@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun MemoDetailPagePreview_Normal() {
    MoeMemosPreviewTheme {
        MemoDetailPageContent(
            memo = normalMemo,
            isLocalAccount = false
        )
    }
}

/**
 * 详情页预览 - 待同步状态
 */
@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun MemoDetailPagePreview_SyncPending() {
    MoeMemosPreviewTheme {
        MemoDetailPageContent(
            memo = syncPendingMemo,
            isLocalAccount = false
        )
    }
}

/**
 * 详情页预览 - 长内容
 */
@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun MemoDetailPagePreview_LongContent() {
    MoeMemosPreviewTheme {
        MemoDetailPageContent(
            memo = longContentMemo,
            isLocalAccount = false
        )
    }
}

/**
 * 详情页预览 - 未找到
 */
@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun MemoDetailPagePreview_NotFound() {
    MoeMemosPreviewTheme {
        MemoDetailPageContent(
            memo = null,
            isLocalAccount = false
        )
    }
}

// ==================== Page Content ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemoDetailPageContent(
    memo: me.mudkip.moememos.data.local.entity.MemoEntity?,
    isLocalAccount: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors

    Scaffold(
        containerColor = colors.bgApp,
        topBar = {
            MoeAppBar(
                title = R.string.memo.string,
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = R.string.back.string
                        )
                    }
                },
                actions = {
                    if (memo != null) {
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Outlined.MoreVert,
                                contentDescription = "More"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (memo == null) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = R.string.memo_not_found.string,
                    style = MoeTypography.body,
                    color = colors.textSecondary
                )
            }
            return@Scaffold
        }

        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = MoeSpacing.xl, vertical = MoeSpacing.md)
                .verticalScroll(rememberScrollState())
        ) {
            // Metadata Card
            me.mudkip.moememos.ui.designsystem.component.MoeCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(MoeSpacing.xl),
                containerColor = colors.bgSurface,
            ) {
                Column {
                    Text(
                        text = DateUtils.getRelativeTimeSpanString(
                            memo.date.toEpochMilli(),
                            System.currentTimeMillis(),
                            DateUtils.SECOND_IN_MILLIS
                        ).toString(),
                        style = MoeTypography.caption,
                        color = colors.textTertiary,
                    )
                    Row(
                        modifier = Modifier
                            .padding(top = MoeSpacing.md)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = R.string.memo.string,
                            style = MoeTypography.headline,
                            color = colors.textPrimary,
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            text = stringResource(memo.visibility.titleResource),
                            style = MoeTypography.label,
                            color = colors.accentPrimary,
                            modifier = Modifier
                                .background(
                                    color = colors.accentSoft,
                                    shape = MoeRadius.shapeFull,
                                )
                                .padding(horizontal = MoeSpacing.md, vertical = MoeSpacing.xs),
                        )
                    }
                    if (!isLocalAccount && memo.needsSync) {
                        Row(
                            modifier = Modifier.padding(top = MoeSpacing.md),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CloudOff,
                                contentDescription = R.string.memo_sync_pending.string,
                                modifier = Modifier.size(18.dp),
                                tint = colors.accentDanger,
                            )
                            Text(
                                text = R.string.memo_sync_pending.string,
                                style = MoeTypography.caption,
                                color = colors.textSecondary,
                                modifier = Modifier.padding(start = MoeSpacing.sm),
                            )
                        }
                    }
                }
            }

            // Content Card
            me.mudkip.moememos.ui.designsystem.component.MoeCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MoeSpacing.md, bottom = MoeSpacing.xxl),
                containerColor = colors.bgElevated,
            ) {
                MemoContent(
                    memo = memo,
                    selectable = true,
                    previewMode = true
                )
            }
        }
    }
}
