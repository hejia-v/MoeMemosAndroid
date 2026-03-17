package me.mudkip.moememos.ui.page.memos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import me.mudkip.moememos.R
import me.mudkip.moememos.data.model.Account
import me.mudkip.moememos.data.model.SyncStatus
import me.mudkip.moememos.ext.string
import me.mudkip.moememos.ui.component.SyncStatusBadge
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.page.common.LocalRootNavController
import me.mudkip.moememos.ui.page.common.RouteName
import me.mudkip.moememos.viewmodel.LocalMemos
import me.mudkip.moememos.viewmodel.LocalUserState
import me.mudkip.moememos.viewmodel.ManualSyncResult
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemosHomePage(
    drawerState: DrawerState? = null,
    navController: NavHostController
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val rootNavController = LocalRootNavController.current
    val memosViewModel = LocalMemos.current
    val userStateViewModel = LocalUserState.current
    val currentAccount by userStateViewModel.currentAccount.collectAsState()
    val syncStatus by memosViewModel.syncStatus.collectAsState()
    var syncAlert by remember { mutableStateOf<HomeSyncAlert?>(null) }

    suspend fun requestManualSync(allowHigherV1Version: String? = null) {
        when (val result = memosViewModel.refreshMemos(allowHigherV1Version)) {
            ManualSyncResult.Completed -> Unit
            is ManualSyncResult.Blocked -> {
                syncAlert = HomeSyncAlert.Blocked(result.message)
            }

            is ManualSyncResult.RequiresConfirmation -> {
                syncAlert = HomeSyncAlert.RequiresConfirmation(result.version, result.message)
            }

            is ManualSyncResult.Failed -> {
                syncAlert = HomeSyncAlert.Failed(result.message)
            }
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    rootNavController.navigate(RouteName.INPUT)
                },
                modifier = Modifier
                    .navigationBarsPadding()
                    .shadow(
                        elevation = 22.dp,
                        shape = CircleShape,
                    ),
                shape = CircleShape,
                containerColor = Color(0xFFF7C41D),
                contentColor = Color(0xFF3A2A00),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    focusedElevation = 0.dp,
                    hoveredElevation = 0.dp,
                ),
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = R.string.compose.string,
                    modifier = Modifier.size(28.dp),
                )
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MoeDesignTokens.colors.bgApp)
        ) {
            val topScrimColor = Color(0xFFFAF9F6)

            HomeBackground()

            // Layer 1 (bottom): scrollable card list
            MemosList(
                lazyListState = listState,
                contentPadding = innerPadding,
                onRefresh = { requestManualSync() },
                onTagClick = { tag ->
                    navController.navigate("${RouteName.TAG}/${URLEncoder.encode(tag, "UTF-8")}") {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                layoutStyle = MemosListLayoutStyle.HomeGrid,
                headerContent = {
                    // Spacer to reserve space for the fixed toolbar above
                    Spacer(modifier = Modifier
                        .statusBarsPadding()
                        .height(64.dp)
                    )
                },
            )

            // Layer 2: keep the status bar area fully solid so content never shows through.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsTopHeight(WindowInsets.statusBars)
                    .align(Alignment.TopCenter)
                    .background(topScrimColor)
            )

            // Layer 3 (middle): stronger fade overlay behind the toolbar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(96.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                topScrimColor,
                                topScrimColor.copy(alpha = 0.98f),
                                topScrimColor.copy(alpha = 0.86f),
                                topScrimColor.copy(alpha = 0.62f),
                                Color.Transparent,
                            )
                        )
                    )
            )

            // Layer 4 (top): fixed floating toolbar
            HomeHeader(
                showDrawerButton = drawerState != null,
                currentAccount = currentAccount,
                syncStatus = syncStatus,
                memoCount = memosViewModel.memos.size,
                onSync = {
                    scope.launch {
                        requestManualSync()
                    }
                },
                onSearch = {
                    navController.navigate(RouteName.SEARCH)
                },
                onPrimaryAction = {
                    if (drawerState != null) {
                        scope.launch { drawerState.open() }
                    } else {
                        rootNavController.navigate(RouteName.SETTINGS)
                    }
                },
            )
        }
    }

    when (val alert = syncAlert) {
        null -> Unit
        is HomeSyncAlert.Blocked -> {
            AlertDialog(
                onDismissRequest = { syncAlert = null },
                title = { Text(R.string.unsupported_memos_version_title.string) },
                text = { Text(alert.message) },
                confirmButton = {
                    TextButton(onClick = { syncAlert = null }) {
                        Text(R.string.close.string)
                    }
                }
            )
        }

        is HomeSyncAlert.RequiresConfirmation -> {
            AlertDialog(
                onDismissRequest = { syncAlert = null },
                title = { Text(R.string.unsupported_memos_version_title.string) },
                text = { Text(alert.message) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            syncAlert = null
                            scope.launch {
                                requestManualSync(allowHigherV1Version = alert.version)
                            }
                        }
                    ) {
                        Text(R.string.still_sync.string)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { syncAlert = null }) {
                        Text(R.string.cancel.string)
                    }
                }
            )
        }

        is HomeSyncAlert.Failed -> {
            AlertDialog(
                onDismissRequest = { syncAlert = null },
                title = { Text(R.string.sync_failed.string) },
                text = { Text(alert.message) },
                confirmButton = {
                    TextButton(onClick = { syncAlert = null }) {
                        Text(R.string.close.string)
                    }
                }
            )
        }
    }
}

