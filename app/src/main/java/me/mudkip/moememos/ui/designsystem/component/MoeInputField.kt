package me.mudkip.moememos.ui.designsystem.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.designsystem.token.MoeRadius
import me.mudkip.moememos.ui.designsystem.token.MoeTypography

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
