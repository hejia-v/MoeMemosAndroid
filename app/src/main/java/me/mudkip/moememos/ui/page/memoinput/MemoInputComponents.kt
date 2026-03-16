package me.mudkip.moememos.ui.page.memoinput

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import android.content.ClipData
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Attachment
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.FormatBold
import androidx.compose.material.icons.outlined.FormatItalic
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material.icons.outlined.FormatStrikethrough
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import me.mudkip.moememos.R
import me.mudkip.moememos.data.local.entity.ResourceEntity
import me.mudkip.moememos.data.model.Account
import me.mudkip.moememos.data.model.MemoVisibility
import me.mudkip.moememos.ext.icon
import me.mudkip.moememos.ext.string
import me.mudkip.moememos.ext.titleResource
import me.mudkip.moememos.ui.component.Attachment
import me.mudkip.moememos.ui.component.InputImage
import me.mudkip.moememos.ui.designsystem.component.MoeAppBar
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.component.MoeInputField
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeRadius
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.viewmodel.MemoInputViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MemoInputTopBar(
    isEditMode: Boolean,
    canSubmit: Boolean,
    onClose: () -> Unit,
    onSubmit: () -> Unit
) {
    val colors = MoeDesignTokens.colors

    MoeAppBar(
        title = if (isEditMode) {
            stringResource(R.string.edit)
        } else {
            stringResource(R.string.compose)
        },
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.close))
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
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = stringResource(R.string.post))
            }
        }
    )
}

@Composable
private fun FormattingButtons(
    onFormat: (MarkdownFormat) -> Unit,
) {
    val colors = MoeDesignTokens.colors

    MarkdownFormat.entries.forEach { format ->
        IconButton(
            onClick = { onFormat(format) },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = colors.textSecondary,
            )
        ) {
            when (format) {
                MarkdownFormat.BOLD -> Icon(Icons.Outlined.FormatBold, contentDescription = format.label)
                MarkdownFormat.ITALIC -> Icon(Icons.Outlined.FormatItalic, contentDescription = format.label)
                MarkdownFormat.STRIKETHROUGH -> Icon(Icons.Outlined.FormatStrikethrough, contentDescription = format.label)
                MarkdownFormat.BULLET -> Icon(Icons.AutoMirrored.Outlined.FormatListBulleted, contentDescription = format.label)
                MarkdownFormat.NUMBERED -> Icon(Icons.Outlined.FormatListNumbered, contentDescription = format.label)
                MarkdownFormat.H1, MarkdownFormat.H2, MarkdownFormat.H3 -> Text(
                    text = format.label,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colors.textSecondary,
                )
            }
        }
    }
}

