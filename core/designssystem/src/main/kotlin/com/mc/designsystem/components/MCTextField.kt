package com.mc.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mc.designsystem.theme.Gray
import com.mc.designsystem.theme.MoneyConvertorTheme

@Composable
fun MCTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = remember { FocusRequester() },
    singleLine: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(horizontal = 15.dp),
    label: String? = null,
    shape: Shape = RoundedCornerShape(7.dp),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions(),
    containerColor: Color = Color(0xFFEFEFEF)
) {
    BasicTextField(
        value = TextFieldValue(value, TextRange(value.length)),
        onValueChange = { onValueChange(it.text) },
        modifier = modifier
            .clip(shape)
            .background(containerColor)
            .fillMaxWidth()
            .focusRequester(focusRequester)
        ,
        textStyle = MaterialTheme.typography.labelLarge,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.padding(contentPadding),
                contentAlignment = Alignment.CenterStart
            ) {
                if (label != null && value.isEmpty()) {
                    Text(text = label, color = Gray)
                }
                innerTextField()
            }
        }
    )

}

@Preview
@Composable
private fun MCTextFieldPreview() {
    MoneyConvertorTheme {
        MCTextField(value = "", onValueChange = {}, label = "label")
    }
}






