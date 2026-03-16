package me.mudkip.moememos.ui.page.login

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.PermIdentity
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.launch
import me.mudkip.moememos.BuildConfig
import me.mudkip.moememos.R
import me.mudkip.moememos.ext.popBackStackIfLifecycleIsResumed
import me.mudkip.moememos.ext.string
import me.mudkip.moememos.ext.suspendOnErrorMessage
import me.mudkip.moememos.ui.component.Markdown
import me.mudkip.moememos.ui.designsystem.component.MoeAppBar
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.component.MoeInputField
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeRadius
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.page.common.RouteName
import me.mudkip.moememos.viewmodel.LoginCompatibility
import me.mudkip.moememos.viewmodel.LocalUserState

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    navController: NavHostController
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val lifecycleOwner = LocalLifecycleOwner.current
    val userStateViewModel = LocalUserState.current
    val snackbarState = remember { SnackbarHostState() }
    val colors = MoeDesignTokens.colors

    var host by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(userStateViewModel.host.ifBlank { BuildConfig.DEFAULT_MEMOS_HOST }))
    }

    var accessToken by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    var loginCompatibilityWarning by remember { mutableStateOf<String?>(null) }

    fun normalizedHost(): String {
        val trimmed = host.text.trim()
        return if (trimmed.contains("//")) trimmed else "https://$trimmed"
    }

    fun login(allowHigherV1Version: Boolean = false) = coroutineScope.launch {
        if (host.text.isBlank() || accessToken.text.isBlank()) {
            snackbarState.showSnackbar(R.string.fill_login_form.string)
            return@launch
        }

        val sanitizedHost = normalizedHost()
        host = TextFieldValue(sanitizedHost)

        if (!allowHigherV1Version) {
            when (val compatibility = userStateViewModel.checkLoginCompatibility(sanitizedHost)) {
                LoginCompatibility.Supported -> Unit
                is LoginCompatibility.Unsupported -> {
                    snackbarState.showSnackbar(compatibility.message)
                    return@launch
                }
                is LoginCompatibility.RequiresConfirmation -> {
                    loginCompatibilityWarning = compatibility.message
                    return@launch
                }
            }
        }

        val resp = userStateViewModel.loginMemosWithAccessToken(
            host = sanitizedHost,
            accessToken = accessToken.text.trim(),
            allowHigherV1Version = allowHigherV1Version,
        )
        resp.suspendOnSuccess {
            navController.navigate(RouteName.MEMOS) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
        .suspendOnErrorMessage {
            snackbarState.showSnackbar(it)
        }
    }

    if (loginCompatibilityWarning != null) {
        AlertDialog(
            onDismissRequest = { loginCompatibilityWarning = null },
            title = { Text(text = R.string.unsupported_memos_version_title.string) },
            text = { Text(text = loginCompatibilityWarning ?: "") },
            confirmButton = {
                TextButton(
                    onClick = {
                        loginCompatibilityWarning = null
                        login(allowHigherV1Version = true)
                    }
                ) {
                    Text(R.string.still_login.string)
                }
            },
            dismissButton = {
                TextButton(onClick = { loginCompatibilityWarning = null }) {
                    Text(R.string.cancel.string)
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier
            .imePadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = colors.bgApp,
        snackbarHost = {
            SnackbarHost(hostState = snackbarState)
        },
        topBar = {
            MoeAppBar(
                title = if (userStateViewModel.currentUser != null) R.string.add_account.string else R.string.moe_memos.string,
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    if (userStateViewModel.currentUser != null) {
                        IconButton(onClick = {
                            navController.popBackStackIfLifecycleIsResumed(lifecycleOwner)
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = R.string.back.string)
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = colors.bgOverlay,
                tonalElevation = 0.dp,
                actions = {},
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        onClick = { login() },
                        containerColor = colors.accentPrimary,
                        contentColor = colors.textOnAccent,
                        text = { Text(R.string.add_account.string) },
                        icon = {
                            Icon(
                                Icons.AutoMirrored.Outlined.Login,
                                contentDescription = R.string.add_account.string
                            )
                        }
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(horizontal = MoeSpacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MoeCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .bringIntoViewRequester(bringIntoViewRequester),
                contentPadding = PaddingValues(MoeSpacing.xl),
                containerColor = colors.bgSurface,
            ) {
                Column {
                    Text(
                        text = if (userStateViewModel.currentUser != null) {
                            R.string.add_account.string
                        } else {
                            R.string.moe_memos.string
                        },
                        style = MoeTypography.headline,
                        color = colors.textPrimary,
                    )
                    Markdown(
                        R.string.input_login_information.string,
                        modifier = Modifier.padding(top = MoeSpacing.sm, bottom = MoeSpacing.xl),
                        textAlign = TextAlign.Center
                    )

                    MoeInputField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusEvent { focusState ->
                                if (focusState.isFocused) {
                                    coroutineScope.launch {
                                        bringIntoViewRequester.bringIntoView()
                                    }
                                }
                            },
                        value = host,
                        onValueChange = { host = it },
                        label = R.string.host.string,
                        placeholder = BuildConfig.DEFAULT_MEMOS_HOST,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Uri,
                            imeAction = ImeAction.Next
                        )
                    )

                    MoeInputField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = MoeSpacing.lg)
                            .onFocusEvent { focusState ->
                                if (focusState.isFocused) {
                                    coroutineScope.launch {
                                        bringIntoViewRequester.bringIntoView()
                                    }
                                }
                            },
                        value = accessToken,
                        onValueChange = { accessToken = it },
                        label = R.string.access_token.string,
                        placeholder = "pat_xxx",
                        maxLines = 1,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Go
                        ),
                        keyboardActions = KeyboardActions(onGo = { login() })
                    )

                    Text(
                        text = normalizedHost(),
                        style = MoeTypography.caption,
                        color = colors.textTertiary,
                        modifier = Modifier
                            .padding(top = MoeSpacing.lg)
                            .background(colors.bgElevated, MoeRadius.shapeMd)
                            .padding(horizontal = MoeSpacing.md, vertical = MoeSpacing.sm),
                        textAlign = TextAlign.Center,
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = MoeSpacing.lg),
                        horizontalArrangement = Arrangement.spacedBy(MoeSpacing.sm)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Computer,
                            contentDescription = null,
                            tint = colors.textSecondary,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Text(
                            text = R.string.host.string,
                            style = MoeTypography.caption,
                            color = colors.textSecondary,
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = MoeSpacing.sm),
                        horizontalArrangement = Arrangement.spacedBy(MoeSpacing.sm)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PermIdentity,
                            contentDescription = null,
                            tint = colors.textSecondary,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Text(
                            text = R.string.access_token.string,
                            style = MoeTypography.caption,
                            color = colors.textSecondary,
                        )
                    }
                }
            }
        }
    }
}
