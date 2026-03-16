package me.mudkip.moememos.ui.page.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Source
import androidx.compose.material.icons.outlined.Web
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.mudkip.moememos.R
import me.mudkip.moememos.data.model.Account
import me.mudkip.moememos.data.model.AppThemeMode
import me.mudkip.moememos.data.model.MemoEditGesture
import me.mudkip.moememos.data.model.Settings
import me.mudkip.moememos.ext.popBackStackIfLifecycleIsResumed
import me.mudkip.moememos.ext.settingsDataStore
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
fun SettingsPage(
    navController: NavHostController
) {
    val userStateViewModel = LocalUserState.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val scope = rememberCoroutineScope()
    val accounts by userStateViewModel.accounts.collectAsState()
    val currentAccount by userStateViewModel.currentAccount.collectAsState()
    val settings by context.settingsDataStore.data.collectAsState(initial = Settings())
    val colors = MoeDesignTokens.colors
    var showEditGestureDialog by remember { mutableStateOf(false) }
    var showThemeModeDialog by remember { mutableStateOf(false) }
    val currentEditGesture = settings.usersList
        .firstOrNull { it.accountKey == settings.currentUser }
        ?.settings
        ?.editGesture
        ?: MemoEditGesture.NONE
    val currentThemeMode = settings.themeMode
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = colors.bgApp,
        topBar = {
            MoeAppBar(
                title = R.string.settings.string,
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStackIfLifecycleIsResumed(lifecycleOwner)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = R.string.back.string
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                start = MoeSpacing.xl,
                top = innerPadding.calculateTopPadding() + MoeSpacing.sm,
                end = MoeSpacing.xl,
                bottom = innerPadding.calculateBottomPadding() + MoeSpacing.xxxl,
            )
        ) {
            item {
                MoeCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = MoeSpacing.md),
                    contentPadding = PaddingValues(
                        horizontal = MoeSpacing.xl,
                        vertical = MoeSpacing.lg,
                    ),
                    containerColor = colors.bgSurface,
                ) {
                    Text(
                        text = currentAccount?.toUser()?.name ?: R.string.local_account.string,
                        style = MoeTypography.title,
                        color = colors.textPrimary,
                    )
                    Text(
                        text = R.string.settings.string,
                        style = MoeTypography.body,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(top = MoeSpacing.xs),
                    )
                }
            }

            item {
                SettingsSectionTitle(text = R.string.accounts.string)
            }

            items(accounts, key = { it.accountKey() }) { account ->
                when (account) {
                    is Account.MemosV0 -> {
                        SettingItem(
                            icon = MemosIcon,
                            text = account.info.name,
                            trailingIcon = {
                                if (currentAccount?.accountKey() == account.accountKey()) {
                                    SelectedIndicator()
                                }
                            }
                        ) {
                            navController.navigate("${RouteName.ACCOUNT}?accountKey=${account.accountKey()}")
                        }
                    }

                    is Account.MemosV1 -> {
                        SettingItem(
                            icon = MemosIcon,
                            text = account.info.name,
                            trailingIcon = {
                                if (currentAccount?.accountKey() == account.accountKey()) {
                                    SelectedIndicator()
                                }
                            }
                        ) {
                            navController.navigate("${RouteName.ACCOUNT}?accountKey=${account.accountKey()}")
                        }
                    }

                    is Account.Local -> {
                        SettingItem(
                            icon = Icons.Outlined.Home,
                            text = R.string.local_account.string,
                            trailingIcon = {
                                if (currentAccount?.accountKey() == account.accountKey()) {
                                    SelectedIndicator()
                                }
                            }
                        ) {
                            navController.navigate("${RouteName.ACCOUNT}?accountKey=${account.accountKey()}")
                        }
                    }
                }
            }

            item {
                SettingItem(icon = Icons.Outlined.PersonAdd, text = R.string.add_account.string) {
                    navController.navigate(RouteName.ADD_ACCOUNT)
                }
            }

            item {
                SettingsSectionTitle(text = R.string.preferences.string)
            }

            item {
                SettingItem(
                    icon = Icons.Outlined.DarkMode,
                    text = R.string.dark_mode.string,
                    trailingIcon = {
                        Text(
                            text = currentThemeMode.titleResource.string,
                            color = colors.textSecondary,
                            style = MoeTypography.label,
                        )
                    }
                ) {
                    showThemeModeDialog = true
                }
            }

            item {
                SettingItem(
                    icon = Icons.Outlined.Edit,
                    text = R.string.edit_gesture.string,
                    trailingIcon = {
                        Text(
                            text = currentEditGesture.titleResource.string,
                            color = colors.textSecondary,
                            style = MoeTypography.label,
                        )
                    }
                ) {
                    showEditGestureDialog = true
                }
            }

            item {
                SettingsSectionTitle(text = R.string.about.string)
            }

            item {
                SettingItem(icon = Icons.Outlined.Web, text = R.string.website.string) {
                    uriHandler.openUri("https://memos.moe")
                }
            }

            item {
                SettingItem(icon = Icons.Outlined.Lock, text = R.string.privacy_policy.string) {
                    uriHandler.openUri("https://memos.moe/privacy")
                }
            }

            item {
                SettingItem(icon = Icons.Outlined.Source, text = R.string.acknowledgements.string) {
                    uriHandler.openUri("https://memos.moe/android-acknowledgements")
                }
            }

            item {
                SettingItem(icon = Icons.Outlined.BugReport, text = R.string.report_an_issue.string) {
                    uriHandler.openUri("https://github.com/mudkipme/MoeMemosAndroid/issues")
                }
            }
        }
    }

    if (showThemeModeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeModeDialog = false },
            title = { Text(R.string.dark_mode.string) },
            text = {
                LazyColumn {
                    items(AppThemeMode.entries) { mode ->
                        TextButton(
                            onClick = {
                                showThemeModeDialog = false
                                scope.launch(Dispatchers.IO) {
                                    context.settingsDataStore.updateData { existingSettings ->
                                        existingSettings.copy(themeMode = mode)
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = mode.titleResource.string,
                                color = if (mode == currentThemeMode) {
                                    colors.accentPrimary
                                } else {
                                    colors.textPrimary
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeModeDialog = false }) {
                    Text(R.string.close.string)
                }
            }
        )
    }

    if (showEditGestureDialog) {
        AlertDialog(
            onDismissRequest = { showEditGestureDialog = false },
            title = { Text(R.string.edit_gesture.string) },
            text = {
                LazyColumn {
                    items(MemoEditGesture.entries) { gesture ->
                        TextButton(
                            onClick = {
                                showEditGestureDialog = false
                                scope.launch(Dispatchers.IO) {
                                    context.settingsDataStore.updateData { existingSettings ->
                                        val userIndex = existingSettings.usersList.indexOfFirst { user ->
                                            user.accountKey == existingSettings.currentUser
                                        }
                                        if (userIndex == -1) {
                                            return@updateData existingSettings
                                        }
                                        val users = existingSettings.usersList.toMutableList()
                                        val user = users[userIndex]
                                        users[userIndex] = user.copy(
                                            settings = user.settings.copy(editGesture = gesture)
                                        )
                                        existingSettings.copy(usersList = users)
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = gesture.titleResource.string,
                                color = if (gesture == currentEditGesture) {
                                    colors.accentPrimary
                                } else {
                                    colors.textPrimary
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showEditGestureDialog = false }) {
                    Text(R.string.close.string)
                }
            }
        )
    }
}

@Composable
private fun SettingsSectionTitle(text: String) {
    val colors = MoeDesignTokens.colors

    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = MoeSpacing.lg, bottom = MoeSpacing.xs),
        style = MoeTypography.label,
        color = colors.textSecondary
    )
}

@Composable
private fun SelectedIndicator() {
    val colors = MoeDesignTokens.colors

    Icon(
        imageVector = Icons.Outlined.Check,
        contentDescription = R.string.selected.string,
        modifier = Modifier.padding(start = MoeSpacing.lg),
        tint = colors.textSecondary,
    )
}

private val MemoEditGesture.titleResource: Int
    get() = when (this) {
        MemoEditGesture.NONE -> R.string.edit_gesture_none
        MemoEditGesture.SINGLE -> R.string.edit_gesture_single
        MemoEditGesture.DOUBLE -> R.string.edit_gesture_double
        MemoEditGesture.LONG -> R.string.edit_gesture_long
    }

private val AppThemeMode.titleResource: Int
    get() = when (this) {
        AppThemeMode.SYSTEM -> R.string.theme_mode_system
        AppThemeMode.LIGHT -> R.string.theme_mode_light
        AppThemeMode.DARK -> R.string.theme_mode_dark
    }
