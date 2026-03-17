package me.mudkip.moememos.ui.component

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.text.format.DateUtils
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.launch
import me.mudkip.moememos.R
import me.mudkip.moememos.data.local.entity.MemoEntity
import me.mudkip.moememos.data.model.Account
import me.mudkip.moememos.data.model.MemoEditGesture
import me.mudkip.moememos.ext.icon
import me.mudkip.moememos.ext.string
import me.mudkip.moememos.ext.titleResource
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeMotion
import me.mudkip.moememos.ui.designsystem.token.MoeRadius
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.page.common.LocalRootNavController
import me.mudkip.moememos.ui.page.common.RouteName
import me.mudkip.moememos.viewmodel.LocalMemos
import me.mudkip.moememos.viewmodel.LocalUserState

@Composable
fun MemosCard(
    memo: MemoEntity,
    onClick: (MemoEntity) -> Unit,
    editGesture: MemoEditGesture = MemoEditGesture.NONE,
    previewMode: Boolean = false,
    showSyncStatus: Boolean = false,
    onTagClick: ((String) -> Unit)? = null,
    style: MemosCardStyle = MemosCardStyle.Standard,
    modifier: Modifier = Modifier,
) {
    val memosViewModel = LocalMemos.current
    val rootNavController = LocalRootNavController.current
    val scope = rememberCoroutineScope()
    val colors = MoeDesignTokens.colors
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    var entered by remember(memo.identifier) { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.988f else 1f,
        animationSpec = tween(
            durationMillis = if (pressed) {
                MoeMotion.fast.inWholeMilliseconds.toInt()
            } else {
                MoeMotion.normal.inWholeMilliseconds.toInt()
            }
        ),
        label = "memo_card_scale"
    )
    val entryAlpha by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(
            durationMillis = MoeMotion.normal.inWholeMilliseconds.toInt()
        ),
        label = "memo_card_alpha"
    )
    val entryOffset by animateDpAsState(
        targetValue = if (entered) 0.dp else 10.dp,
        animationSpec = tween(
            durationMillis = MoeMotion.normal.inWholeMilliseconds.toInt()
        ),
        label = "memo_card_offset"
    )

    LaunchedEffect(memo.identifier) {
        entered = true
    }

    val cardModifier = Modifier
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
            alpha = entryAlpha
            translationY = entryOffset.toPx()
        }
        .then(
            when (style) {
                MemosCardStyle.Standard -> Modifier
                    .padding(horizontal = MoeSpacing.xl, vertical = MoeSpacing.sm)
                    .fillMaxWidth()

                MemosCardStyle.HomeCompact -> Modifier.fillMaxWidth()
            }
        )
        .then(modifier)
        .combinedClickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = {
                if (editGesture == MemoEditGesture.SINGLE) {
                    rootNavController.navigate("${RouteName.EDIT}?memoId=${memo.identifier}")
                } else {
                    onClick(memo)
                }
            },
            onLongClick = if (editGesture == MemoEditGesture.LONG) {
                {
                    rootNavController.navigate("${RouteName.EDIT}?memoId=${memo.identifier}")
                }
            } else {
                null
            },
            onDoubleClick = if (editGesture == MemoEditGesture.DOUBLE) {
                {
                    rootNavController.navigate("${RouteName.EDIT}?memoId=${memo.identifier}")
                }
            } else {
                null
            }
        )

    when (style) {
        MemosCardStyle.Standard -> {
            MoeCard(
                modifier = cardModifier,
                border = if (memo.pinned) {
                    BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                } else {
                    null
                },
                containerColor = colors.bgElevated
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .padding(
                                start = MoeSpacing.lg,
                                end = MoeSpacing.sm,
                                top = MoeSpacing.md,
                            )
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            DateUtils.getRelativeTimeSpanString(
                                memo.date.toEpochMilli(),
                                System.currentTimeMillis(),
                                DateUtils.SECOND_IN_MILLIS
                            ).toString(),
                            style = MoeTypography.caption,
                            color = colors.textTertiary
                        )
                        if (showSyncStatus && memo.needsSync) {
                            Icon(
                                imageVector = Icons.Outlined.CloudOff,
                                contentDescription = R.string.memo_sync_pending.string,
                                modifier = Modifier
                                    .padding(start = 5.dp)
                                    .size(20.dp),
                                tint = colors.accentDanger
                            )
                        }
                        if (LocalUserState.current.currentUser?.defaultVisibility != memo.visibility) {
                            Icon(
                                memo.visibility.icon,
                                contentDescription = stringResource(memo.visibility.titleResource),
                                modifier = Modifier
                                    .padding(start = 5.dp)
                                    .size(20.dp),
                                tint = colors.textTertiary
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        MemosCardActionButton(memo)
                    }

                    MemoContent(
                        memo,
                        previewMode = previewMode,
                        checkboxChange = { checked, startOffset, endOffset ->
                            scope.launch {
                                var text = memo.content.substring(startOffset, endOffset)
                                text = if (checked) {
                                    text.replace("[ ]", "[x]")
                                } else {
                                    text.replace("[x]", "[ ]")
                                }
                                memosViewModel.editMemo(
                                    memo.identifier,
                                    memo.content.replaceRange(startOffset, endOffset, text),
                                    memo.resources,
                                    memo.visibility
                                )
                            }
                        },
                        onViewMore = {
                            onClick(memo)
                        },
                        onTagClick = onTagClick
                    )
                }
            }
        }

        MemosCardStyle.HomeCompact -> {
            val preview = remember(memo.content) { memo.buildHomeCardPreview() }

            MoeCard(
                modifier = cardModifier,
                border = null,
                containerColor = colors.bgSurface,
                shape = MoeRadius.shapeXl,
                elevation = 4.dp,
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = MoeSpacing.lg,
                        end = MoeSpacing.lg,
                        top = MoeSpacing.lg,
                        bottom = MoeSpacing.lg,
                    )
                ) {
                    if (memo.pinned || (showSyncStatus && memo.needsSync)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = MoeSpacing.sm),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (memo.pinned) {
                                Text(
                                    text = R.string.pin.string,
                                    style = MoeTypography.caption,
                                    color = colors.accentPrimary,
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            if (showSyncStatus && memo.needsSync) {
                                Icon(
                                    imageVector = Icons.Outlined.CloudOff,
                                    contentDescription = R.string.memo_sync_pending.string,
                                    modifier = Modifier.size(16.dp),
                                    tint = colors.accentDanger
                                )
                            }
                        }
                    }
                    Text(
                        text = preview.title,
                        style = MoeTypography.title,
                        color = colors.textPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (preview.body.isNotBlank()) {
                        Text(
                            text = preview.body,
                            style = MoeTypography.body,
                            color = colors.textSecondary,
                            maxLines = 5,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = MoeSpacing.sm),
                        )
                    }
                    Text(
                        text = remember(memo.date) {
                            SimpleDateFormat("M月d日", Locale.CHINESE)
                                .format(Date(memo.date.toEpochMilli()))
                        },
                        style = MoeTypography.caption,
                        color = colors.textTertiary,
                        modifier = Modifier.padding(top = MoeSpacing.md),
                    )
                }
            }
        }
    }
}

