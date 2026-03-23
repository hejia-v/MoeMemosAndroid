package me.mudkip.moememos.ui.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeRadius
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.preview.MoeMemosPreviewTheme

@Composable
fun MoeSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val colors = MoeDesignTokens.colors

    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
        snackbar = { data ->
            MoeSnackbar(data = data)
        }
    )
}

@Composable
private fun MoeSnackbar(data: SnackbarData) {
    val colors = MoeDesignTokens.colors

    Snackbar(
        snackbarData = data,
        shape = MoeRadius.shapeLg,
        containerColor = colors.bgSurface,
        contentColor = colors.textPrimary,
        actionColor = colors.accentPrimary,
        modifier = Modifier.padding(MoeSpacing.md),
    )
}

// ==================== Previews ====================

@PreviewLightDark
@Composable
private fun MoeSnackbarHostPreview() {
    MoeMemosPreviewTheme {
        val snackbarHostState = remember { SnackbarHostState() }
        MoeSnackbarHost(hostState = snackbarHostState)
    }
}
