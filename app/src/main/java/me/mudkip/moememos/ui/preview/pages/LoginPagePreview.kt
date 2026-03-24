package me.mudkip.moememos.ui.preview.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.PermIdentity
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import me.mudkip.moememos.ui.designsystem.component.MoeAppBar
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.component.MoeInputField
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeRadius
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.preview.MoeMemosPreviewTheme

/**
 * LoginPage Preview
 *
 * 展示登录页面的完整内容，包括：
 * - 顶部栏（返回按钮、标题）
 * - 登录表单卡片（Host、Access Token 输入框）
 * - 底部栏（登录按钮）
 */

// ==================== Main Preview ====================

/**
 * 登录页面预览 - 空表单（首次登录）
 */
@PreviewLightDark
@Composable
fun LoginPagePreview_EmptyForm() {
    MoeMemosPreviewTheme {
        LoginPageContent(
            title = "Moe Memos",
            showBackButton = false,
            host = TextFieldValue(""),
            accessToken = TextFieldValue("")
        )
    }
}

/**
 * 登录页面预览 - 已填写信息
 */
@PreviewLightDark
@Composable
fun LoginPagePreview_WithInput() {
    MoeMemosPreviewTheme {
        LoginPageContent(
            title = "Add Account",
            showBackButton = true,
            host = TextFieldValue("https://demo.memos.moe"),
            accessToken = TextFieldValue("pat_xxxxxxxxxxxxxxxx")
        )
    }
}

/**
 * 登录页面预览 - 添加账户模式
 */
@PreviewLightDark
@Composable
fun LoginPagePreview_AddAccountMode() {
    MoeMemosPreviewTheme {
        LoginPageContent(
            title = "Add Account",
            showBackButton = true,
            host = TextFieldValue(""),
            accessToken = TextFieldValue("")
        )
    }
}

// ==================== Page Content ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginPageContent(
    title: String,
    showBackButton: Boolean,
    host: TextFieldValue,
    accessToken: TextFieldValue,
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors
    var currentHost by remember { mutableStateOf(host) }
    var currentAccessToken by remember { mutableStateOf(accessToken) }

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
        },
        bottomBar = {
            BottomAppBar(
                containerColor = colors.bgOverlay,
                tonalElevation = 0.dp,
                actions = {},
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        onClick = { },
                        containerColor = colors.accentPrimary,
                        contentColor = colors.textOnAccent,
                        text = { Text("Add Account") },
                        icon = {
                            Icon(
                                Icons.AutoMirrored.Outlined.Login,
                                contentDescription = "Login"
                            )
                        }
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(horizontal = MoeSpacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MoeCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(MoeSpacing.xl),
                containerColor = colors.bgSurface,
            ) {
                Column {
                    Text(
                        text = title,
                        style = MoeTypography.headline,
                        color = colors.textPrimary,
                    )
                    Text(
                        text = "Enter your login information to connect to a Memos server",
                        style = MoeTypography.body,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(top = MoeSpacing.sm, bottom = MoeSpacing.xl)
                    )

                    MoeInputField(
                        modifier = Modifier.fillMaxWidth(),
                        value = currentHost,
                        onValueChange = { currentHost = it },
                        label = "Host",
                        placeholder = "https://demo.memos.moe",
                        maxLines = 1
                    )

                    MoeInputField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = MoeSpacing.lg),
                        value = currentAccessToken,
                        onValueChange = { currentAccessToken = it },
                        label = "Access Token",
                        placeholder = "pat_xxx",
                        maxLines = 1,
                        visualTransformation = PasswordVisualTransformation()
                    )

                    // Normalized host display
                    Text(
                        text = currentHost.text.ifBlank { "https://" },
                        style = MoeTypography.caption,
                        color = colors.textTertiary,
                        modifier = Modifier
                            .padding(top = MoeSpacing.lg)
                            .background(colors.bgElevated, MoeRadius.shapeMd)
                            .padding(horizontal = MoeSpacing.md, vertical = MoeSpacing.sm)
                    )

                    // Field labels
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = MoeSpacing.lg),
                        horizontalArrangement = Arrangement.spacedBy(MoeSpacing.sm)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Computer,
                            contentDescription = null,
                            tint = colors.textSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Host",
                            style = MoeTypography.caption,
                            color = colors.textSecondary,
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = MoeSpacing.sm),
                        horizontalArrangement = Arrangement.spacedBy(MoeSpacing.sm)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PermIdentity,
                            contentDescription = null,
                            tint = colors.textSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Access Token",
                            style = MoeTypography.caption,
                            color = colors.textSecondary,
                        )
                    }
                }
            }
        }
    }
}
