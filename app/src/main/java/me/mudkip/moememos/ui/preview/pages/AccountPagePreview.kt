package me.mudkip.moememos.ui.preview.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import me.mudkip.moememos.ui.component.MemosIcon
import me.mudkip.moememos.ui.designsystem.component.MoeAppBar
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.preview.MoeMemosPreviewTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * AccountPage Preview
 *
 * 展示账户页面的完整内容，包括：
 * - 顶部栏（返回按钮、标题）
 * - 账户信息卡片
 * - 操作按钮（切换账户、导出、登出等）
 */

// ==================== Main Preview ====================

/**
 * 账户页面预览 - 本地账户
 */
@PreviewLightDark
@Composable
fun AccountPagePreview_LocalAccount() {
    MoeMemosPreviewTheme {
        LocalAccountPageContent(
            startDate = Instant.now().minusSeconds(31536000), // 1 year ago
            showSwitchAccountButton = true
        )
    }
}

/**
 * 账户页面预览 - Memos 账户
 */
@PreviewLightDark
@Composable
fun AccountPagePreview_MemosAccount() {
    MoeMemosPreviewTheme {
        MemosAccountPageContent(
            username = "demo_user",
            host = "demo.memos.moe",
            startDate = Instant.now().minusSeconds(86400 * 180), // 6 months ago
            showSwitchAccountButton = false
        )
    }
}

/**
 * 账户页面预览 - 当前选中账户（无切换按钮）
 */
@PreviewLightDark
@Composable
fun AccountPagePreview_CurrentAccount() {
    MoeMemosPreviewTheme {
        LocalAccountPageContent(
            startDate = Instant.now().minusSeconds(86400 * 30), // 30 days ago
            showSwitchAccountButton = false
        )
    }
}

// ==================== Page Content ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountPageContainer(
    title: String,
    content: @Composable () -> Unit
) {
    val colors = MoeDesignTokens.colors

    Scaffold(
        containerColor = colors.bgApp,
        topBar = {
            MoeAppBar(
                title = title,
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = MoeSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(MoeSpacing.md)
        ) {
            content()
        }
    }
}

@Composable
private fun LocalAccountPageContent(
    startDate: Instant,
    showSwitchAccountButton: Boolean
) {
    val colors = MoeDesignTokens.colors

    AccountPageContainer(title = "Local Account") {
        // Account info card
        MoeCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(MoeSpacing.xl),
            containerColor = colors.bgSurface,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = null,
                    tint = colors.accentPrimary,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(MoeSpacing.lg))
                Column {
                    Text(
                        text = "Local Account",
                        style = MoeTypography.title,
                        color = colors.textPrimary
                    )
                    Text(
                        text = "Started ${formatDate(startDate)}",
                        style = MoeTypography.body,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(top = MoeSpacing.xs)
                    )
                }
            }
        }

        // Info card
        MoeCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(MoeSpacing.lg),
            containerColor = colors.bgElevated,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = colors.textSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(MoeSpacing.md))
                Text(
                    text = "Local account stores data only on this device",
                    style = MoeTypography.body,
                    color = colors.textSecondary
                )
            }
        }

        // Action buttons
        if (showSwitchAccountButton) {
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.accentPrimary,
                    contentColor = colors.textOnAccent
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.SwapHoriz,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(MoeSpacing.sm))
                Text("Switch to this account")
            }
        }

        // Export button
        OutlinedButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Outlined.Upload,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(MoeSpacing.sm))
            Text("Export local data")
        }
    }
}

@Composable
private fun MemosAccountPageContent(
    username: String,
    host: String,
    startDate: Instant,
    showSwitchAccountButton: Boolean
) {
    val colors = MoeDesignTokens.colors

    AccountPageContainer(title = "Memos Account") {
        // Account info card
        MoeCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(MoeSpacing.xl),
            containerColor = colors.bgSurface,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = MemosIcon,
                    contentDescription = null,
                    tint = colors.accentPrimary,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(MoeSpacing.lg))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = username,
                        style = MoeTypography.title,
                        color = colors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = host,
                        style = MoeTypography.body,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(top = MoeSpacing.xs),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "Started ${formatDate(startDate)}",
                        style = MoeTypography.caption,
                        color = colors.textTertiary,
                        modifier = Modifier.padding(top = MoeSpacing.xs)
                    )
                }
            }
        }

        // Stats card
        MoeCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(MoeSpacing.lg),
            containerColor = colors.bgSurface,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(label = "Memos", value = "42")
                StatItem(label = "Tags", value = "8")
                StatItem(label = "Resources", value = "15")
            }
        }

        // Action buttons
        if (showSwitchAccountButton) {
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.accentPrimary,
                    contentColor = colors.textOnAccent
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.SwapHoriz,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(MoeSpacing.sm))
                Text("Switch to this account")
            }
        }

        // Sign out button
        OutlinedButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = colors.accentDanger
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Logout,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(MoeSpacing.sm))
            Text("Sign out")
        }
    }
}

// ==================== Components ====================

@Composable
private fun StatItem(
    label: String,
    value: String
) {
    val colors = MoeDesignTokens.colors

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MoeTypography.display,
            color = colors.accentPrimary
        )
        Text(
            text = label,
            style = MoeTypography.caption,
            color = colors.textSecondary
        )
    }
}

private fun formatDate(instant: Instant): String {
    return DateTimeFormatter
        .ofLocalizedDate(FormatStyle.MEDIUM)
        .format(instant.atZone(ZoneId.systemDefault()))
}
