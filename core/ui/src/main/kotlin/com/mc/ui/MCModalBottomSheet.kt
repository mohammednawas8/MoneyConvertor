package com.mc.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.navigation.BottomSheetNavigator
import androidx.compose.material.navigation.bottomSheet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mc.ui.util.SheetUtils
import com.mc.ui.util.isKeyboardVisibleAsState
import com.mc.ui.util.rememberBottomSheetNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

data class SheetNavParams @OptIn(ExperimentalMaterial3Api::class) constructor(
    val backStackEntry: NavBackStackEntry,
    val sheetState: SheetState,
    val subNavController: NavHostController,
    val subBottomSheetNavigator: BottomSheetNavigator,
    val scope: CoroutineScope
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MCModalBottomSheet(
    modifier: Modifier = Modifier,
    state: SheetState,
    shouldFillMaxHeight: Boolean = true,
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties(),
    onDismissRequest: () -> Unit,
    heightFraction: Float = 0.89f,
    dragHandle: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val scope = rememberCoroutineScope()
    val isImeVisible by isKeyboardVisibleAsState()
    val maxHeight = LocalConfiguration.current.screenHeightDp.dp

    ProvideStatusBarHeight { statusBarHeight ->
        val maxFraction by animateFloatAsState(targetValue = if (isImeVisible) 0.975f - (statusBarHeight / maxHeight) else heightFraction)

        ModalBottomSheet(
            modifier = if (shouldFillMaxHeight) modifier.fillMaxHeight(maxFraction) else modifier.wrapContentHeight(),
            onDismissRequest = {
                onDismissRequest()
                scope.launch {
                    state.hide()
                }
            },
            shape = SheetUtils.getRoundedSheetShape(),
            sheetState = state,
            properties = properties,
            dragHandle = dragHandle,
            containerColor = Color.White,
            windowInsets = WindowInsets(0, 0, 0, 0),
        ) {
            Column(
                // navigationBarsPadding() did not work on Android 10 and older versions
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                    Modifier.padding(bottom = 50.dp) // Workaround
                } else {
                    Modifier.navigationBarsPadding()
                }
            ) {
                content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
inline fun <reified T : Any> NavGraphBuilder.modalBottomSheet(
    deepLinks: List<NavDeepLink> = emptyList(),
    shouldFillMaxHeight: Boolean = true,
    noinline close: () -> Unit,
    noinline content: @Composable ColumnScope.(backstackEntry: NavBackStackEntry, state: SheetState) -> Unit
) {
    bottomSheet<T>(deepLinks = deepLinks) { backStackEntry ->
        val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        MCModalBottomSheet(
            state = state,
            shouldFillMaxHeight = shouldFillMaxHeight,
            onDismissRequest = close,
        ) {
            content(this, backStackEntry, state)
        }
    }
}

/**
 * This version is used if you have sub routes (sub graph) inside the sheet
 * It provides [SheetNavParams] which contains the subNavController, subBottomSheetNavigator
 * And you need to pass them to the BottomSheetLayout and NavHost to ensure that the navigation inside the sheet works.
 */
@OptIn(ExperimentalMaterial3Api::class,)
fun NavGraphBuilder.subRoutesModalBottomSheet(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    heightFraction: Float = 0.89f,
    popBackStack: () -> Unit,
    content: @Composable ColumnScope.(SheetNavParams) -> Unit
) {
    bottomSheet(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks
    ) { backStackEntry ->
        val subBottomSheetNavigator = rememberBottomSheetNavigator()
        val navController = rememberNavController(subBottomSheetNavigator)
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val scope = rememberCoroutineScope()
        val focusRequester = remember { FocusRequester() }
        val isKeyboardVisible by isKeyboardVisibleAsState()
        var isContentFocused by remember { mutableStateOf(false) }

        MCModalBottomSheet(
            state = sheetState,
            onDismissRequest = popBackStack,
            heightFraction = heightFraction,
            properties = ModalBottomSheetDefaults.properties(
                shouldDismissOnBackPress = false
            )
        ) {
            // This box acts like a back handler for the bottomSheet
            // because the BackHandler composable doesn't work with ModalBottomSheet
            Box(
                modifier = Modifier
                    .onFocusChanged {
                        scope.launch {
                            delay(1.seconds)
                            if (isContentFocused) {
                                return@launch
                            }
                            if (it.hasFocus.not()) {
                                focusRequester.requestFocus()
                            }
                        }
                    }
                    .onPreviewKeyEvent {
                        if (it.key == Key.Back && it.type == KeyEventType.KeyUp) {
                            // Back-Click
                            if (!navController.popBackStack()) {
                                scope.launch {
                                    sheetState.hide()
                                    popBackStack()
                                }
                            }
                            return@onPreviewKeyEvent true
                        }
                        return@onPreviewKeyEvent false
                    }
                    .focusRequester(focusRequester)
                    .focusable()
            )
            Column(
                modifier = Modifier.onFocusChanged {
                    isContentFocused = it.hasFocus
                }
            ) {
                content(
                    SheetNavParams(
                        backStackEntry = backStackEntry,
                        sheetState = sheetState,
                        subNavController = navController,
                        subBottomSheetNavigator = subBottomSheetNavigator,
                        scope = scope
                    )
                )
            }
        }

        LaunchedEffect(key1 = sheetState) {
            focusRequester.requestFocus()
        }

        LaunchedEffect(isKeyboardVisible) {
            if (isKeyboardVisible.not()) {
                focusRequester.requestFocus()
            }
        }
    }
}

@SuppressLint("InternalInsetResource")
@Composable
private fun ProvideStatusBarHeight(content: @Composable (paddingTop: Dp) -> Unit) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val paddingTop = remember {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            val statusBarHeightPixels = context.resources.getDimensionPixelSize(resourceId)
            with(density) { statusBarHeightPixels.toDp() }
        } else {
            0.dp
        }
    }

    content(paddingTop)
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavController.closeSubRoutesSheet(sheetNavArgs: SheetNavParams) {
    sheetNavArgs.scope.launch {
        sheetNavArgs.sheetState.hide()
        popBackStack()
    }
}