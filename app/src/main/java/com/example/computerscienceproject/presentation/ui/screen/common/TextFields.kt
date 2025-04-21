@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.computerscienceproject.presentation.ui.screen.common

import android.os.Build
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.computerscienceproject.R
import com.example.computerscienceproject.presentation.ui.theme.ComputerScienceProjectTheme
import com.example.computerscienceproject.presentation.ui.theme.lightGrey
import com.example.computerscienceproject.presentation.ui.theme.lightGrey2

@Composable
fun textFieldColors(
    focusedContainerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    unfocusedContainerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor: Color = MaterialTheme.colorScheme.outline,
    unfocusedTextColor: Color = MaterialTheme.colorScheme.onSurface,
    cursorColor: Color = MaterialTheme.colorScheme.primary,
    focusedTextColor: Color = MaterialTheme.colorScheme.onPrimary,
    unfocusedLeadingIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    focusedLeadingIconColor: Color = MaterialTheme.colorScheme.primary, // assuming Brand is Color.Blue for simplicity
    unfocusedTrailingIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    focusedTrailingIconColor: Color = MaterialTheme.colorScheme.primary,
    disabledBorderColor : Color = MaterialTheme.colorScheme.outline,
    disabledContainerColor : Color = MaterialTheme.colorScheme.surfaceContainer,
    errorContainerColor : Color = MaterialTheme.colorScheme.surfaceContainer,
    errorBorderColor: Color = MaterialTheme.colorScheme.error,
    errorCursorColor: Color = MaterialTheme.colorScheme.error,
    errorTextColor: Color = MaterialTheme.colorScheme.onSurface,
) =
    OutlinedTextFieldDefaults.colors(
        focusedContainerColor = focusedContainerColor,
        unfocusedContainerColor = unfocusedContainerColor,
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
        unfocusedTextColor = unfocusedTextColor,
        cursorColor = cursorColor,
        focusedTextColor = focusedTextColor,
        unfocusedLeadingIconColor = unfocusedLeadingIconColor,
        focusedLeadingIconColor = focusedLeadingIconColor,
        unfocusedTrailingIconColor = unfocusedTrailingIconColor,
        focusedTrailingIconColor = focusedTrailingIconColor,
        errorContainerColor = errorContainerColor,
        errorBorderColor = errorBorderColor,
        errorTextColor = errorTextColor,
        errorCursorColor = errorCursorColor,
        disabledBorderColor = disabledBorderColor,
        disabledContainerColor = disabledContainerColor
    )



@Composable
private fun BaseOutlinedTextField(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier.padding(vertical = 14.dp, horizontal = 12.dp),
    text: String = "",
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall,
    hint: String = "",
    hintTextStyle: TextStyle = MaterialTheme.typography.titleSmall,
    hintTextColor: Color = lightGrey,
    colors: TextFieldColors = textFieldColors(),
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    shape: Shape = RoundedCornerShape(7.dp),
    imeAction: ImeAction = ImeAction.Done,
    borderThick: Dp = 1.dp,
    leadingIcon: Int? = null,
    leadingIconSize: Dp = 20.dp,
    leadingIconPadding: Dp = 0.dp,
    trailingIcon: Int? = null,
    customTrailingIconColor: Color? = null,
    trailingIconSize: Dp = 22.dp,
    trailingIconPadding: Dp = 10.dp,
    trailingIconOnClick: () -> Unit = {},
    customTrailingIcon: @Composable (() -> Unit)? = null,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    clickable: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    isFocusedChange: (Boolean) -> Unit = {},
    onValueChange: (String) -> Unit = {},
    onClick: () -> Unit = {},
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

    val isFocused = interactionSource.collectIsFocusedAsState().value

    isFocusedChange(isFocused)

        BasicTextField(
            value = text,
            onValueChange = {
                onValueChange(it)
            },
            textStyle = textStyle.copy(color = textColor),
            modifier = modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = clickable
                ) {
                    onClick()
                },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType).copy(imeAction = imeAction),
            visualTransformation = visualTransformation,
            cursorBrush = SolidColor(colors.cursorColor),
            interactionSource = interactionSource,
            enabled = isEnabled,
            readOnly = readOnly,
            singleLine = singleLine
        ) { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = text,
                innerTextField = {
                    Box(modifier = textModifier) {
                        if (text.isEmpty()) {
                            Text(
                                modifier = Modifier.align(Alignment.TopStart),
                                text = hint,
                                color = hintTextColor,
                                style = hintTextStyle
                            )
                        }
                        innerTextField()
                    }
                },
                enabled = isEnabled,
                singleLine = singleLine,
                visualTransformation = visualTransformation,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(0.dp),
                container = {
                    OutlinedTextFieldDefaults.ContainerBox(
                        enabled = isEnabled,
                        isError = isError,
                        interactionSource = interactionSource,
                        colors = colors,
                        shape = shape,
                        focusedBorderThickness = borderThick,
                        unfocusedBorderThickness = borderThick
                    )
                },
                prefix = leadingIcon?.let {
                    {
                        Icon(
                            painter = painterResource(id = leadingIcon),
                            contentDescription = "Leading Icon",
                            modifier = Modifier
                                .padding(start = leadingIconPadding)
                                .size(leadingIconSize),
                            tint = if (isFocused || isError) colors.focusedLeadingIconColor else colors.unfocusedLeadingIconColor
                        )
                    }
                },
                suffix =
                if (trailingIcon != null) {
                    {
                        Icon(
                            painter = painterResource(id = trailingIcon),
                            modifier = Modifier
                                .padding(end = trailingIconPadding)
                                .size(trailingIconSize)
                                .clickable { trailingIconOnClick() },
                            contentDescription = "Trailing Icon",
                            tint = customTrailingIconColor ?: (if (isFocused || isError) colors.focusedTrailingIconColor else colors.unfocusedTrailingIconColor),
                        )
                    }
                } else if (customTrailingIcon != null) {
                    {
                        customTrailingIcon()
                    }
                } else {
                    null
                },
            )
        }
}

