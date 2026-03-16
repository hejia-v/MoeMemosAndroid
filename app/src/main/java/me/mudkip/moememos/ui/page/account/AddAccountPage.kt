package me.mudkip.moememos.ui.page.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.launch
import me.mudkip.moememos.R
import me.mudkip.moememos.data.model.Account
import me.mudkip.moememos.ext.popBackStackIfLifecycleIsResumed
import me.mudkip.moememos.ext.string
import me.mudkip.moememos.ui.component.MemosIcon
import me.mudkip.moememos.ui.designsystem.component.MoeAppBar
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.page.common.RouteName
import me.mudkip.moememos.viewmodel.LocalUserState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountPage(
    navController: NavHostController,
) {
    val userStateViewModel = LocalUserState.current
    val accounts by userStateViewModel.accounts.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val coroutineScope = rememberCoroutineScope()
    val hasLocalAccount = accounts.any { it is Account.Local }
    val colors = MoeDesignTokens.colors

    fun toMemos() {
        navController.navigate(RouteName.MEMOS) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = colors.bgApp,
        topBar = {
            MoeAppBar(
                title = if (accounts.isEmpty()) R.string.moe_memos.string else R.string.add_account.string,
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    if (accounts.isNotEmpty()) {
                        IconButton(onClick = {
                            navController.popBackStackIfLifecycleIsResumed(lifecycleOwner)
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = R.string.back.string
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = MoeSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(MoeSpacing.md),
            contentPadding = PaddingValues(vertical = MoeSpacing.md),
        ) {
            if (!hasLocalAccount) {
                item {
                    MoeCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                coroutineScope.launch {
                                    userStateViewModel.addLocalAccount()
                                        .suspendOnSuccess { toMemos() }
                                }
                            },
                        contentPadding = PaddingValues(0.dp),
                        containerColor = colors.bgSurface,
                    ) {
                        ListItem(
                            headlineContent = {
                                Text(R.string.add_local_account.string, style = MoeTypography.title)
                            },
                            supportingContent = {
                                Text(
                                    R.string.local_account_description.string,
                                    style = MoeTypography.body,
                                    color = colors.textSecondary
                                )
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.Home,
                                    contentDescription = null,
                                    tint = colors.accentPrimary
                                )
                            },
                        )
                    }
                }
            }

            item {
                MoeCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(RouteName.LOGIN) },
                    contentPadding = PaddingValues(0.dp),
                    containerColor = colors.bgSurface,
                ) {
                    ListItem(
                        headlineContent = {
                            Text(R.string.add_memos_account.string, style = MoeTypography.title)
                        },
                        supportingContent = {
                            Text(
                                R.string.memos_account_description.string,
                                style = MoeTypography.body,
                                color = colors.textSecondary
                            )
                        },
                        leadingContent = {
                            Icon(
                                imageVector = MemosIcon,
                                contentDescription = null,
                                tint = colors.accentPrimary
                            )
                        }
                    )
                }
            }
        }
    }
}
