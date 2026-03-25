package me.mudkip.moememos.ui.preview.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import me.mudkip.moememos.data.local.entity.ResourceEntity
import me.mudkip.moememos.ui.designsystem.component.MoeAppBar
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.preview.MoeMemosPreviewTheme
import java.time.Instant

/**
 * ResourceListPage Preview
 *
 * 展示资源列表页面的完整内容，包括：
 * - 顶部栏（返回按钮、标题）
 * - 统计卡片
 * - 标签切换按钮
 * - 资源网格/列表
 */

// ==================== Preview Data ====================

private data class PreviewResource(
    val identifier: String,
    val filename: String,
    val mimeType: String
)

private val imageResources: List<PreviewResource>
    get() = listOf(
        PreviewResource("img-1", "screenshot_1.png", "image/png"),
        PreviewResource("img-2", "photo_2024.jpg", "image/jpeg"),
        PreviewResource("img-3", "diagram.png", "image/png"),
        PreviewResource("img-4", "profile.jpg", "image/jpeg"),
        PreviewResource("img-5", "document_scan.png", "image/png"),
        PreviewResource("img-6", "whiteboard.jpg", "image/jpeg")
    )

private val otherResources: List<PreviewResource>
    get() = listOf(
        PreviewResource("doc-1", "meeting_notes.pdf", "application/pdf"),
        PreviewResource("doc-2", "report.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        PreviewResource("doc-3", "data.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    )

// ==================== Main Preview ====================

/**
 * 资源列表页面预览 - 图片标签页
 */
@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun ResourceListPagePreview_ImagesTab() {
    MoeMemosPreviewTheme {
        ResourceListPageContent(
            selectedFilter = ResourceFilter.IMAGE,
            imageCount = imageResources.size,
            otherCount = otherResources.size
        )
    }
}

/**
 * 资源列表页面预览 - 其他文件标签页
 */
@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun ResourceListPagePreview_OtherTab() {
    MoeMemosPreviewTheme {
        ResourceListPageContent(
            selectedFilter = ResourceFilter.OTHER,
            imageCount = imageResources.size,
            otherCount = otherResources.size
        )
    }
}

/**
 * 资源列表页面预览 - 空状态
 */
@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun ResourceListPagePreview_EmptyState() {
    MoeMemosPreviewTheme {
        ResourceListPageContent(
            selectedFilter = ResourceFilter.IMAGE,
            imageCount = 0,
            otherCount = 0
        )
    }
}

// ==================== Page Content ====================

private enum class ResourceFilter {
    IMAGE,
    OTHER
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ResourceListPageContent(
    selectedFilter: ResourceFilter,
    imageCount: Int,
    otherCount: Int,
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors
    var currentFilter by remember { mutableStateOf(selectedFilter) }
    val currentCount = if (currentFilter == ResourceFilter.IMAGE) imageCount else otherCount

    Scaffold(
        containerColor = colors.bgApp,
        topBar = {
            MoeAppBar(
                title = "Resources",
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Stats card
            MoeCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MoeSpacing.xl, vertical = MoeSpacing.md),
                contentPadding = PaddingValues(MoeSpacing.lg),
                containerColor = colors.bgSurface,
            ) {
                Text(
                    text = "Resources",
                    style = MoeTypography.title,
                    color = colors.textPrimary,
                )
                Text(
                    text = currentCount.toString(),
                    style = MoeTypography.display,
                    color = colors.accentPrimary,
                    modifier = Modifier.padding(top = MoeSpacing.xs),
                )

                // Segmented buttons
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MoeSpacing.lg)
                ) {
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                        selected = currentFilter == ResourceFilter.IMAGE,
                        onClick = { currentFilter = ResourceFilter.IMAGE },
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = colors.accentSoft,
                            activeContentColor = colors.accentPrimary,
                            inactiveContainerColor = colors.bgElevated,
                            inactiveContentColor = colors.textSecondary,
                            activeBorderColor = colors.strokeSubtle,
                            inactiveBorderColor = colors.strokeSubtle,
                        ),
                        label = { Text("Image") }
                    )
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                        selected = currentFilter == ResourceFilter.OTHER,
                        onClick = { currentFilter = ResourceFilter.OTHER },
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = colors.accentSoft,
                            activeContentColor = colors.accentPrimary,
                            inactiveContainerColor = colors.bgElevated,
                            inactiveContentColor = colors.textSecondary,
                            activeBorderColor = colors.strokeSubtle,
                            inactiveBorderColor = colors.strokeSubtle,
                        ),
                        label = { Text("Other") }
                    )
                }
            }

            // Content based on filter
            if (currentCount == 0) {
                EmptyResourceContent(
                    filter = currentFilter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else if (currentFilter == ResourceFilter.IMAGE) {
                // Image grid
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = MoeSpacing.xl),
                    horizontalArrangement = Arrangement.spacedBy(MoeSpacing.md),
                    verticalItemSpacing = MoeSpacing.md,
                    contentPadding = PaddingValues(bottom = MoeSpacing.xl)
                ) {
                    items(imageResources) { resource ->
                        ImageResourceCard(resource = resource)
                    }
                }
            } else {
                // Other files list placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(MoeSpacing.xl),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Document files will appear here",
                        style = MoeTypography.body,
                        color = colors.textSecondary
                    )
                }
            }
        }
    }
}

// ==================== Components ====================

@Composable
private fun ImageResourceCard(
    resource: PreviewResource,
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colors.bgElevated),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.accentSoft),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = resource.filename.substringAfterLast(".").uppercase(),
                    style = MoeTypography.caption,
                    color = colors.accentPrimary
                )
            }
            Text(
                text = resource.filename,
                style = MoeTypography.caption,
                color = colors.textSecondary,
                modifier = Modifier.padding(top = MoeSpacing.sm)
            )
        }
    }
}

@Composable
private fun EmptyResourceContent(
    filter: ResourceFilter,
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = if (filter == ResourceFilter.IMAGE) "No images" else "No other files",
            style = MoeTypography.title,
            color = colors.textSecondary
        )

        Text(
            text = "Resources will appear here when you add them to memos",
            style = MoeTypography.body,
            color = colors.textTertiary,
            modifier = Modifier.padding(top = MoeSpacing.sm)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}
