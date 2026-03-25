package me.mudkip.moememos.ui.preview.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
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
import me.mudkip.moememos.data.local.entity.MemoEntity
import me.mudkip.moememos.ui.component.MemoContent
import me.mudkip.moememos.ui.designsystem.component.MoeAppBar
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.preview.MoeMemosPreviewTheme
import me.mudkip.moememos.ui.preview.PreviewData
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * TagMemoPage Preview
 *
 * 展示标签页面的完整内容，包括：
 * - 顶部栏（菜单按钮、标签名）
 * - 带标签过滤的备忘录列表
 */

// ==================== Preview Data ====================

private const val sampleTag = "work"

private val tagMemos: List<MemoEntity>
    get() = List(5) { index ->
        PreviewData.sampleMemo.copy(
            identifier = "tag-memo-$index",
            content = when (index) {
                0 -> "#work Project planning meeting tomorrow at 10am."
                1 -> "Review the #work quarterly report before Friday."
                2 -> "#work task: Update documentation for the new feature."
                3 -> "Remember to sync with the #work team about deadlines."
                else -> "#work notes from today's standup meeting."
            }
        )
    }

// ==================== Main Preview ====================

/**
 * 标签页面预览 - 有备忘录
 */
@PreviewLightDark
@Composable
fun TagMemoPagePreview_WithMemos() {
    MoeMemosPreviewTheme {
        TagMemoPageContent(
            tag = sampleTag,
            memos = tagMemos
        )
    }
}

/**
 * 标签页面预览 - 空标签
 */
@PreviewLightDark
@Composable
fun TagMemoPagePreview_EmptyTag() {
    MoeMemosPreviewTheme {
        TagMemoPageContent(
            tag = "empty-tag",
            memos = emptyList()
        )
    }
}

/**
 * 标签页面预览 - 常用标签（多条数据）
 */
@PreviewLightDark
@Composable
fun TagMemoPagePreview_PopularTag() {
    MoeMemosPreviewTheme {
        val popularTagMemos = List(10) { index ->
            PreviewData.sampleMemo.copy(
                identifier = "popular-tag-memo-$index",
                content = "#$sampleTag Task item #$index for the project."
            )
        }

        TagMemoPageContent(
            tag = sampleTag,
            memos = popularTagMemos
        )
    }
}

// ==================== Page Content ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TagMemoPageContent(
    tag: String,
    memos: List<MemoEntity>,
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors

    Scaffold(
        containerColor = colors.bgApp,
        topBar = {
            MoeAppBar(
                title = "#$tag",
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (memos.isEmpty()) {
            EmptyTagContent(
                tag = tag,
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(MoeSpacing.xs)
            ) {
                items(memos) { memo ->
                    TagMemoCard(memo = memo)
                }
            }
        }
    }
}

// ==================== Components ====================

@Composable
private fun TagMemoCard(
    memo: MemoEntity,
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors

    MoeCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MoeSpacing.xl, vertical = MoeSpacing.sm),
        containerColor = colors.bgSurface,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(start = MoeSpacing.lg)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    remember(memo.date) {
                        DateTimeFormatter
                            .ofLocalizedDateTime(FormatStyle.MEDIUM)
                            .format(memo.date.atZone(ZoneId.systemDefault()))
                    },
                    style = MoeTypography.caption,
                    color = colors.textTertiary
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "More")
                }
            }

            MemoContent(
                memo = memo,
                previewMode = true
            )
        }
    }
}

@Composable
private fun EmptyTagContent(
    tag: String,
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "#$tag",
            style = MoeTypography.headline,
            color = colors.textPrimary
        )

        Text(
            text = "No memos with this tag",
            style = MoeTypography.title,
            color = colors.textSecondary,
            modifier = Modifier.padding(top = MoeSpacing.md)
        )

        Text(
            text = "Memos tagged with #$tag will appear here",
            style = MoeTypography.body,
            color = colors.textTertiary,
            modifier = Modifier.padding(top = MoeSpacing.sm)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}
