package me.mudkip.moememos.ui.designsystem.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoeAppBar(
    title: String,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    val colors = MoeDesignTokens.colors

    TopAppBar(
        title = {
            Text(
                text = title,
                style = MoeTypography.title,
                color = colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colors.bgOverlay,
            scrolledContainerColor = colors.bgOverlay,
            navigationIconContentColor = colors.textPrimary,
            titleContentColor = colors.textPrimary,
            actionIconContentColor = colors.textPrimary,
        ),
    )
}
