package me.mudkip.moememos.ui.preview.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import me.mudkip.moememos.data.local.entity.MemoEntity
import me.mudkip.moememos.data.model.MemoVisibility
import me.mudkip.moememos.ui.component.MemoContent
import me.mudkip.moememos.ui.designsystem.component.MoeAppBar
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeRadius
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.preview.MoeMemosPreviewTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * ExplorePage Preview
 *
 * 展示探索页面的完整内容，包括：
 * - 顶部栏（菜单按钮、标题）
 * - 公开备忘录列表
 * - 用户头像和名称
 */

// ==================== Preview Data ====================

private data class ExploreMemoWithUser(
    val memo: MemoEntity,
    val username: String
)

private val exploreMemos: List<ExploreMemoWithUser>
    get() = listOf(
        ExploreMemoWithUser(
            memo = MemoEntity(
                identifier = "explore-1",
                remoteId = "1",
                accountKey = "explore-account",
                content = "Just discovered this amazing note-taking app! #productivity",
                date = Instant.now().minusSeconds(3600),
                visibility = MemoVisibility.PUBLIC,
                pinned = false,
                archived = false,
                needsSync = false
            ),
            username = "Alice"
        ),
        ExploreMemoWithUser(
            memo = MemoEntity(
                identifier = "explore-2",
                remoteId = "2",
                accountKey = "explore-account",
                content = "## Tips for better notes\n\n- Keep it simple\n- Use tags\n- Review regularly",
                date = Instant.now().minusSeconds(7200),
                visibility = MemoVisibility.PUBLIC,
                pinned = false,
                archived = false,
                needsSync = false
            ),
            username = "Bob"
        ),
        ExploreMemoWithUser(
            memo = MemoEntity(
                identifier = "explore-3",
                remoteId = "3",
                accountKey = "explore-account",
                content = "Working on a new project today. Excited to share progress soon! #coding #opensource",
                date = Instant.now().minusSeconds(14400),
                visibility = MemoVisibility.PUBLIC,
                pinned = false,
                archived = false,
                needsSync = false
            ),
            username = "Charlie"
        )
    )

// ==================== Main Preview ====================

/**
 * 探索页面预览 - 有公开备忘录
 */
@PreviewLightDark
@Composable
fun ExplorePagePreview_WithPublicMemos() {
    MoeMemosPreviewTheme {
        ExplorePageContent(
            memos = exploreMemos
        )
    }
}

/**
 * 探索页面预览 - 空状态
 */
@PreviewLightDark
@Composable
fun ExplorePagePreview_EmptyState() {
    MoeMemosPreviewTheme {
        ExplorePageContent(
            memos = emptyList()
        )
    }
}

// ==================== Page Content ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExplorePageContent(
    memos: List<ExploreMemoWithUser>,
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors

    Scaffold(
        containerColor = colors.bgApp,
        topBar = {
            MoeAppBar(
                title = "Explore",
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (memos.isEmpty()) {
            EmptyExploreContent(
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
                items(memos) { item ->
                    ExploreMemoCard(
                        memo = item.memo,
                        username = item.username
                    )
                }
            }
        }
    }
}

// ==================== Components ====================

@Composable
private fun ExploreMemoCard(
    memo: MemoEntity,
    username: String,
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
                    .padding(start = MoeSpacing.lg, end = MoeSpacing.md, top = MoeSpacing.md)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User avatar placeholder
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(MoeRadius.shapeFull)
                        .background(colors.accentSoft),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = username.firstOrNull()?.toString() ?: "?",
                        style = MoeTypography.label,
                        color = colors.accentPrimary
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = MoeSpacing.md)
                ) {
                    Text(
                        text = username,
                        style = MoeTypography.label,
                        color = colors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = remember(memo.date) {
                            DateTimeFormatter
                                .ofLocalizedDateTime(FormatStyle.MEDIUM)
                                .format(memo.date.atZone(ZoneId.systemDefault()))
                        },
                        style = MoeTypography.caption,
                        color = colors.textTertiary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

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
private fun EmptyExploreContent(
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Explore public memos",
            style = MoeTypography.title,
            color = colors.textSecondary
        )

        Text(
            text = "Discover what others are sharing",
            style = MoeTypography.body,
            color = colors.textTertiary,
            modifier = Modifier.padding(top = MoeSpacing.sm)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}