@Composable
fun PrimaryTextFieldCheckbox(
    modifier: Modifier = Modifier,
    text : String,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp),
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit = {}) {

    val borderStroke = if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline

    BaseOutlinedTextField(
        modifier = modifier,
        text = text,
        textStyle = textStyle,
        hint = "",
        borderThick = if (checked) 2.dp else 1.dp,
        colors = textFieldColors(unfocusedBorderColor = borderStroke,focusedBorderColor = borderStroke, disabledBorderColor = borderStroke),
        trailingIcon = if (checked) R.drawable.checkbox_icon else null,
        customTrailingIconColor = Color.Unspecified,
        trailingIconSize = 20.dp,
        trailingIconPadding = 16.dp,
        trailingIconOnClick = { onCheckedChange(!checked) },
        onClick = { onCheckedChange(!checked) },
        readOnly = false,
        isEnabled = false
    )
}

@Composable
fun PrimaryOutlinedTextField(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier.padding(vertical = 16.dp, horizontal = 10.dp),
    text : String = "",
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall,
    hint: String = "",
    hintTextStyle: TextStyle = MaterialTheme.typography.titleSmall,
    hintTextColor: Color = lightGrey,
    title: String = "",
    titleStyle: TextStyle = MaterialTheme.typography.titleSmall,
    error: String = "",
    colors: TextFieldColors = textFieldColors(),
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    shape: Shape = RoundedCornerShape(7.dp),
    singleLine: Boolean = true,
    leadingIcon: Int? = null,
    leadingIconSize: Dp = 20.dp,
    leadingIconPadding: Dp = 14.dp,
    trailingIcon: Int? = null,
    trailingIconSize: Dp = 20.dp,
    trailingIconPadding: Dp = 14.dp,
    trailingIconOnClick: () -> Unit = {},
    customTrailingIcon: @Composable (() -> Unit)? = null,
    titleHorizontalPadding: Dp = 0.dp,
    borderThick: Dp = 1.dp,
    isEnabled: Boolean = true,
    readOnly: Boolean = false,
    onFocusChange: (Boolean) -> Unit = {},
    onClick: () -> Unit = {},
    onValueChange: (String) -> Unit = {},
) {
    Column {
        if (title.isNotEmpty()) {
            TitleOnTopOfInputField(title = title, textStyle = titleStyle, horizontalPadding = titleHorizontalPadding)
        }

        BaseOutlinedTextField(
            modifier = modifier,
            textModifier = textModifier,
            text = text,
            textColor = textColor,
            textStyle = textStyle,
            hint = hint,
            hintTextColor = hintTextColor,
            colors = colors,
            singleLine = singleLine,
            readOnly = readOnly,
            isEnabled = isEnabled,
            isError = error.isNotEmpty(),
            shape = shape,
            borderThick = borderThick,
            hintTextStyle = hintTextStyle,
            leadingIcon = leadingIcon,
            leadingIconSize = leadingIconSize,
            leadingIconPadding = leadingIconPadding,
            trailingIcon = trailingIcon,
            trailingIconPadding = trailingIconPadding,
            trailingIconOnClick = trailingIconOnClick,
            customTrailingIcon = customTrailingIcon,
            trailingIconSize = trailingIconSize,
            keyboardType = keyboardType,
            visualTransformation = visualTransformation,
            isFocusedChange = onFocusChange,
            onValueChange = { onValueChange(it) },
            onClick = onClick
        )

        if (error.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(
                    top = 6.dp,
                    start = titleHorizontalPadding,
                    end = titleHorizontalPadding
                ),
                text = error,
                style = textStyle,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCheckbox() {

    ComputerScienceProjectTheme {
        PrimaryTextFieldCheckbox(text = "Male", checked = true)
    }

}