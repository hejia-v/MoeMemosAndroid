package me.mudkip.moememos.ui.preview.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Attachment
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import me.mudkip.moememos.data.model.MemoVisibility
import me.mudkip.moememos.ui.designsystem.component.MoeAppBar
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.component.MoeInputField
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeRadius
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.preview.MoeMemosPreviewTheme

/**
 * MemoInputPage Preview
 *
 * 展示备忘录输入/编辑页面的完整内容，包括：
 * - 顶部栏（关闭按钮、标题、发送按钮）
 * - 输入区域（标签、输入框）
 * - 底部栏（可见性、标签、附件等按钮）
 */

// ==================== Main Preview ====================

/**
 * 输入页面预览 - 新建备忘录（空白）
 */
@PreviewLightDark
@Composable
fun MemoInputPagePreview_NewMemoEmpty() {
    MoeMemosPreviewTheme {
        MemoInputPageContent(
            isEditMode = false,
            text = TextFieldValue(""),
            currentVisibility = MemoVisibility.PRIVATE
        )
    }
}

/**
 * 输入页面预览 - 新建备忘录（有内容）
 */
@PreviewLightDark
@Composable
fun MemoInputPagePreview_NewMemoWithContent() {
    MoeMemosPreviewTheme {
        MemoInputPageContent(
            isEditMode = false,
            text = TextFieldValue("This is a new memo with some content.\n\n- Item 1\n- Item 2\n- Item 3"),
            currentVisibility = MemoVisibility.PRIVATE
        )
    }
}

/**
 * 输入页面预览 - 编辑模式
 */
@PreviewLightDark
@Composable
fun MemoInputPagePreview_EditMode() {
    MoeMemosPreviewTheme {
        MemoInputPageContent(
            isEditMode = true,
            text = TextFieldValue("This is an existing memo being edited.\n\n**Bold text** and *italic text*."),
            currentVisibility = MemoVisibility.PUBLIC
        )
    }
}

/**
 * 输入页面预览 - 公开可见性
 */
@PreviewLightDark
@Composable
fun MemoInputPagePreview_PublicVisibility() {
    MoeMemosPreviewTheme {
        MemoInputPageContent(
            isEditMode = false,
            text = TextFieldValue("This will be a public memo!"),
            currentVisibility = MemoVisibility.PUBLIC
        )
    }
}

// ==================== Page Content ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemoInputPageContent(
    isEditMode: Boolean,
    text: TextFieldValue,
    currentVisibility: MemoVisibility,
    modifier: Modifier = Modifier
) {
    val colors = MoeDesignTokens.colors
    var currentText by remember { mutableStateOf(text) }
    val canSubmit = currentText.text.isNotEmpty()

    Scaffold(
        containerColor = colors.bgApp,
        topBar = {
            MemoInputTopBarPreview(
                isEditMode = isEditMode,
                canSubmit = canSubmit,
                onClose = { },
                onSubmit = { }
            )
        },
        bottomBar = {
            MemoInputBottomBarPreview(
                currentVisibility = currentVisibility,
                onVisibilityClick = { },
                onTagClick = { },
                onImageClick = { },
                onAttachmentClick = { },
                onCameraClick = { }
            )
        }
    ) { innerPadding ->
        MemoInputEditorPreview(
            modifier = modifier.padding(innerPadding),
            text = currentText,
            onTextChange = { currentText = it },
            isEditMode = isEditMode,
            currentVisibility = currentVisibility
        )
    }
}

// ==================== Components ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemoInputTopBarPreview(
    isEditMode: Boolean,
    canSubmit: Boolean,
    onClose: () -> Unit,
    onSubmit: () -> Unit
) {
    val colors = MoeDesignTokens.colors

    MoeAppBar(
        title = if (isEditMode) "Edit" else "Compose",
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = "Close")
            }
        },
        actions = {
            IconButton(
                enabled = canSubmit,
                onClick = onSubmit,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = colors.textOnAccent,
                    disabledContentColor = colors.textTertiary,
                ),
                modifier = Modifier
                    .background(
                        color = if (canSubmit) colors.accentPrimary else colors.bgPressed,
                        shape = MoeRadius.shapeFull,
                    )
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemoInputBottomBarPreview(
    currentVisibility: MemoVisibility,
    onVisibilityClick: () -> Unit,
    onTagClick: () -> Unit,
    onImageClick: () -> Unit,
    onAttachmentClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    val colors = MoeDesignTokens.colors

    BottomAppBar(
        containerColor = colors.bgOverlay,
        contentColor = colors.textPrimary,
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MoeSpacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Visibility button
            IconButton(onClick = onVisibilityClick) {
                Icon(
                    imageVector = Icons.Outlined.Image,
                    contentDescription = "Visibility"
                )
            }

            // Tag button
            IconButton(onClick = onTagClick) {
                Icon(Icons.Outlined.Tag, contentDescription = "Tag")
            }

            // Todo button
            IconButton(onClick = { }) {
                Icon(Icons.Outlined.CheckBox, contentDescription = "Add task")
            }

            // Image button
            IconButton(onClick = onImageClick) {
                Icon(Icons.Outlined.Image, contentDescription = "Add image")
            }

            // Attachment button
            IconButton(onClick = onAttachmentClick) {
                Icon(Icons.Outlined.Attachment, contentDescription = "Attachment")
            }

            // Camera button
            IconButton(onClick = onCameraClick) {
                Icon(Icons.Outlined.PhotoCamera, contentDescription = "Take photo")
            }
        }
    }
}

@Composable
private fun MemoInputEditorPreview(
    modifier: Modifier = Modifier,
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    isEditMode: Boolean,
    currentVisibility: MemoVisibility
) {
    val colors = MoeDesignTokens.colors

    Column(modifier.fillMaxHeight()) {
        MoeCard(
            modifier = Modifier
                .padding(
                    start = MoeSpacing.xl,
                    end = MoeSpacing.xl,
                    top = MoeSpacing.md,
                    bottom = MoeSpacing.md,
                )
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(MoeSpacing.xl),
            containerColor = colors.bgSurface,
        ) {
            Column(modifier = Modifier.fillMaxHeight()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isEditMode) "Edit memo" else "New memo",
                            style = MoeTypography.caption,
                            color = colors.textTertiary,
                        )
                        Text(
                            text = "Any thoughts?",
                            style = MoeTypography.headline,
                            color = colors.textPrimary,
                            modifier = Modifier.padding(top = MoeSpacing.xs),
                        )
                    }
                    Text(
                        text = when (currentVisibility) {
                            MemoVisibility.PUBLIC -> "Public"
                            MemoVisibility.PROTECTED -> "Protected"
                            MemoVisibility.PRIVATE -> "Private"
                        },
                        style = MoeTypography.label,
                        color = colors.accentPrimary,
                        modifier = Modifier
                            .background(
                                color = colors.accentSoft,
                                shape = MoeRadius.shapeFull,
                            )
                            .padding(horizontal = MoeSpacing.md, vertical = MoeSpacing.xs),
                    )
                }

                MoeInputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MoeSpacing.xl)
                        .weight(1f),
                    value = text,
                    onValueChange = onTextChange,
                    label = "Any thoughts?",
                    maxLines = Int.MAX_VALUE
                )
            }
        }
    }
}