private sealed class HomeSyncAlert {
    data class Blocked(val message: String) : HomeSyncAlert()
    data class RequiresConfirmation(val version: String, val message: String) : HomeSyncAlert()
    data class Failed(val message: String) : HomeSyncAlert()
}

@Composable
private fun HomeBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFAF9F6),
                        Color(0xFFF7F5F0),
                        Color(0xFFF3F0EB),
                    )
                )
            )
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .size(360.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.94f),
                        Color.White.copy(alpha = 0.20f),
                        Color.Transparent,
                    )
                )
            )
    )
}

@Composable
private fun HomeHeader(
    showDrawerButton: Boolean,
    currentAccount: Account?,
    syncStatus: SyncStatus,
    memoCount: Int,
    onSync: () -> Unit,
    onSearch: () -> Unit,
    onPrimaryAction: () -> Unit,
) {
    val colors = MoeDesignTokens.colors

    Row(
        modifier = Modifier
            .statusBarsPadding()
            .padding(top = MoeSpacing.sm, bottom = MoeSpacing.md)
            .padding(horizontal = MoeSpacing.xl)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HomeHeaderIconButton(
            onClick = onPrimaryAction,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = if (showDrawerButton) R.string.menu.string else R.string.settings.string,
                    tint = colors.textPrimary,
                )
            }
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = MoeSpacing.md)
        ) {
            Text(
                text = R.string.memos.string,
                style = MoeTypography.title,
                color = colors.textPrimary,
            )
Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 3.dp)
            ) {
                HomeInlineStat(value = memoCount.toString())
                if (currentAccount !is Account.Local) {
                    HomeInlineStat(
                        value = if (syncStatus.syncing) "…" else syncStatus.unsyncedCount.toString(),
                        emphasize = !syncStatus.syncing && syncStatus.unsyncedCount > 0,
                    )
                }
            }
        }
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = colors.bgSurface.copy(alpha = 0.95f),
            shadowElevation = 4.dp,
            tonalElevation = 0.dp,
        ) {
            Row(
                modifier = Modifier.padding(horizontal = MoeSpacing.xs, vertical = MoeSpacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (currentAccount !is Account.Local) {
                    SyncStatusBadge(
                        syncing = syncStatus.syncing,
                        unsyncedCount = syncStatus.unsyncedCount,
                        onSync = onSync
                    )
                }
                HomeHeaderIconButton(
                    onClick = onSearch,
                    tonal = true,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = R.string.search.string,
                            tint = colors.textPrimary,
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun HomeHeaderIconButton(
    onClick: () -> Unit,
    tonal: Boolean = false,
    icon: @Composable () -> Unit,
) {
    val colors = MoeDesignTokens.colors

    Box(
        modifier = Modifier
            .size(44.dp)
            .then(
                if (tonal) {
                    Modifier
                } else {
                    Modifier.shadow(4.dp, CircleShape)
                }
            )
            .clip(CircleShape)
            .background(if (tonal) colors.bgOverlay else colors.bgSurface.copy(alpha = 0.96f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        icon()
    }
}

@Composable
private fun HomeInlineStat(
    value: String,
    emphasize: Boolean = false,
) {
    val colors = MoeDesignTokens.colors
    val dotColor = if (emphasize) colors.accentPrimary else colors.textTertiary
    val textColor = if (emphasize) colors.accentPrimary else colors.textPrimary

    Row(
        modifier = Modifier.padding(end = MoeSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .clip(CircleShape)
                .background(dotColor.copy(alpha = 0.7f))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            style = MoeTypography.caption,
            color = textColor,
        )
    }
}
