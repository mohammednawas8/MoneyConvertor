package com.mc.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mc.designsystem.theme.MoneyConvertorTheme
import kotlin.math.sin

@Composable
fun MCTextField(
    modifier: Modifier = Modifier,
    value: String,
    singleLine: Boolean = false,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    BasicTextField(
        value = TextFieldValue(value, TextRange(value.length)),
        onValueChange = { onValueChange(it.text) },
        modifier = modifier
            .clip(RoundedCornerShape(7.dp))
            .background(Color(0xFFEFEFEF))
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 20.dp),
        textStyle = MaterialTheme.typography.labelLarge,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine
    )
}

@Preview
@Composable
private fun MCTextFieldPreview() {
    MoneyConvertorTheme {
        MCTextField(value = "1500", onValueChange = {})
    }
}






