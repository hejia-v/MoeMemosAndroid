package me.mudkip.moememos.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.preview.pages.*

/**
 * Page Preview Catalog - Central place to view all page previews.
 *
 * Open this file and use the Split/Design view to see all page previews at once.
 * This catalog is organized by page categories:
 *
 * 1. Memo Pages - Pages related to viewing and managing memos
 * 2. Account Pages - Pages for account management and authentication
 * 3. Other Pages - Settings, resources, and other utility pages
 */

// ==================== Section Title ====================

@Composable
private fun CatalogSectionTitle(title: String) {
    val colors = MoeDesignTokens.colors
    Text(
        text = title,
        style = MoeTypography.headline,
        color = colors.textPrimary,
        modifier = Modifier.padding(vertical = MoeSpacing.lg)
    )
}

@Composable
private fun PagePreviewLabel(label: String) {
    val colors = MoeDesignTokens.colors
    Text(
        text = label,
        style = MoeTypography.label,
        color = colors.textSecondary,
        modifier = Modifier.padding(vertical = MoeSpacing.sm)
    )
}

/**
 * A wrapper to provide a fixed height for page previews.
 * This resolves the IllegalStateException: Vertically scrollable component was measured with an infinity maximum height constraints.
 */
@Composable
private fun PreviewWrapper(content: @Composable () -> Unit) {
    val colors = MoeDesignTokens.colors
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(640.dp)
            .clip(RoundedCornerShape(MoeSpacing.md))
            .border(1.dp, colors.strokeSubtle, RoundedCornerShape(MoeSpacing.md))
    ) {
        content()
    }
}

// ==================== Main Catalog ====================

@PreviewLightDark
@Composable
fun AllPagesPreviewCatalog() {
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
            // Title
            Text(
                text = "Page Preview Catalog",
                style = MoeTypography.display,
                color = colors.textPrimary
            )
            Text(
                text = "This catalog displays all app pages in a single view for easy reference.",
                style = MoeTypography.body,
                color = colors.textSecondary
            )

            Spacer(modifier = Modifier.height(MoeSpacing.xl))

            // ==================== MEMO PAGES ====================
            CatalogSectionTitle("📝 Memo Pages")

            // Memos Home Page
            PagePreviewLabel("MemosHomePage - Data State")
            PreviewWrapper { MemosHomePagePreview_DataState() }

            PagePreviewLabel("MemosHomePage - Empty State")
            PreviewWrapper { MemosHomePagePreview_EmptyState() }

            // Archived Memo Page
            PagePreviewLabel("ArchivedMemoPage - Data State")
            PreviewWrapper { ArchivedMemoPagePreview_DataState() }

            PagePreviewLabel("ArchivedMemoPage - Empty State")
            PreviewWrapper { ArchivedMemoPagePreview_EmptyState() }

            // Search Page
            PagePreviewLabel("SearchPage - With Results")
            PreviewWrapper { SearchPagePreview_WithResults() }

            PagePreviewLabel("SearchPage - No Results")
            PreviewWrapper { SearchPagePreview_NoResults() }

            // Tag Memo Page
            PagePreviewLabel("TagMemoPage - With Memos")
            PreviewWrapper { TagMemoPagePreview_WithMemos() }

            PagePreviewLabel("TagMemoPage - Empty Tag")
            PreviewWrapper { TagMemoPagePreview_EmptyTag() }

            // Explore Page
            PagePreviewLabel("ExplorePage - With Public Memos")
            PreviewWrapper { ExplorePagePreview_WithPublicMemos() }

            PagePreviewLabel("ExplorePage - Empty State")
            PreviewWrapper { ExplorePagePreview_EmptyState() }

            // Memo Input Page
            PagePreviewLabel("MemoInputPage - New Memo (Empty)")
            PreviewWrapper { MemoInputPagePreview_NewMemoEmpty() }

            PagePreviewLabel("MemoInputPage - New Memo (With Content)")
            PreviewWrapper { MemoInputPagePreview_NewMemoWithContent() }

            PagePreviewLabel("MemoInputPage - Edit Mode")
            PreviewWrapper { MemoInputPagePreview_EditMode() }

            // ==================== ACCOUNT PAGES ====================
            CatalogSectionTitle("👤 Account Pages")

            // Add Account Page
            PagePreviewLabel("AddAccountPage - First Account")
            PreviewWrapper { AddAccountPagePreview_FirstAccount() }

            PagePreviewLabel("AddAccountPage - Add Another Account")
            PreviewWrapper { AddAccountPagePreview_AddAnotherAccount() }

            // Login Page
            PagePreviewLabel("LoginPage - Empty Form")
            PreviewWrapper { LoginPagePreview_EmptyForm() }

            PagePreviewLabel("LoginPage - With Input")
            PreviewWrapper { LoginPagePreview_WithInput() }

            // Account Page
            PagePreviewLabel("AccountPage - Local Account")
            PreviewWrapper { AccountPagePreview_LocalAccount() }

            PagePreviewLabel("AccountPage - Memos Account")
            PreviewWrapper { AccountPagePreview_MemosAccount() }

            // ==================== OTHER PAGES ====================
            CatalogSectionTitle("⚙️ Other Pages")

            // Settings Page
            PagePreviewLabel("SettingsPage - Single Account")
            PreviewWrapper { SettingsPagePreview_SingleAccount() }

            PagePreviewLabel("SettingsPage - Multiple Accounts")
            PreviewWrapper { SettingsPagePreview_MultipleAccounts() }

            // Resource List Page
            PagePreviewLabel("ResourceListPage - Images Tab")
            PreviewWrapper { ResourceListPagePreview_ImagesTab() }

            PagePreviewLabel("ResourceListPage - Other Tab")
            PreviewWrapper { ResourceListPagePreview_OtherTab() }

            PagePreviewLabel("ResourceListPage - Empty State")
            PreviewWrapper { ResourceListPagePreview_EmptyState() }

            // Footer
            Spacer(modifier = Modifier.height(MoeSpacing.xxxl))
            Text(
                text = "End of Page Preview Catalog",
                style = MoeTypography.caption,
                color = colors.textTertiary,
                modifier = Modifier.padding(vertical = MoeSpacing.xl)
            )
        }
    }
}
