package com.mc.designsystem.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mc.designsystem.theme.MoneyConvertorTheme
import com.mc.designsystem.theme.WhiteBackground

@Composable
fun MCBackgroundScreen(
    modifier: Modifier = Modifier,
    hideKeyboardOnTap: Boolean = true,
    content: @Composable () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(hideKeyboardOnTap) {
                if (hideKeyboardOnTap) {
                    detectTapGestures {
                        focusManager.clearFocus()
                    }
                }
            },
        color = MaterialTheme.colorScheme.background
    ) {
        val maxWidth = with(LocalDensity.current) {
            LocalConfiguration.current.screenWidthDp.dp.toPx()
        }
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = -maxWidth
                    scaleX = 2f
                }
        ) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x7CC9C9FF),
                        Color(0xFFDDF6F3),
                        WhiteBackground,
                    )
                )
            )
        }
        content()
    }
}

@Preview
@Composable
private fun MCBackgroundScreenPreview() {
    MoneyConvertorTheme {
        MCBackgroundScreen {

        }
    }
}













