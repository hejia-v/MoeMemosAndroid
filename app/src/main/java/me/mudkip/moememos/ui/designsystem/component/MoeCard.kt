package me.mudkip.moememos.ui.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeElevation
import me.mudkip.moememos.ui.designsystem.token.MoeRadius
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.preview.MoeMemosPreviewTheme

@Composable
fun MoeCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    border: BorderStroke? = null,
    containerColor: Color = MoeDesignTokens.colors.bgElevated,
    shape: Shape = MoeRadius.shapeLg,
    elevation: Dp = MoeElevation.raised,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = shape,
        border = border,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation,
        ),
    ) {
        Box(modifier = Modifier.padding(contentPadding)) {
            content()
        }
    }
}

@PreviewLightDark
@Composable
private fun MoeCardPreview() {
    MoeMemosPreviewTheme {
        val colors = MoeDesignTokens.colors
        MoeCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MoeSpacing.md),
            contentPadding = PaddingValues(MoeSpacing.lg),
        ) {
            Text(
                text = "This is a card content",
                style = MoeTypography.body,
                color = colors.textPrimary
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun MoeCardWithBorderPreview() {
    MoeMemosPreviewTheme {
        val colors = MoeDesignTokens.colors
        MoeCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MoeSpacing.md),
            contentPadding = PaddingValues(MoeSpacing.lg),
            border = BorderStroke(1.dp, colors.accentPrimary),
            containerColor = colors.bgSurface
        ) {
            Text(
                text = "Card with border",
                style = MoeTypography.body,
                color = colors.textPrimary
            )
        }
    }
}