enum class MemosCardStyle {
    Standard,
    HomeCompact,
}

private data class HomeCardPreview(
    val title: String,
    val body: String,
)

private fun MemoEntity.buildHomeCardPreview(): HomeCardPreview {
    val normalized = content
        .replace(Regex("!\\[[^\\]]*]\\([^)]*\\)"), " ")
        .replace(Regex("\\[([^\\]]+)]\\([^)]*\\)"), "$1")
        .replace(Regex("`{1,3}"), "")
        .replace(Regex("^\\s{0,3}#{1,6}\\s*", RegexOption.MULTILINE), "")
        .replace(Regex("^\\s{0,3}>\\s?", RegexOption.MULTILINE), "")
        .replace(Regex("^\\s*[-*+]\\s+", RegexOption.MULTILINE), "")
        .replace(Regex("^\\s*\\d+\\.\\s+", RegexOption.MULTILINE), "")
        .replace("[ ]", "")
        .replace("[x]", "")
        .replace(Regex("\\s+"), " ")
        .trim()

    val sentences = normalized
        .split(Regex("[\\n.!?\\u3002\\uFF01\\uFF1F]"))
        .map { it.trim() }
        .filter { it.isNotEmpty() }

    val title = sentences.firstOrNull()?.take(36)
        ?: content.lines().firstOrNull { it.isNotBlank() }?.trim()?.take(36)
        ?: "Memo"
    val body = when {
        sentences.size > 1 -> sentences.drop(1).joinToString(" ")
        normalized.length > title.length -> normalized.removePrefix(title).trim()
        else -> ""
    }

    return HomeCardPreview(title = title, body = body)
}

