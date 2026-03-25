package me.mudkip.moememos.ui.page.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeMotion
import me.mudkip.moememos.ui.designsystem.token.MoeElevation
import me.mudkip.moememos.ui.designsystem.token.MoeRadius
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography

@Composable
fun SettingItem(
    icon: ImageVector,
    text: String,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    val colors = MoeDesignTokens.colors
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.985f else 1f,
        animationSpec = tween(
            durationMillis = if (pressed) {
                MoeMotion.fast.inWholeMilliseconds.toInt()
            } else {
                MoeMotion.normal.inWholeMilliseconds.toInt()
            }
        ),
        label = "setting_item_scale"
    )

    Card(
        onClick = onClick,
        interactionSource = interactionSource,
        shape = MoeRadius.shapeLg,
        colors = CardDefaults.cardColors(
            containerColor = colors.bgSurface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = MoeElevation.raised,
        ),
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .fillMaxWidth()
            .padding(horizontal = MoeSpacing.xl, vertical = MoeSpacing.xs)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MoeSpacing.xl, vertical = MoeSpacing.lg),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = text,
                modifier = Modifier
                    .background(colors.accentSoft, MoeRadius.shapeMd)
                    .padding(MoeSpacing.sm),
                tint = colors.accentPrimary
            )
            Spacer(modifier = Modifier.width(MoeSpacing.lg))
            Text(
                text,
                style = MoeTypography.title,
                color = colors.textPrimary,
                modifier = Modifier.weight(1f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(MoeSpacing.md))
                trailingIcon()
            }
        }
    }
}
