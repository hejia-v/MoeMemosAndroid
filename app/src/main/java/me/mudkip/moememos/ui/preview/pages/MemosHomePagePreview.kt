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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import me.mudkip.moememos.data.local.entity.MemoEntity
import me.mudkip.moememos.ui.component.MemoContent
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.preview.MoeMemosPreviewTheme
import me.mudkip.moememos.ui.preview.PreviewData
import me.mudkip.moememos.ui.preview.PreviewStates
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Memos Home Page Preview
 *
 * 展示主页的完整内容，包括：
 * - 顶部栏（菜单按钮、标题、搜索按钮）
 * - 备忘录列表
 * - 悬浮操作按钮
 */

// ==================== Preview Data ====================

private val sampleMemos: List<MemoEntity>
    get() = List(5) { index ->
        PreviewData.sampleMemo.copy(
            identifier = "memo-$index",
            content = when (index) {
                0 -> "# Important Note\n\nThis is a pinned memo with **bold text** and *italic*."
                1 -> "Today's tasks:\n- Review code\n- Write documentation\n- Test features"
                2 -> "Meeting notes from yesterday's discussion about project timeline."
                3 -> "Quick reminder: Call the client tomorrow morning."
                else -> "Just a random thought that crossed my mind..."
            },
            pinned = index == 0
        )
    }

// ==================== Main Preview ====================

/**
 * 主页预览 - 有数据状态
 */
@PreviewLightDark
@Composable
fun MemosHomePagePreview_DataState() {
    MoeMemosPreviewTheme {
        MemosHomePageContent(
            memos = sampleMemos,
            memoCount = sampleMemos.size
        )
    }
}

/**
 * 主页预览 - 空状态
 */
@PreviewLightDark
@Composable
fun MemosHomePagePreview_EmptyState() {
    MoeMemosPreviewTheme {
        MemosHomePageContent(
            memos = emptyList(),
            memoCount = 0
        )
    }
}

/**
 * 主页预览 - 单条数据
 */
@PreviewLightDark
@Composable
fun MemosHomePagePreview_SingleItem() {
    MoeMemosPreviewTheme {
        MemosHomePageContent(
            memos = listOf(PreviewData.sampleMemo),
            memoCount = 1
        )
    }
}

// ==================== Page Content ====================

@Composable
private fun MemosHomePageContent(
    memos: List<MemoEntity>,
    memoCount: Int,
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors
    val topScrimColor = Color(0xFFFAF9F6)

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                modifier = Modifier.shadow(22.dp, CircleShape),
                shape = CircleShape,
                containerColor = Color(0xFFF7C41D),
                contentColor = Color(0xFF3A2A00),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(colors.bgApp)
        ) {
            // Background
            HomeBackground()

            // Content List
            if (memos.isEmpty()) {
                EmptyContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding
                ) {
                    // Header spacer
                    item {
                        Spacer(
                            modifier = Modifier
                                .statusBarsPadding()
                                .height(64.dp)
                        )
                    }

                    // Memo cards
                    items(memos) { memo ->
                        MemoCardItem(memo = memo)
                    }
                }
            }

            // Status bar scrim
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(32.dp)
                    .align(Alignment.TopCenter)
                    .background(topScrimColor)
            )

            // Gradient fade
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(96.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                topScrimColor,
                                topScrimColor.copy(alpha = 0.98f),
                                topScrimColor.copy(alpha = 0.86f),
                                topScrimColor.copy(alpha = 0.62f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Header
            HomeHeader(
                memoCount = memoCount,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

// ==================== Components ====================

@Composable
private fun HomeBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFAF9F6),
                        Color(0xFFF7F5F0),
                        Color(0xFFF3F0EB)
                    )
                )
            )
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .size(360.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.94f),
                        Color.White.copy(alpha = 0.20f),
                        Color.Transparent
                    )
                )
            )
    )
}

@Composable
private fun HomeHeader(
    memoCount: Int,
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors

    Row(
        modifier = modifier
            .statusBarsPadding()
            .padding(top = MoeSpacing.sm, bottom = MoeSpacing.md)
            .padding(horizontal = MoeSpacing.xl)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Menu button
        HeaderIconButton(
            onClick = { }
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menu",
                tint = colors.textPrimary
            )
        }

        // Title and count
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = MoeSpacing.md)
        ) {
            Text(
                text = "Memos",
                style = MoeTypography.title,
                color = colors.textPrimary
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 3.dp)
            ) {
                HomeInlineStat(value = memoCount.toString())
            }
        }

        // Search button
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = colors.bgSurface.copy(alpha = 0.95f),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = MoeSpacing.xs, vertical = MoeSpacing.xs),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeaderIconButton(
                    onClick = { },
                    tonal = true
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = colors.textPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderIconButton(
    onClick: () -> Unit,
    tonal: Boolean = false,
    icon: @Composable () -> Unit
) {
    val colors = MoeDesignTokens.colors

    Box(
        modifier = Modifier
            .size(44.dp)
            .then(
                if (tonal) Modifier else Modifier.shadow(4.dp, CircleShape)
            )
            .clip(CircleShape)
            .background(if (tonal) colors.bgOverlay else colors.bgSurface.copy(alpha = 0.96f)),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}

@Composable
private fun HomeInlineStat(value: String) {
    val colors = MoeDesignTokens.colors

    Row(
        modifier = Modifier.padding(end = MoeSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .clip(CircleShape)
                .background(colors.textTertiary.copy(alpha = 0.7f))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            style = MoeTypography.caption,
            color = colors.textPrimary
        )
    }
}

@Composable
private fun MemoCardItem(
    memo: MemoEntity,
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors

    MoeCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MoeSpacing.xl, vertical = MoeSpacing.sm),
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

@Composable
private fun EmptyContent(
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "No memos yet",
            style = MoeTypography.title,
            color = colors.textSecondary
        )

        Text(
            text = "Tap + to create your first memo",
            style = MoeTypography.body,
            color = colors.textTertiary,
            modifier = Modifier.padding(top = MoeSpacing.sm)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}
