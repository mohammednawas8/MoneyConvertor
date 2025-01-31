package com.mc.ui.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.navigation.BottomSheetNavigator
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

fun Modifier.sheetMaxHeight(factor: Float = 1f): Modifier = composed {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    heightIn(max = (screenHeight.value * factor).dp)
}

fun Modifier.paddingWithoutTop(paddingValues: PaddingValues): Modifier = padding(
    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
    bottom = paddingValues.calculateBottomPadding(),
)

fun Modifier.paddingWithoutBottom(paddingValues: PaddingValues): Modifier = padding(
    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
    top = paddingValues.calculateTopPadding(),
)

/**
 * Create and remember a [BottomSheetNavigator]
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberBottomSheetNavigator(): BottomSheetNavigator {
    // Current Transition states("Hidden -> HalfExpanded -> Expanded -> HalfExpanded -> Hidden")
    // TODO("Transition states: Hidden -> Expanded -> HalfExpanded -> Hidden")

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )
    return remember(sheetState) {
        BottomSheetNavigator(sheetState = sheetState)
    }
}

object SheetUtils {

    const val CornerDpSize = 12
    fun getRoundedSheetShape(): RoundedCornerShape {
        return RoundedCornerShape(
            topStart = CornerDpSize.dp,
            topEnd = CornerDpSize.dp
        )
    }
}