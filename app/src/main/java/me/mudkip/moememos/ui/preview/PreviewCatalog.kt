package me.mudkip.moememos.ui.preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Attachment
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import me.mudkip.moememos.R
import me.mudkip.moememos.data.model.DailyUsageStat
import me.mudkip.moememos.ui.component.HeatmapStat
import me.mudkip.moememos.ui.component.SyncStatusBadge
import me.mudkip.moememos.ui.designsystem.component.MoeAppBar
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.component.MoeInputField
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeRadius
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Preview Catalog - Central place to view all UI components.
 * Open this file and use the Split/Design view to see all components at once.
 */
// ==================== Component Content Cards ====================

@Composable
private fun CatalogSectionTitle(title: String) {
    val colors = MoeDesignTokens.colors
    Text(
        text = title,
        style = MoeTypography.label,
        color = colors.textSecondary,
        modifier = Modifier.padding(vertical = MoeSpacing.sm)
    )
}

@Composable
private fun CatalogCard(
    title: String,
    content: @Composable () -> Unit
) {
    val colors = MoeDesignTokens.colors
    MoeCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(MoeSpacing.lg),
        containerColor = colors.bgSurface
    ) {
        Column {
            Text(
                text = title,
                style = MoeTypography.title,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(MoeSpacing.md))
            content()
        }
    }
}

// ==================== Individual Component Previews ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MoeAppBarPreviewContent() {
    MoeAppBar(
        title = "MoeMemos",
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            }
            IconButton(onClick = {}) {
                Icon(Icons.Filled.MoreVert, contentDescription = "More")
            }
        }
    )
}

@Composable
private fun MoeCardPreviewContent() {
    val colors = MoeDesignTokens.colors
    MoeCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(MoeSpacing.lg),
        containerColor = colors.bgElevated
    ) {
        Text(
            text = "Card Content",
            style = MoeTypography.body,
            color = colors.textPrimary
        )
    }
}

@Composable
private fun MoeInputFieldPreviewContent() {
    var text by remember { mutableStateOf("") }
    MoeInputField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier.fillMaxWidth(),
        label = "Input",
        placeholder = "Type something..."
    )
}

@Composable
private fun SyncStatusBadgeRow() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(MoeSpacing.md)
    ) {
        Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
            SyncStatusBadge(syncing = false, unsyncedCount = 0, onSync = {})
            Text("Idle", style = MoeTypography.caption)
        }
        Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
            SyncStatusBadge(syncing = true, unsyncedCount = 0, onSync = {})
            Text("Syncing", style = MoeTypography.caption)
        }
        Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
            SyncStatusBadge(syncing = false, unsyncedCount = 5, onSync = {})
            Text("Unsynced", style = MoeTypography.caption)
        }
    }
}

@Composable
private fun HeatmapStatRow() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(MoeSpacing.xs)
    ) {
        repeat(7) { i ->
            Box(modifier = Modifier.size(20.dp)) {
                HeatmapStat(day = DailyUsageStat(date = LocalDate.now().minusDays(i.toLong()), count = i))
            }
        }
    }
}

@Composable
private fun AttachmentChipPreview() {
    val colors = MoeDesignTokens.colors
    AssistChip(
        onClick = {},
        shape = RoundedCornerShape(MoeSpacing.md),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = colors.bgSurface,
            labelColor = colors.textPrimary,
            leadingIconContentColor = colors.accentPrimary,
        ),
        label = {
            Text(
                text = "document.pdf",
                style = MoeTypography.label,
            )
        },
        leadingIcon = {
            Icon(
                Icons.Outlined.Attachment,
                contentDescription = stringResource(R.string.attachment),
                Modifier.size(AssistChipDefaults.IconSize)
            )
        }
    )
}

// ==================== Main Catalog Preview ====================

/**
 * Simplified Memo Card for preview.
 * Shows the visual style of a memo card without ViewModel dependencies.
 */