@Composable
fun MemosCardActionButton(
    memo: MemoEntity,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val clipboardManager = context.getSystemService(ClipboardManager::class.java)
    val memosViewModel = LocalMemos.current
    val userStateViewModel = LocalUserState.current
    val currentAccount by userStateViewModel.currentAccount.collectAsState()
    val rootNavController = LocalRootNavController.current
    val scope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }
    val memoLabel = stringResource(R.string.memo)
    val colors = MoeDesignTokens.colors

    Box {
        IconButton(onClick = { menuExpanded = true }) {
            Icon(Icons.Filled.MoreVert, contentDescription = null)
        }
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
            shape = MoeRadius.shapeLg,
            containerColor = colors.bgSurface,
        ) {
            if (memo.pinned) {
                DropdownMenuItem(
                    text = { Text(R.string.unpin.string, color = colors.textPrimary) },
                    onClick = {
                        scope.launch {
                            memosViewModel.updateMemoPinned(memo.identifier, false).suspendOnSuccess {
                                menuExpanded = false
                            }
                        }
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.PinDrop,
                            contentDescription = null,
                            tint = colors.textSecondary,
                        )
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = colors.textPrimary,
                        leadingIconColor = colors.textSecondary,
                    )
                )
            } else {
                DropdownMenuItem(
                    text = { Text(R.string.pin.string, color = colors.textPrimary) },
                    onClick = {
                        scope.launch {
                            memosViewModel.updateMemoPinned(memo.identifier, true).suspendOnSuccess {
                                menuExpanded = false
                            }
                        }
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.PushPin,
                            contentDescription = null,
                            tint = colors.textSecondary,
                        )
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = colors.textPrimary,
                        leadingIconColor = colors.textSecondary,
                    )
                )
            }
            DropdownMenuItem(
                text = { Text(R.string.edit.string, color = colors.textPrimary) },
                onClick = {
                    rootNavController.navigate("${RouteName.EDIT}?memoId=${memo.identifier}")
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = null,
                        tint = colors.textSecondary,
                    )
                },
                colors = MenuDefaults.itemColors(
                    textColor = colors.textPrimary,
                    leadingIconColor = colors.textSecondary,
                )
            )
            DropdownMenuItem(
                text = { Text(R.string.share.string, color = colors.textPrimary) },
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, memo.content)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Share,
                        contentDescription = null,
                        tint = colors.textSecondary,
                    )
                },
                colors = MenuDefaults.itemColors(
                    textColor = colors.textPrimary,
                    leadingIconColor = colors.textSecondary,
                )
            )
            DropdownMenuItem(
                text = { Text(R.string.copy.string, color = colors.textPrimary) },
                onClick = {
                    clipboardManager?.setPrimaryClip(
                        ClipData.newPlainText(memoLabel, memo.content)
                    )
                    menuExpanded = false
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.ContentCopy,
                        contentDescription = null,
                        tint = colors.textSecondary,
                    )
                },
                colors = MenuDefaults.itemColors(
                    textColor = colors.textPrimary,
                    leadingIconColor = colors.textSecondary,
                )
            )
            if (currentAccount !is Account.Local) {
                DropdownMenuItem(
                    text = { Text(R.string.copy_link.string, color = colors.textPrimary) },
                    onClick = {
                        memosViewModel.host.value?.let { host ->
                            val memoUrl = "$host/${memo.remoteId ?: memo.identifier}"
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, memoUrl)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)
                        }
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Link,
                            contentDescription = null,
                            tint = colors.textSecondary,
                        )
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = colors.textPrimary,
                        leadingIconColor = colors.textSecondary,
                    )
                )
            }
            DropdownMenuItem(
                text = { Text(R.string.archive.string, color = colors.textSecondary) },
                onClick = {
                    scope.launch {
                        memosViewModel.archiveMemo(memo.identifier).suspendOnSuccess {
                            menuExpanded = false
                        }
                    }
                },
                colors = MenuDefaults.itemColors(
                    textColor = colors.textSecondary,
                    leadingIconColor = colors.textSecondary,
                ),
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Archive,
                        contentDescription = null,
                        tint = colors.textSecondary,
                    )
                }
            )
            DropdownMenuItem(
                text = { Text(R.string.delete.string, color = colors.accentDanger) },
                onClick = {
                    showDeleteDialog = true
                    menuExpanded = false
                },
                colors = MenuDefaults.itemColors(
                    textColor = colors.accentDanger,
                    leadingIconColor = colors.accentDanger,
                ),
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = colors.accentDanger,
                    )
                }
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = R.string.delete_this_memo.string,
                    style = MoeTypography.title,
                    color = colors.textPrimary,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            memosViewModel.deleteMemo(memo.identifier).suspendOnSuccess {
                                showDeleteDialog = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = colors.textOnAccent,
                        containerColor = colors.accentDanger
                    )
                ) {
                    Text(R.string.confirm.string)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colors.textSecondary,
                    )
                ) {
                    Text(R.string.cancel.string)
                }
            }
        )
    }
}
