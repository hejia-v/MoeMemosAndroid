package me.mudkip.moememos.ui.preview.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import me.mudkip.moememos.data.local.entity.MemoEntity
import me.mudkip.moememos.ui.component.MemoContent
import me.mudkip.moememos.ui.designsystem.component.MoeAppBar
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.preview.MoeMemosPreviewTheme
import me.mudkip.moememos.ui.preview.PreviewData
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * ArchivedMemoPage Preview
 *
 * 展示归档备忘录页面的完整内容，包括：
 * - 顶部栏（菜单按钮、标题）
 * - 归档备忘录列表
 */

// ==================== Preview Data ====================

private val archivedMemos: List<MemoEntity>
    get() = List(5) { index ->
        PreviewData.sampleArchivedMemo.copy(
            identifier = "archived-memo-$index",
            content = when (index) {
                0 -> "This is an archived memo from last month."
                1 -> "Meeting notes that are no longer needed."
                2 -> "Old project requirements document."
                3 -> "Expired todo list from previous sprint."
                else -> "Just some old notes that I archived."
            }
        )
    }

// ==================== Main Preview ====================

/**
 * 归档页面预览 - 有数据状态
 */
@PreviewLightDark
@Composable
fun ArchivedMemoPagePreview_DataState() {
    MoeMemosPreviewTheme {
        ArchivedMemoPageContent(
            memos = archivedMemos
        )
    }
}

/**
 * 归档页面预览 - 空状态
 */
@PreviewLightDark
@Composable
fun ArchivedMemoPagePreview_EmptyState() {
    MoeMemosPreviewTheme {
        ArchivedMemoPageContent(
            memos = emptyList()
        )
    }
}

/**
 * 归档页面预览 - 单条数据
 */
@PreviewLightDark
@Composable
fun ArchivedMemoPagePreview_SingleItem() {
    MoeMemosPreviewTheme {
        ArchivedMemoPageContent(
            memos = listOf(PreviewData.sampleArchivedMemo)
        )
    }
}

// ==================== Page Content ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArchivedMemoPageContent(
    memos: List<MemoEntity>,
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors

    Scaffold(
        containerColor = colors.bgApp,
        topBar = {
            MoeAppBar(
                title = "Archived",
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (memos.isEmpty()) {
            ArchivedEmptyContent(
                modifier = Modifier
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
                    ArchivedMemoCardItem(memo = memo)
                }
            }
        }
    }
}

// ==================== Components ====================

@Composable
private fun ArchivedMemoCardItem(
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
private fun ArchivedEmptyContent(
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "No archived memos",
            style = MoeTypography.title,
            color = colors.textSecondary
        )

        Text(
            text = "Archived memos will appear here",
            style = MoeTypography.body,
            color = colors.textTertiary,
            modifier = Modifier.padding(top = MoeSpacing.sm)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}
