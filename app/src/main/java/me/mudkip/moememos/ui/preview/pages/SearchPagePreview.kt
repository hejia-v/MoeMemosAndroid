package me.mudkip.moememos.ui.preview.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import me.mudkip.moememos.data.local.entity.MemoEntity
import me.mudkip.moememos.ui.component.MemoContent
import me.mudkip.moememos.ui.designsystem.component.MoeAppBar
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.component.MoeInputField
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.preview.MoeMemosPreviewTheme
import me.mudkip.moememos.ui.preview.PreviewData
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * SearchPage Preview
 *
 * 展示搜索页面的完整内容，包括：
 * - 顶部栏（返回按钮、标题）
 * - 搜索框
 * - 搜索结果列表
 */

// ==================== Preview Data ====================

private val searchResults: List<MemoEntity>
    get() = List(3) { index ->
        PreviewData.sampleMemo.copy(
            identifier = "search-result-$index",
            content = when (index) {
                0 -> "This is a search result about **project** planning."
                1 -> "# Meeting Notes\n\nDiscussed the project timeline and milestones."
                else -> "Quick note about project resources needed."
            }
        )
    }

// ==================== Main Preview ====================

/**
 * 搜索页面预览 - 有搜索结果
 */
@PreviewLightDark
@Composable
fun SearchPagePreview_WithResults() {
    MoeMemosPreviewTheme {
        SearchPageContent(
            searchText = TextFieldValue("project"),
            results = searchResults
        )
    }
}

/**
 * 搜索页面预览 - 空搜索框
 */
@PreviewLightDark
@Composable
fun SearchPagePreview_EmptySearch() {
    MoeMemosPreviewTheme {
        SearchPageContent(
            searchText = TextFieldValue(""),
            results = emptyList()
        )
    }
}

/**
 * 搜索页面预览 - 无结果
 */
@PreviewLightDark
@Composable
fun SearchPagePreview_NoResults() {
    MoeMemosPreviewTheme {
        SearchPageContent(
            searchText = TextFieldValue("nonexistent"),
            results = emptyList()
        )
    }
}

// ==================== Page Content ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchPageContent(
    searchText: TextFieldValue,
    results: List<MemoEntity>,
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors
    var currentSearchText by remember { mutableStateOf(searchText) }

    Scaffold(
        containerColor = colors.bgApp,
        topBar = {
            MoeAppBar(
                title = "Search",
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search box
            MoeCard(
                modifier = Modifier
                    .padding(horizontal = MoeSpacing.xl, vertical = MoeSpacing.sm)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(MoeSpacing.lg),
                containerColor = colors.bgSurface,
            ) {
                MoeInputField(
                    modifier = Modifier.fillMaxWidth(),
                    value = currentSearchText,
                    onValueChange = { currentSearchText = it },
                    maxLines = 1,
                    placeholder = "Search memos...",
                )
            }

            // Results
            if (results.isEmpty() && currentSearchText.text.isNotEmpty()) {
                NoResultsContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else if (results.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(MoeSpacing.xs)
                ) {
                    items(results) { memo ->
                        SearchResultCard(memo = memo)
                    }
                }
            } else {
                EmptySearchHint(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}

// ==================== Components ====================

@Composable
private fun SearchResultCard(
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
private fun NoResultsContent(
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "No results found",
            style = MoeTypography.title,
            color = colors.textSecondary
        )

        Text(
            text = "Try a different search term",
            style = MoeTypography.body,
            color = colors.textTertiary,
            modifier = Modifier.padding(top = MoeSpacing.sm)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun EmptySearchHint(
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Search your memos",
            style = MoeTypography.title,
            color = colors.textSecondary
        )

        Text(
            text = "Type to search through all your memos",
            style = MoeTypography.body,
            color = colors.textTertiary,
            modifier = Modifier.padding(top = MoeSpacing.sm)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}
