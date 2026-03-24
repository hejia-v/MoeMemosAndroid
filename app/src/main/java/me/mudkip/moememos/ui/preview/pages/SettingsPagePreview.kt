package me.mudkip.moememos.ui.preview.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Source
import androidx.compose.material.icons.outlined.Web
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import me.mudkip.moememos.ui.component.MemosIcon
import me.mudkip.moememos.ui.designsystem.component.MoeAppBar
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.preview.MoeMemosPreviewTheme

/**
 * SettingsPage Preview
 *
 * 展示设置页面的完整内容，包括：
 * - 顶部栏（返回按钮、标题）
 * - 当前账户卡片
 * - 账户列表
 * - 偏好设置
 * - 关于信息
 */

// ==================== Main Preview ====================

/**
 * 设置页面预览 - 单账户（本地账户）
 */
@PreviewLightDark
@Composable
fun SettingsPagePreview_SingleAccount() {
    MoeMemosPreviewTheme {
        SettingsPageContent(
            currentAccountName = "Local Account",
            accounts = listOf(AccountInfo("Local Account", AccountType.LOCAL, true)),
            themeMode = "System",
            editGesture = "Double tap"
        )
    }
}

/**
 * 设置页面预览 - 多账户
 */
@PreviewLightDark
@Composable
fun SettingsPagePreview_MultipleAccounts() {
    MoeMemosPreviewTheme {
        SettingsPageContent(
            currentAccountName = "demo_user",
            accounts = listOf(
                AccountInfo("demo_user", AccountType.MEMOS, true),
                AccountInfo("work_user", AccountType.MEMOS, false),
                AccountInfo("Local Account", AccountType.LOCAL, false)
            ),
            themeMode = "Dark",
            editGesture = "None"
        )
    }
}

/**
 * 设置页面预览 - 本地账户
 */
@PreviewLightDark
@Composable
fun SettingsPagePreview_LocalAccountOnly() {
    MoeMemosPreviewTheme {
        SettingsPageContent(
            currentAccountName = "Local Account",
            accounts = listOf(AccountInfo("Local Account", AccountType.LOCAL, true)),
            themeMode = "Light",
            editGesture = "Long press"
        )
    }
}

// ==================== Data ====================

private enum class AccountType {
    LOCAL,
    MEMOS
}

private data class AccountInfo(
    val name: String,
    val type: AccountType,
    val isCurrent: Boolean
)

// ==================== Page Content ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsPageContent(
    currentAccountName: String,
    accounts: List<AccountInfo>,
    themeMode: String,
    editGesture: String,
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors

    Scaffold(
        containerColor = colors.bgApp,
        topBar = {
            MoeAppBar(
                title = "Settings",
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                start = MoeSpacing.xl,
                top = innerPadding.calculateTopPadding() + MoeSpacing.sm,
                end = MoeSpacing.xl,
                bottom = innerPadding.calculateBottomPadding() + MoeSpacing.xxxl,
            )
        ) {
            // Current account card
            item {
                MoeCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = MoeSpacing.md),
                    contentPadding = PaddingValues(
                        horizontal = MoeSpacing.xl,
                        vertical = MoeSpacing.lg,
                    ),
                    containerColor = colors.bgSurface,
                ) {
                    Text(
                        text = currentAccountName,
                        style = MoeTypography.title,
                        color = colors.textPrimary,
                    )
                    Text(
                        text = "Settings",
                        style = MoeTypography.body,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(top = MoeSpacing.xs),
                    )
                }
            }

            // Accounts section
            item {
                SettingsSectionTitle(text = "Accounts")
            }

            items(accounts) { account ->
                SettingItem(
                    icon = when (account.type) {
                        AccountType.LOCAL -> Icons.Outlined.Home
                        AccountType.MEMOS -> MemosIcon
                    },
                    text = account.name,
                    trailingIcon = {
                        if (account.isCurrent) {
                            SelectedIndicator()
                        }
                    }
                ) { }
            }

            // Add account item
            item {
                SettingItem(
                    icon = Icons.Outlined.PersonAdd,
                    text = "Add Account"
                ) { }
            }

            // Preferences section
            item {
                SettingsSectionTitle(text = "Preferences")
            }

            item {
                SettingItem(
                    icon = Icons.Outlined.DarkMode,
                    text = "Dark Mode",
                    trailingIcon = {
                        Text(
                            text = themeMode,
                            color = colors.textSecondary,
                            style = MoeTypography.label,
                        )
                    }
                ) { }
            }

            item {
                SettingItem(
                    icon = Icons.Outlined.Edit,
                    text = "Edit Gesture",
                    trailingIcon = {
                        Text(
                            text = editGesture,
                            color = colors.textSecondary,
                            style = MoeTypography.label,
                        )
                    }
                ) { }
            }

            // About section
            item {
                SettingsSectionTitle(text = "About")
            }

            item {
                SettingItem(
                    icon = Icons.Outlined.Web,
                    text = "Website"
                ) { }
            }

            item {
                SettingItem(
                    icon = Icons.Outlined.Lock,
                    text = "Privacy Policy"
                ) { }
            }

            item {
                SettingItem(
                    icon = Icons.Outlined.Source,
                    text = "Acknowledgements"
                ) { }
            }

            item {
                SettingItem(
                    icon = Icons.Outlined.BugReport,
                    text = "Report an Issue"
                ) { }
            }
        }
    }
}

// ==================== Components ====================

@Composable
private fun SettingsSectionTitle(text: String) {
    val colors = MoeDesignTokens.colors

    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = MoeSpacing.lg, bottom = MoeSpacing.xs),
        style = MoeTypography.label,
        color = colors.textSecondary
    )
}

@Composable
private fun SettingItem(
    icon: ImageVector,
    text: String,
    trailingIcon: @Composable () -> Unit = { },
    onClick: () -> Unit
) {
    val colors = MoeDesignTokens.colors

    MoeCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MoeSpacing.xs)
            .clickable { onClick() },
        containerColor = colors.bgSurface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MoeSpacing.lg),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.accentPrimary,
                modifier = Modifier.padding(end = MoeSpacing.md)
            )
            Text(
                text = text,
                style = MoeTypography.body,
                color = colors.textPrimary,
                modifier = Modifier.weight(1f)
            )
            trailingIcon()
        }
    }
}

@Composable
private fun SelectedIndicator() {
    val colors = MoeDesignTokens.colors

    Icon(
        imageVector = Icons.Outlined.Check,
        contentDescription = "Selected",
        modifier = Modifier.padding(start = MoeSpacing.lg),
        tint = colors.textSecondary,
    )
}
