package me.mudkip.moememos.ui.preview.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import me.mudkip.moememos.ui.component.MemosIcon
import me.mudkip.moememos.ui.designsystem.component.MoeAppBar
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.preview.MoeMemosPreviewTheme

/**
 * AddAccountPage Preview
 *
 * 展示添加账户页面的完整内容，包括：
 * - 顶部栏（返回按钮、标题）
 * - 账户类型选择卡片列表
 */

// ==================== Main Preview ====================

/**
 * 添加账户页面预览 - 首次添加账户
 */
@PreviewLightDark
@Composable
fun AddAccountPagePreview_FirstAccount() {
    MoeMemosPreviewTheme {
        AddAccountPageContent(
            title = "Moe Memos",
            showBackButton = false,
            showLocalAccountOption = true
        )
    }
}

/**
 * 添加账户页面预览 - 添加更多账户
 */
@PreviewLightDark
@Composable
fun AddAccountPagePreview_AddAnotherAccount() {
    MoeMemosPreviewTheme {
        AddAccountPageContent(
            title = "Add Account",
            showBackButton = true,
            showLocalAccountOption = true
        )
    }
}

/**
 * 添加账户页面预览 - 已有本地账户
 */
@PreviewLightDark
@Composable
fun AddAccountPagePreview_HasLocalAccount() {
    MoeMemosPreviewTheme {
        AddAccountPageContent(
            title = "Add Account",
            showBackButton = true,
            showLocalAccountOption = false
        )
    }
}

// ==================== Page Content ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddAccountPageContent(
    title: String,
    showBackButton: Boolean,
    showLocalAccountOption: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors

    Scaffold(
        containerColor = colors.bgApp,
        topBar = {
            MoeAppBar(
                title = title,
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = { }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = MoeSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(MoeSpacing.md),
            contentPadding = PaddingValues(vertical = MoeSpacing.md),
        ) {
            if (showLocalAccountOption) {
                item {
                    MoeCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { },
                        contentPadding = PaddingValues(0.dp),
                        containerColor = colors.bgSurface,
                    ) {
                        ListItem(
                            headlineContent = {
                                Text("Add Local Account", style = MoeTypography.title)
                            },
                            supportingContent = {
                                Text(
                                    "Store data locally on your device",
                                    style = MoeTypography.body,
                                    color = colors.textSecondary
                                )
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.Home,
                                    contentDescription = null,
                                    tint = colors.accentPrimary
                                )
                            },
                        )
                    }
                }
            }

            item {
                MoeCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { },
                    contentPadding = PaddingValues(0.dp),
                    containerColor = colors.bgSurface,
                ) {
                    ListItem(
                        headlineContent = {
                            Text("Add Memos Account", style = MoeTypography.title)
                        },
                        supportingContent = {
                            Text(
                                "Connect to a Memos server to sync your data",
                                style = MoeTypography.body,
                                color = colors.textSecondary
                            )
                        },
                        leadingContent = {
                            Icon(
                                imageVector = MemosIcon,
                                contentDescription = null,
                                tint = colors.accentPrimary
                            )
                        }
                    )
                }
            }
        }
    }
}
