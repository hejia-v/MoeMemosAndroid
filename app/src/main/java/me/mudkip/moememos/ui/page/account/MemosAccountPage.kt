package me.mudkip.moememos.ui.page.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import me.mudkip.moememos.R
import me.mudkip.moememos.data.api.MemosProfile
import me.mudkip.moememos.data.model.MemosAccount
import me.mudkip.moememos.ext.string
import me.mudkip.moememos.ui.component.MemosIcon
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeRadius
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MemosAccountPage(
    innerPadding: PaddingValues,
    account: MemosAccount,
    profile: MemosProfile?,
    okHttpClient: OkHttpClient,
    showSwitchAccountButton: Boolean,
    onSwitchAccount: () -> Unit,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    val colors = MoeDesignTokens.colors
    val accountHost = account.host.toHttpUrlOrNull()?.host.orEmpty()
    val accountName = account.name.ifBlank { accountHost }
    val accountAvatarUrl = resolveAvatarUrl(account.host, account.avatarUrl)
    val imageLoader = remember(context, okHttpClient) {
        ImageLoader.Builder(context)
            .components {
                add(
                    OkHttpNetworkFetcherFactory(
                        callFactory = { okHttpClient }
                    )
                )
            }
            .build()
    }

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
                contentPadding = PaddingValues(MoeSpacing.xl),
                containerColor = colors.bgSurface,
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (accountAvatarUrl.isNullOrBlank()) {
                            Icon(
                                imageVector = Icons.Outlined.AccountCircle,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(MoeSpacing.xxxl)
                                    .clip(MoeRadius.shapeFull),
                                tint = colors.accentPrimary,
                            )
                        } else {
                            AsyncImage(
                                model = accountAvatarUrl,
                                imageLoader = imageLoader,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(MoeSpacing.xxxl)
                                    .clip(MoeRadius.shapeFull),
                            )
                        }
                        Column(Modifier.padding(start = MoeSpacing.md)) {
                            Text(
                                accountName,
                                style = MoeTypography.headline,
                                color = colors.textPrimary,
                            )
                            Text(
                                accountHost,
                                style = MoeTypography.body,
                                color = colors.textSecondary,
                            )
                        }
                    }
                    if (profile?.version?.isNotEmpty() == true) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = MoeSpacing.md),
                        ) {
                            Icon(
                                imageVector = MemosIcon,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = MoeSpacing.sm)
                                    .clip(MoeRadius.shapeFull),
                                tint = colors.textSecondary,
                            )
                            Text(
                                "memos v${profile.version}",
                                modifier = Modifier.padding(top = 5.dp),
                                style = MoeTypography.caption,
                                color = colors.textTertiary,
                            )
                        }
                    }
                }
            }
        }

        if (showSwitchAccountButton) {
            item {
                FilledTonalButton(
                    onClick = onSwitchAccount,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = colors.accentSoft,
                        contentColor = colors.accentPrimary,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = MoeSpacing.sm),
                    contentPadding = PaddingValues(MoeSpacing.md)
                ) {
                    Text(R.string.switch_account.string)
                }
            }
        }

        item {
            FilledTonalButton(
                onClick = onSignOut,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = colors.bgPressed,
                    contentColor = colors.accentDanger
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MoeSpacing.sm),
                contentPadding = PaddingValues(MoeSpacing.md)
            ) {
                Text(R.string.sign_out.string)
            }
        }
    }
}

private fun resolveAvatarUrl(host: String, avatarUrl: String): String? {
    if (avatarUrl.isBlank()) {
        return null
    }
    if (avatarUrl.toHttpUrlOrNull() != null || "://" in avatarUrl) {
        return avatarUrl
    }
    val baseUrl = host.toHttpUrlOrNull() ?: return avatarUrl
    return runCatching {
        baseUrl.toUrl().toURI().resolve(avatarUrl).toString()
    }.getOrDefault(avatarUrl)
}
