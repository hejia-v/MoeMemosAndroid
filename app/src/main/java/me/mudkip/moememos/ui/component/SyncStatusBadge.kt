package me.mudkip.moememos.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.mudkip.moememos.R
import me.mudkip.moememos.ext.string
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeMotion
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.preview.MoeMemosPreviewTheme

@Composable
fun SyncStatusBadge(
    syncing: Boolean,
    unsyncedCount: Int,
    onSync: () -> Unit
) {
    val indicatorSize: Dp = 20.dp
    val colors = MoeDesignTokens.colors
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val containerColor by animateColorAsState(
        targetValue = if (pressed) colors.bgPressed else colors.bgSurface,
        animationSpec = tween(MoeMotion.fast.inWholeMilliseconds.toInt()),
        label = "sync_badge_container"
    )

    IconButton(
        onClick = {
            if (!syncing) {
                onSync()
            }
        },
        interactionSource = interactionSource,
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(containerColor)
    ) {
        AnimatedContent(
            targetState = when {
                syncing -> SyncBadgeState.Syncing
                unsyncedCount > 0 -> SyncBadgeState.Unsynced
                else -> SyncBadgeState.Idle
            },
            transitionSpec = {
                fadeIn(animationSpec = tween(160)).togetherWith(
                    fadeOut(animationSpec = tween(120))
                )
            },
            label = "sync_badge_state"
        ) { state ->
            when (state) {
                SyncBadgeState.Syncing -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(indicatorSize),
                        strokeWidth = 2.dp,
                        color = colors.accentPrimary,
                    )
                }

                SyncBadgeState.Unsynced -> {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = colors.accentDanger,
                                contentColor = colors.textOnAccent
                            ) {
                                Text(unsyncedCount.toString())
                            }
                        }
                    ) {
                        Icon(
                            Icons.Outlined.CloudOff,
                            contentDescription = R.string.sync_status_unsynced.string,
                            tint = colors.accentDanger
                        )
                    }
                }

                SyncBadgeState.Idle -> {
                    Icon(
                        Icons.Outlined.Sync,
                        contentDescription = R.string.sync_status_sync_now.string,
                        tint = colors.textPrimary
                    )
                }
            }
        }
    }
}

private enum class SyncBadgeState {
    Syncing,
    Unsynced,
    Idle,
}

// ==================== Previews ====================

@PreviewLightDark
@Composable
private fun SyncStatusBadgeIdlePreview() {
    MoeMemosPreviewTheme {
        SyncStatusBadge(
            syncing = false,
            unsyncedCount = 0,
            onSync = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun SyncStatusBadgeSyncingPreview() {
    MoeMemosPreviewTheme {
        SyncStatusBadge(
            syncing = true,
            unsyncedCount = 0,
            onSync = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun SyncStatusBadgeUnsyncedPreview() {
    MoeMemosPreviewTheme {
        SyncStatusBadge(
            syncing = false,
            unsyncedCount = 5,
            onSync = {}
        )
    }
}
