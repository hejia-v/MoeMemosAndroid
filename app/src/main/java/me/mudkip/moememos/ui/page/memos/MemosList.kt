package me.mudkip.moememos.ui.page.memos

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.mudkip.moememos.R
import me.mudkip.moememos.data.model.Account
import me.mudkip.moememos.data.model.MemoEditGesture
import me.mudkip.moememos.data.model.Settings
import me.mudkip.moememos.ext.settingsDataStore
import me.mudkip.moememos.ext.string
import me.mudkip.moememos.ui.component.MemosCard
import me.mudkip.moememos.ui.component.MemosCardStyle
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.page.common.LocalRootNavController
import me.mudkip.moememos.ui.page.common.RouteName
import me.mudkip.moememos.viewmodel.LocalMemos
import me.mudkip.moememos.viewmodel.LocalUserState
import me.mudkip.moememos.viewmodel.ManualSyncResult
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemosList(
    contentPadding: PaddingValues,
    lazyListState: LazyListState = rememberLazyListState(),
    tag: String? = null,
    searchString: String? = null,
    onRefresh: (suspend () -> Unit)? = null,
    onTagClick: ((String) -> Unit)? = null,
    headerContent: (@Composable () -> Unit)? = null,
    layoutStyle: MemosListLayoutStyle = MemosListLayoutStyle.List,
) {
    val context = LocalContext.current
    val navController = LocalRootNavController.current
    val viewModel = LocalMemos.current
    val userStateViewModel = LocalUserState.current
    val currentAccount by userStateViewModel.currentAccount.collectAsState()
    val settings by context.settingsDataStore.data.collectAsState(initial = Settings())
    val editGesture = settings.usersList
        .firstOrNull { it.accountKey == settings.currentUser }
        ?.settings
        ?.editGesture
    val refreshState = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    var syncAlert by remember { mutableStateOf<PullRefreshSyncAlert?>(null) }
    val filteredMemos = remember(viewModel.memos.toList(), tag, searchString) {
        val pinned = viewModel.memos.filter { it.pinned }
        val nonPinned = viewModel.memos.filter { !it.pinned }
        var fullList = pinned + nonPinned

        tag?.let { selectedTag ->
            fullList = fullList.filter { memo ->
                memo.content.contains("#$selectedTag") || memo.content.contains("#$selectedTag/")
            }
        }

        searchString?.let { query ->
            if (query.isNotEmpty()) {
                fullList = fullList.filter { memo ->
                    memo.content.contains(query, true)
                }
            }
        }

        fullList
    }
    var listTopId: String? by rememberSaveable { mutableStateOf(null) }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            scope.launch {
                if (onRefresh != null) {
                    onRefresh()
                } else {
                    when (val result = viewModel.refreshMemos()) {
                        ManualSyncResult.Completed -> Unit
                        is ManualSyncResult.Blocked -> {
                            syncAlert = PullRefreshSyncAlert.Blocked(result.message)
                        }

                        is ManualSyncResult.RequiresConfirmation -> {
                            syncAlert = PullRefreshSyncAlert.RequiresConfirmation(result.version, result.message)
                        }

                        is ManualSyncResult.Failed -> {
                            syncAlert = PullRefreshSyncAlert.Failed(result.message)
                        }
                    }
                }
                isRefreshing = false
            }
        },
        state = refreshState,
        modifier = Modifier.padding(contentPadding)
    ) {
        when (layoutStyle) {
            MemosListLayoutStyle.List -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(
                        top = MoeSpacing.sm,
                        bottom = MoeSpacing.xxxl,
                    )
                ) {
                    if (headerContent != null) {
                        item(key = "header") {
                            headerContent()
                        }
                    }

                    if (filteredMemos.isEmpty()) {
                        item(key = "empty") {
                            EmptyMemosCard(
                                modifier = Modifier.padding(horizontal = MoeSpacing.xl, vertical = MoeSpacing.sm)
                            )
                        }
                    }

                    items(filteredMemos, key = { it.identifier }) { memo ->
                        MemoListItem(
                            memoIdentifier = memo.identifier,
                            onOpen = {
                                navController.navigate("${RouteName.MEMO_DETAIL}?memoId=${Uri.encode(it)}")
                            },
                            editGesture = editGesture ?: MemoEditGesture.NONE,
                            showSyncStatus = currentAccount !is Account.Local,
                            onTagClick = onTagClick,
                        )
                    }
                }
            }

            MemosListLayoutStyle.HomeGrid -> {
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val minCardWidth = if (maxWidth < 520.dp) 150.dp else 184.dp

                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Adaptive(minSize = minCardWidth),
                        modifier = Modifier.fillMaxSize(),
                        verticalItemSpacing = MoeSpacing.md,
                        horizontalArrangement = Arrangement.spacedBy(MoeSpacing.md),
                        contentPadding = PaddingValues(
                            start = MoeSpacing.xl,
                            end = MoeSpacing.xl,
                            top = MoeSpacing.sm,
                            bottom = MoeSpacing.xxxl,
                        ),
                    ) {
                        if (headerContent != null) {
                            item(
                                key = "header",
                                span = StaggeredGridItemSpan.FullLine,
                            ) {
                                headerContent()
                            }
                        }

                        if (filteredMemos.isEmpty()) {
                            item(
                                key = "empty",
                                span = StaggeredGridItemSpan.FullLine,
                            ) {
                                EmptyMemosCard()
                            }
                        } else {
                            items(filteredMemos, key = { it.identifier }) { memo ->
                                MemoListItem(
                                    memoIdentifier = memo.identifier,
                                    onOpen = {
                                        navController.navigate("${RouteName.MEMO_DETAIL}?memoId=${Uri.encode(it)}")
                                    },
                                    editGesture = editGesture ?: MemoEditGesture.NONE,
                                    showSyncStatus = currentAccount !is Account.Local,
                                    onTagClick = onTagClick,
                                    style = MemosCardStyle.HomeCompact,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            Timber.d(it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadMemos()
    }

    LaunchedEffect(filteredMemos.firstOrNull()?.identifier) {
        if (listTopId != null && filteredMemos.isNotEmpty() && listTopId != filteredMemos.first().identifier) {
            lazyListState.scrollToItem(0)
        }

        listTopId = filteredMemos.firstOrNull()?.identifier
    }

    when (val alert = syncAlert) {
        null -> Unit
        is PullRefreshSyncAlert.Blocked -> {
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

        is PullRefreshSyncAlert.RequiresConfirmation -> {
            AlertDialog(
                onDismissRequest = { syncAlert = null },
                title = { Text(R.string.unsupported_memos_version_title.string) },
                text = { Text(alert.message) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            syncAlert = null
                            scope.launch {
                                when (val result = viewModel.refreshMemos(alert.version)) {
                                    ManualSyncResult.Completed -> Unit
                                    is ManualSyncResult.Blocked -> {
                                        syncAlert = PullRefreshSyncAlert.Blocked(result.message)
                                    }

                                    is ManualSyncResult.RequiresConfirmation -> {
                                        syncAlert = PullRefreshSyncAlert.RequiresConfirmation(result.version, result.message)
                                    }

                                    is ManualSyncResult.Failed -> {
                                        syncAlert = PullRefreshSyncAlert.Failed(result.message)
                                    }
                                }
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

        is PullRefreshSyncAlert.Failed -> {
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

@Composable
private fun MemoListItem(
    memoIdentifier: String,
    onOpen: (String) -> Unit,
    editGesture: MemoEditGesture,
    showSyncStatus: Boolean,
    onTagClick: ((String) -> Unit)?,
    style: MemosCardStyle = MemosCardStyle.Standard,
) {
    val viewModel = LocalMemos.current
    val memo = viewModel.memos.firstOrNull { it.identifier == memoIdentifier } ?: return

    MemosCard(
        memo = memo,
        onClick = { onOpen(it.identifier) },
        editGesture = editGesture,
        previewMode = true,
        showSyncStatus = showSyncStatus,
        onTagClick = onTagClick,
        style = style,
    )
}

enum class MemosListLayoutStyle {
    List,
    HomeGrid,
}

@Composable
private fun EmptyMemosCard(modifier: Modifier = Modifier) {
    val colors = MoeDesignTokens.colors

    MoeCard(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            horizontal = MoeSpacing.xl,
            vertical = MoeSpacing.xxl,
        )
    ) {
        Column {
            Text(
                text = R.string.no_memos.string,
                style = MoeTypography.title,
                color = colors.textPrimary,
            )
            Text(
                text = R.string.memos_home_subtitle.string,
                style = MoeTypography.body,
                color = colors.textSecondary,
                modifier = Modifier.padding(top = MoeSpacing.sm),
            )
        }
    }
}

private sealed class PullRefreshSyncAlert {
    data class Blocked(val message: String) : PullRefreshSyncAlert()
    data class RequiresConfirmation(val version: String, val message: String) : PullRefreshSyncAlert()
    data class Failed(val message: String) : PullRefreshSyncAlert()
}