@Composable
internal fun MemoInputBottomBar(
    currentAccount: Account?,
    currentVisibility: MemoVisibility,
    visibilityMenuExpanded: Boolean,
    onVisibilityExpandedChange: (Boolean) -> Unit,
    onVisibilitySelected: (MemoVisibility) -> Unit,
    tags: List<String>,
    tagMenuExpanded: Boolean,
    onTagExpandedChange: (Boolean) -> Unit,
    onHashTagClick: () -> Unit,
    onTagSelected: (String) -> Unit,
    onToggleTodoItem: () -> Unit,
    onPickImage: () -> Unit,
    onPickAttachment: () -> Unit,
    onTakePhoto: () -> Unit,
    onFormat: (MarkdownFormat) -> Unit,
) {
    val scrollState = rememberScrollState()
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
            Row(
                modifier = Modifier.horizontalScroll(scrollState),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (currentAccount !is Account.Local) {
                    Box {
                        DropdownMenu(
                            expanded = visibilityMenuExpanded,
                            onDismissRequest = { onVisibilityExpandedChange(false) },
                            shape = MoeRadius.shapeLg,
                            containerColor = colors.bgSurface,
                            properties = PopupProperties(focusable = false)
                        ) {
                            enumValues<MemoVisibility>().forEach { visibility ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(visibility.titleResource),
                                            style = MoeTypography.body,
                                        )
                                    },
                                    onClick = {
                                        onVisibilitySelected(visibility)
                                        onVisibilityExpandedChange(false)
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = colors.textPrimary,
                                        leadingIconColor = colors.textSecondary,
                                        trailingIconColor = colors.accentPrimary,
                                    ),
                                    leadingIcon = {
                                        Icon(
                                            visibility.icon,
                                            contentDescription = stringResource(visibility.titleResource)
                                        )
                                    },
                                    trailingIcon = {
                                        if (currentVisibility == visibility) {
                                            Icon(Icons.Outlined.Check, contentDescription = null)
                                        }
                                    }
                                )
                            }
                        }
                        IconButton(
                            onClick = { onVisibilityExpandedChange(!visibilityMenuExpanded) },
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = colors.textPrimary,
                            )
                        ) {
                            Icon(
                                currentVisibility.icon,
                                contentDescription = stringResource(currentVisibility.titleResource)
                            )
                        }
                    }
                }

                if (tags.isEmpty()) {
                    IconButton(onClick = onHashTagClick) {
                        Icon(Icons.Outlined.Tag, contentDescription = stringResource(R.string.tag))
                    }
                } else {
                    Box {
                        DropdownMenu(
                            expanded = tagMenuExpanded,
                            onDismissRequest = { onTagExpandedChange(false) },
                            shape = MoeRadius.shapeLg,
                            containerColor = colors.bgSurface,
                            properties = PopupProperties(focusable = false)
                        ) {
                            tags.forEach { tag ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = tag,
                                            style = MoeTypography.body,
                                        )
                                    },
                                    onClick = {
                                        onTagSelected(tag)
                                        onTagExpandedChange(false)
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = colors.textPrimary,
                                        leadingIconColor = colors.textSecondary,
                                    ),
                                    leadingIcon = {
                                        Icon(Icons.Outlined.Tag, contentDescription = null)
                                    }
                                )
                            }
                        }
                        IconButton(
                            onClick = { onTagExpandedChange(!tagMenuExpanded) },
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = colors.textPrimary,
                            )
                        ) {
                            Icon(Icons.Outlined.Tag, contentDescription = stringResource(R.string.tag))
                        }
                    }
                }

                IconButton(
                    onClick = onToggleTodoItem,
                    colors = IconButtonDefaults.iconButtonColors(contentColor = colors.textPrimary)
                ) {
                    Icon(Icons.Outlined.CheckBox, contentDescription = stringResource(R.string.add_task))
                }

                IconButton(
                    onClick = onPickImage,
                    colors = IconButtonDefaults.iconButtonColors(contentColor = colors.textPrimary)
                ) {
                    Icon(Icons.Outlined.Image, contentDescription = stringResource(R.string.add_image))
                }

                IconButton(
                    onClick = onPickAttachment,
                    colors = IconButtonDefaults.iconButtonColors(contentColor = colors.textPrimary)
                ) {
                    Icon(Icons.Outlined.Attachment, contentDescription = stringResource(R.string.attachment))
                }

                IconButton(
                    onClick = onTakePhoto,
                    colors = IconButtonDefaults.iconButtonColors(contentColor = colors.textPrimary)
                ) {
                    Icon(Icons.Outlined.PhotoCamera, contentDescription = stringResource(R.string.take_photo))
                }

                Spacer(modifier = Modifier.size(4.dp))

                FormattingButtons(onFormat = onFormat)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun MemoInputEditor(
    modifier: Modifier = Modifier,
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    isEditMode: Boolean,
    currentVisibility: MemoVisibility,
    focusRequester: FocusRequester,
    validMimeTypePrefixes: Set<String>,
    onDroppedText: (String) -> Unit,
    uploadResources: List<ResourceEntity>,
    inputViewModel: MemoInputViewModel
) {
    val colors = MoeDesignTokens.colors
    val imageResources = remember(uploadResources) {
        uploadResources.filter { it.mimeType?.startsWith("image/") == true }
    }
    val attachmentResources = remember(uploadResources) {
        uploadResources.filterNot { it.mimeType?.startsWith("image/") == true }
    }

    Column(
        modifier
            .fillMaxHeight()
            .dragAndDropTarget(
                shouldStartDragAndDrop = accept@{ startEvent ->
                    startEvent
                        .mimeTypes()
                        .any { eventMimeType ->
                            validMimeTypePrefixes.any(eventMimeType::startsWith)
                        }
                },
                target = object : DragAndDropTarget {
                    override fun onDrop(event: DragAndDropEvent): Boolean {
                        val androidDragEvent = event.toAndroidDragEvent()
                        val concatText = androidDragEvent.clipData
                            .textList()
                            .fold("") { acc, droppedText ->
                                if (acc.isNotBlank()) {
                                    acc.trimEnd { it == '\n' } + "\n\n" + droppedText.trimStart { it == '\n' }
                                } else {
                                    droppedText
                                }
                            }
                        onDroppedText(concatText)
                        return true
                    }
                }
            )
    ) {
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
                            text = if (isEditMode) {
                                stringResource(R.string.memo_input_edit_label)
                            } else {
                                stringResource(R.string.memo_input_new_label)
                            },
                            style = MoeTypography.caption,
                            color = colors.textTertiary,
                        )
                        Text(
                            text = stringResource(R.string.any_thoughts),
                            style = MoeTypography.headline,
                            color = colors.textPrimary,
                            modifier = Modifier.padding(top = MoeSpacing.xs),
                        )
                        Text(
                            text = stringResource(R.string.memo_input_helper),
                            style = MoeTypography.body,
                            color = colors.textSecondary,
                            modifier = Modifier.padding(top = MoeSpacing.sm),
                        )
                    }
                    Text(
                        text = stringResource(currentVisibility.titleResource),
                        style = MoeTypography.label,
                        color = colors.accentPrimary,
                        modifier = Modifier
                            .padding(start = MoeSpacing.md)
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
                        .weight(1f)
                        .focusRequester(focusRequester),
                    value = text,
                    onValueChange = onTextChange,
                    label = stringResource(R.string.any_thoughts),
                    maxLines = Int.MAX_VALUE,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                )
            }
        }

        AnimatedVisibility(
            visible = imageResources.isNotEmpty(),
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            InputResourceSection(
                title = stringResource(R.string.memo_input_images),
                modifier = Modifier
                    .padding(
                        start = MoeSpacing.xl,
                        end = MoeSpacing.xl,
                        bottom = if (attachmentResources.isEmpty()) MoeSpacing.xl else MoeSpacing.sm,
                    )
            ) {
                LazyRow(
                    modifier = Modifier.height(84.dp),
                    horizontalArrangement = Arrangement.spacedBy(MoeSpacing.sm)
                ) {
                    items(imageResources, key = { it.identifier }) { resource ->
                        InputImage(resource = resource, inputViewModel = inputViewModel)
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = attachmentResources.isNotEmpty(),
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            InputResourceSection(
                title = stringResource(R.string.memo_input_attachments),
                modifier = Modifier
                    .padding(
                        start = MoeSpacing.xl,
                        end = MoeSpacing.xl,
                        bottom = MoeSpacing.xl,
                    )
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(MoeSpacing.sm),
                    verticalArrangement = Arrangement.spacedBy(MoeSpacing.sm),
                ) {
                    attachmentResources.forEach { resource ->
                        Attachment(
                            resource = resource,
                            onRemove = { inputViewModel.deleteResource(resource.identifier) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InputResourceSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val colors = MoeDesignTokens.colors

    MoeCard(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(MoeSpacing.lg),
        containerColor = colors.bgSurface,
    ) {
        Column {
            Text(
                text = title,
                style = MoeTypography.label,
                color = colors.textPrimary,
            )
            Box(modifier = Modifier.padding(top = MoeSpacing.md)) {
                content()
            }
        }
    }
}

@Composable
internal fun SaveChangesDialog(
    onSave: () -> Unit,
    onDiscard: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = MoeDesignTokens.colors

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.memo_input_save_changes_title),
                style = MoeTypography.title,
                color = colors.textPrimary,
            )
        },
        text = {
            Text(
                text = stringResource(R.string.memo_input_save_changes_message),
                style = MoeTypography.body,
                color = colors.textSecondary,
            )
        },
        confirmButton = {
            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.accentPrimary,
                    contentColor = colors.textOnAccent,
                )
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDiscard,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = colors.textSecondary,
                )
            ) {
                Text(stringResource(R.string.discard))
            }
        }
    )
}

private fun ClipData.textList(): List<String> {
    return (0 until itemCount)
        .mapNotNull(::getItemAt)
        .mapNotNull { it.text?.toString() }
}
