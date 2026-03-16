package me.mudkip.moememos.ui.page.memos

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import me.mudkip.moememos.R
import me.mudkip.moememos.data.model.Account
import me.mudkip.moememos.ext.string
import me.mudkip.moememos.ui.component.SyncStatusBadge
import me.mudkip.moememos.ui.designsystem.component.MoeAppBar
import me.mudkip.moememos.ui.designsystem.component.MoeCard
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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val rootNavController = LocalRootNavController.current
    val memosViewModel = LocalMemos.current
    val userStateViewModel = LocalUserState.current
    val currentAccount by userStateViewModel.currentAccount.collectAsState()
    val syncStatus by memosViewModel.syncStatus.collectAsState()
    val colors = MoeDesignTokens.colors

    val expandedFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }
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
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = colors.bgApp,
        topBar = {
            MoeAppBar(
                title = R.string.memos.string,
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    if (drawerState != null) {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = R.string.menu.string)
                        }
                    }
                },
                actions = {
                    if (currentAccount !is Account.Local) {
                        SyncStatusBadge(
                            syncing = syncStatus.syncing,
                            unsyncedCount = syncStatus.unsyncedCount,
                            onSync = {
                                scope.launch {
                                    requestManualSync()
                                }
                            }
                        )
                    }
                    IconButton(onClick = {
                        navController.navigate(RouteName.SEARCH)
                    }) {
                        Icon(Icons.Filled.Search, contentDescription = R.string.search.string)
                    }
                }
            )
        },

        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    rootNavController.navigate(RouteName.INPUT)
                },
                expanded = expandedFab,
                containerColor = colors.accentPrimary,
                contentColor = colors.textOnAccent,
                text = { Text(R.string.new_memo.string) },
                icon = { Icon(Icons.Filled.Add, contentDescription = R.string.compose.string) }
            )
        },

        content = { innerPadding ->
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
                headerContent = {
                    HomeOverviewCard(
                        memoCount = memosViewModel.memos.size,
                        currentAccount = currentAccount,
                        syncing = syncStatus.syncing,
                        unsyncedCount = syncStatus.unsyncedCount,
                        collapsedFraction = remember(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset) {
                            when {
                                listState.firstVisibleItemIndex > 0 -> 1f
                                else -> (listState.firstVisibleItemScrollOffset / 180f).coerceIn(0f, 1f)
                            }
                        },
                    )
                },
            )
        }
    )

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
private fun HomeOverviewCard(
    memoCount: Int,
    currentAccount: Account?,
    syncing: Boolean,
    unsyncedCount: Int,
    collapsedFraction: Float,
) {
    val colors = MoeDesignTokens.colors
    val alpha by animateFloatAsState(
        targetValue = 1f - (collapsedFraction * 0.55f),
        animationSpec = tween(durationMillis = 180),
        label = "home_overview_alpha"
    )
    val offsetY by animateFloatAsState(
        targetValue = -(collapsedFraction * 10f),
        animationSpec = tween(durationMillis = 180),
        label = "home_overview_offset"
    )

    MoeCard(
        modifier = Modifier
            .offset(y = offsetY.dp)
            .alpha(alpha)
            .padding(horizontal = MoeSpacing.xl, vertical = MoeSpacing.sm)
            .fillMaxWidth(),
        contentPadding = PaddingValues(
            horizontal = MoeSpacing.xl,
            vertical = MoeSpacing.xl,
        ),
        containerColor = colors.bgSurface,
    ) {
        Column {
            Text(
                text = R.string.memos_home_title.string,
                style = MoeTypography.headline,
                color = colors.textPrimary,
            )
            Text(
                text = R.string.memos_home_subtitle.string,
                style = MoeTypography.body,
                color = colors.textSecondary,
                modifier = Modifier.padding(top = MoeSpacing.sm),
            )
            Row(
                modifier = Modifier
                    .padding(top = MoeSpacing.xl)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = R.string.memos_home_total.string,
                        style = MoeTypography.caption,
                        color = colors.textTertiary,
                    )
                    Text(
                        text = memoCount.toString(),
                        style = MoeTypography.display,
                        color = colors.textPrimary,
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (currentAccount is Account.Local) {
                            R.string.memos_home_local_mode.string
                        } else if (syncing) {
                            R.string.memos_home_syncing.string
                        } else {
                            R.string.sync_status_unsynced.string
                        },
                        style = MoeTypography.caption,
                        color = colors.textTertiary,
                    )
                    Text(
                        text = if (currentAccount is Account.Local || syncing) {
                            " "
                        } else {
                            unsyncedCount.toString()
                        },
                        style = MoeTypography.title,
                        color = colors.accentPrimary,
                    )
                }
            }
        }
    }
}
