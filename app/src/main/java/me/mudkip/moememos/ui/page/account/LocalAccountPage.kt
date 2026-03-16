package me.mudkip.moememos.ui.page.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import me.mudkip.moememos.R
import me.mudkip.moememos.ext.string
import me.mudkip.moememos.ui.designsystem.component.MoeCard
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeRadius
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography

@Composable
fun LocalAccountPage(
    innerPadding: PaddingValues,
    showSwitchAccountButton: Boolean,
    onSwitchAccount: () -> Unit,
    onExportLocalAccount: () -> Unit
) {
    val colors = MoeDesignTokens.colors

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
                        Icon(
                            imageVector = Icons.Outlined.Home,
                            contentDescription = null,
                            modifier = Modifier
                                .size(MoeSpacing.xxxl)
                                .padding(end = MoeSpacing.sm),
                            tint = colors.accentPrimary,
                        )
                        Text(
                            R.string.local_account.string,
                            style = MoeTypography.headline,
                            color = colors.textPrimary,
                        )
                    }
                    Text(
                        R.string.local_account_description.string,
                        style = MoeTypography.body,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(top = MoeSpacing.sm)
                    )
                    Text(
                        R.string.local_account_non_removable.string,
                        style = MoeTypography.caption,
                        color = colors.textTertiary,
                        modifier = Modifier.padding(top = MoeSpacing.lg)
                    )
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
            Button(
                onClick = onExportLocalAccount,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.accentPrimary,
                    contentColor = colors.textOnAccent,
                ),
                shape = MoeRadius.shapeLg,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MoeSpacing.sm),
                contentPadding = PaddingValues(MoeSpacing.md)
            ) {
                Text(R.string.export_local_account.string)
            }
        }
    }
}
