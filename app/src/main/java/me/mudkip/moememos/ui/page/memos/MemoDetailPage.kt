package me.mudkip.moememos.ui.page.memos

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import me.mudkip.moememos.R
import me.mudkip.moememos.data.model.Account
import me.mudkip.moememos.ext.icon
import me.mudkip.moememos.ext.popBackStackIfLifecycleIsResumed
import me.mudkip.moememos.ext.string
import me.mudkip.moememos.ext.titleResource
import me.mudkip.moememos.ui.component.MemoContent
import me.mudkip.moememos.ui.component.MemosCardActionButton
import me.mudkip.moememos.ui.designsystem.component.MoeAppBar
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeRadius
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.viewmodel.LocalMemos
import me.mudkip.moememos.viewmodel.LocalUserState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoDetailPage(
    navController: NavHostController,
    memoIdentifier: String
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val memosViewModel = LocalMemos.current
    val userStateViewModel = LocalUserState.current
    val currentAccount by userStateViewModel.currentAccount.collectAsState()
    val scope = rememberCoroutineScope()
    val colors = MoeDesignTokens.colors
    val memo = remember(memosViewModel.memos.toList(), memoIdentifier) {
        memosViewModel.memos.firstOrNull { it.identifier == memoIdentifier }
    }
    var hadMemo by rememberSaveable(memoIdentifier) { mutableStateOf(false) }

    LaunchedEffect(memo?.identifier) {
        when {
            memo != null -> hadMemo = true
            hadMemo -> navController.popBackStackIfLifecycleIsResumed(lifecycleOwner)
        }
    }

    Scaffold(
        containerColor = colors.bgApp,
        topBar = {
            MoeAppBar(
                title = R.string.memo.string,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStackIfLifecycleIsResumed(lifecycleOwner) }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = R.string.back.string)
                    }
                },
                actions = {
                    memo?.let { MemosCardActionButton(it) }
                }
            )
        }
    ) { innerPadding ->
        if (memo == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = R.string.memo_not_found.string)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = MoeSpacing.xl, vertical = MoeSpacing.md)
                .verticalScroll(rememberScrollState())
        ) {
            MoeCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(MoeSpacing.xl),
                containerColor = colors.bgSurface,
            ) {
                Column {
                    Text(
                        text = DateUtils.getRelativeTimeSpanString(
                            memo.date.toEpochMilli(),
                            System.currentTimeMillis(),
                            DateUtils.SECOND_IN_MILLIS
                        ).toString(),
                        style = MoeTypography.caption,
                        color = colors.textTertiary,
                    )
                    Row(
                        modifier = Modifier
                            .padding(top = MoeSpacing.md)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = R.string.memo.string,
                            style = MoeTypography.headline,
                            color = colors.textPrimary,
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            text = stringResource(memo.visibility.titleResource),
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
                    if (currentAccount !is Account.Local && memo.needsSync) {
                        Row(
                            modifier = Modifier.padding(top = MoeSpacing.md),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CloudOff,
                                contentDescription = R.string.memo_sync_pending.string,
                                modifier = Modifier.size(18.dp),
                                tint = colors.accentDanger,
                            )
                            Text(
                                text = R.string.memo_sync_pending.string,
                                style = MoeTypography.caption,
                                color = colors.textSecondary,
                                modifier = Modifier.padding(start = MoeSpacing.sm),
                            )
                        }
                    }
                }
            }

            MoeCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MoeSpacing.md, bottom = MoeSpacing.xxl),
                containerColor = colors.bgElevated,
            ) {
                MemoContent(
                    memo = memo,
                    selectable = true,
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
                    }
                )
            }
        }
    }
}
