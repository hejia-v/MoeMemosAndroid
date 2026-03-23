package me.mudkip.moememos.ui.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeRadius
import me.mudkip.moememos.ui.designsystem.token.MoeSpacing
import me.mudkip.moememos.ui.designsystem.token.MoeTypography
import me.mudkip.moememos.ui.preview.MoeMemosPreviewTheme

@Composable
fun MoeInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val colors = MoeDesignTokens.colors

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        minLines = minLines,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        shape = MoeRadius.shapeLg,
        textStyle = MoeTypography.body.copy(color = colors.textPrimary),
        label = label?.let {
            {
                Text(
                    text = it,
                    style = MoeTypography.label,
                    color = colors.textSecondary,
                )
            }
        },
        placeholder = placeholder?.let {
            {
                Text(
                    text = it,
                    style = MoeTypography.body,
                    color = colors.textTertiary,
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colors.bgElevated,
            unfocusedContainerColor = colors.bgElevated,
            disabledContainerColor = colors.bgSurface,
            focusedBorderColor = colors.strokeStrong,
            unfocusedBorderColor = colors.strokeSubtle,
            disabledBorderColor = colors.strokeSubtle,
            cursorColor = colors.accentPrimary,
            focusedTextColor = colors.textPrimary,
            unfocusedTextColor = colors.textPrimary,
            focusedLabelColor = colors.textSecondary,
            unfocusedLabelColor = colors.textSecondary,
            focusedPlaceholderColor = colors.textTertiary,
            unfocusedPlaceholderColor = colors.textTertiary,
        ),
    )
}

@Composable
fun MoeInputField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val colors = MoeDesignTokens.colors

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        minLines = minLines,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        shape = MoeRadius.shapeLg,
        textStyle = MoeTypography.body.copy(color = colors.textPrimary),
        label = label?.let {
            {
                Text(
                    text = it,
                    style = MoeTypography.label,
                    color = colors.textSecondary,
                )
            }
        },
        placeholder = placeholder?.let {
            {
                Text(
                    text = it,
                    style = MoeTypography.body,
                    color = colors.textTertiary,
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colors.bgElevated,
            unfocusedContainerColor = colors.bgElevated,
            disabledContainerColor = colors.bgSurface,
            focusedBorderColor = colors.strokeStrong,
            unfocusedBorderColor = colors.strokeSubtle,
            disabledBorderColor = colors.strokeSubtle,
            cursorColor = colors.accentPrimary,
            focusedTextColor = colors.textPrimary,
            unfocusedTextColor = colors.textPrimary,
            focusedLabelColor = colors.textSecondary,
            unfocusedLabelColor = colors.textSecondary,
            focusedPlaceholderColor = colors.textTertiary,
            unfocusedPlaceholderColor = colors.textTertiary,
        ),
    )
}

// ==================== Previews ====================

@PreviewLightDark
@Composable
private fun MoeInputFieldPreview() {
    MoeMemosPreviewTheme {
        var text by remember { mutableStateOf("") }
        Column(
            modifier = Modifier.padding(MoeSpacing.lg)
        ) {
            MoeInputField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth(),
                label = "Username",
                placeholder = "Enter your username"
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun MoeInputFieldWithTextPreview() {
    MoeMemosPreviewTheme {
        var text by remember { mutableStateOf("Hello World") }
        Column(
            modifier = Modifier.padding(MoeSpacing.lg)
        ) {
            MoeInputField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth(),
                label = "Content",
                placeholder = "Type something..."
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun MoeInputFieldMultilinePreview() {
    MoeMemosPreviewTheme {
        var text by remember { mutableStateOf("Line 1\nLine 2\nLine 3") }
        Column(
            modifier = Modifier.padding(MoeSpacing.lg)
        ) {
            MoeInputField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth(),
                label = "Notes",
                minLines = 3,
                maxLines = 5
            )
        }
    }
}