@Composable
private fun MemoCardPreview(
    title: String,
    content: String,
    date: Instant,
    isPinned: Boolean = false,
    showSyncStatus: Boolean = false,
    needsSync: Boolean = false
) {
    val colors = MoeDesignTokens.colors
    val dateFormatter = remember {
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    }

    MoeCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(MoeSpacing.lg),
        containerColor = colors.bgElevated,
        border = if (isPinned) {
            BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        } else {
            null
        }
    ) {
        Column {
            // Header row with date and status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dateFormatter.format(date.atZone(ZoneId.systemDefault()).toLocalDate()),
                    style = MoeTypography.caption,
                    color = colors.textTertiary
                )
                if (showSyncStatus && needsSync) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Outlined.CloudOff,
                        contentDescription = "Sync pending",
                        modifier = Modifier
                            .padding(start = MoeSpacing.sm)
                            .size(16.dp),
                        tint = colors.accentDanger
                    )
                }
                if (isPinned) {
                    Text(
                        text = "📌 Pinned",
                        style = MoeTypography.caption,
                        color = colors.accentPrimary,
                        modifier = Modifier.padding(start = MoeSpacing.sm)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More",
                    tint = colors.textSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Content
            Text(
                text = content,
                style = MoeTypography.body,
                color = colors.textPrimary,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = MoeSpacing.md)
            )
        }
    }
}

@Composable
private fun MemoCardListPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(MoeSpacing.md)
    ) {
        // Pinned memo
        MemoCardPreview(
            title = "Important Note",
            content = "This is a pinned memo with some important information that I want to keep at the top.",
            date = Instant.now().minusSeconds(3600),
            isPinned = true
        )

        // Regular memo
        MemoCardPreview(
            title = "Daily Log",
            content = "Today I worked on adding preview support to the MoeMemos Android app. The goal is to make it easier to visualize and edit UI components.",
            date = Instant.now().minusSeconds(86400)
        )

        // Memo with sync pending
        MemoCardPreview(
            title = "Draft",
            content = "This memo is still pending sync to the server.",
            date = Instant.now(),
            showSyncStatus = true,
            needsSync = true
        )

        // Long memo
        MemoCardPreview(
            title = "Long Content",
            content = "This is a very long memo that demonstrates how the card handles text overflow. The content should be truncated after 5 lines with an ellipsis. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            date = Instant.now().minusSeconds(172800)
        )
    }
}

@PreviewLightDark
@Composable
fun AllComponentsPreview() {
    MoeMemosPreviewTheme {
        val colors = MoeDesignTokens.colors
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.bgApp)
                .verticalScroll(rememberScrollState())
                .padding(MoeSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(MoeSpacing.xl)
        ) {
            // AppBar Section
            CatalogSectionTitle("AppBar")
            CatalogCard("MoeAppBar") {
                MoeAppBarPreviewContent()
            }

            // Memo Cards Section (主页卡片)
            CatalogSectionTitle("Memo Cards (主页卡片)")
            MemoCardListPreview()

            // Card Section
            CatalogSectionTitle("Cards")
            CatalogCard("MoeCard") {
                MoeCardPreviewContent()
            }

            // Input Section
            CatalogSectionTitle("Inputs")
            CatalogCard("MoeInputField") {
                MoeInputFieldPreviewContent()
            }

            // Status Section
            CatalogSectionTitle("Status Badges")
            CatalogCard("Sync Status") {
                SyncStatusBadgeRow()
            }

            // Heatmap Section
            CatalogSectionTitle("Heatmap")
            CatalogCard("Activity Stats") {
                HeatmapStatRow()
            }

            // Chips Section
            CatalogSectionTitle("Chips")
            CatalogCard("Attachment Chip") {
                AttachmentChipPreview()
            }

            Spacer(modifier = Modifier.height(MoeSpacing.xxxl))
        }
    }
}
