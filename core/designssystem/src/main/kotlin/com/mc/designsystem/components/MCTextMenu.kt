package com.mc.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mc.designsystem.theme.Gray
import com.mc.designsystem.theme.MoneyConvertorTheme
import com.mc.designsystem.theme.WhiteBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MCTextMenu(
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        Row(
            modifier = Modifier.menuAnchor(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = selectedOption, style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Gray,
                modifier = Modifier.graphicsLayer {
                    rotationZ = if (expanded) 180f else 0f
                }
            )
        }

        ExposedDropdownMenu(
            modifier = Modifier
                .width(150.dp)
                .height(300.dp)
                .background(WhiteBackground),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .width(150.dp)
                    .height(300.dp),
            ) {
                itemsIndexed(options) { index, option ->
                    DropdownMenuItem(
                        text = {
                            Text(text = option)
                        },
                        onClick = {
                            onOptionSelected(index)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MCTextMenuPreview() {
    MoneyConvertorTheme {
        MCTextMenu(
            selectedOption = "USD",
            options = listOf("USD", "EUR", "GBP", "JPY", "AUD"),
            onOptionSelected = {}
        )
    }
}











