package com.mc.currencyconvertor.currency_selector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mc.currencyconvertor.R
import com.mc.designsystem.components.MCTextField
import com.mc.designsystem.theme.Gray
import com.mc.designsystem.theme.MoneyConvertorTheme
import com.mc.model.currency_convertor.CurrencyType
import com.mc.ui.util.EventsCollector

@Composable
fun CurrencySelectorRoute(
    navigateBack: () -> Unit,
    onFromCurrencySelected: (String) -> Unit,
    onToCurrencySelected: (String) -> Unit,
    viewModel: CurrencySelectorViewModel = hiltViewModel()
) {
    val uiModel by viewModel.uiModel.collectAsStateWithLifecycle()

    EventsCollector(viewModel.events) {
        when(it) {
            is CurrencySelectorEvents.SelectCurrency -> {
                when(it.type) {
                    CurrencyType.FROM -> onFromCurrencySelected(it.currency)
                    CurrencyType.TO -> onToCurrencySelected(it.currency)
                }
                navigateBack()
            }
        }
    }

    CurrencySelectorScreen(
        uiModel = uiModel,
        navigateBack = navigateBack,
        onCurrencySelected = viewModel::selectCurrency,
        onSearchQueryChange = viewModel::onSearchQueryChange
    )
}

@Composable
internal fun CurrencySelectorScreen(
    uiModel: CurrencySelectorUiModel,
    navigateBack: () -> Unit,
    onCurrencySelected: (String) -> Unit,
    onSearchQueryChange:(String) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(true) {
        listState.scrollToItem(uiModel.selectedCurrencyIndex)
    }

    Scaffold(
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    IconButton(
                        onClick = navigateBack
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }

                    Text(
                        text = stringResource(id = R.string.select_currency),
                        modifier = Modifier.align(alignment = Alignment.Center),
                        fontSize = 22.sp,
                    )
                }

                HorizontalDivider()

                MCTextField(
                    value = uiModel.searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.padding(all = 8.dp),
                    contentPadding = PaddingValues(15.dp),
                    label = stringResource(id = com.mc.designssystem.R.string.search),
                    containerColor = Gray.copy(alpha = 0.25f)
                )

                HorizontalDivider()
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            state = listState
        ) {
            itemsIndexed(
                items = uiModel.currencies,
                key = { _, currency -> currency }
            ) { i, currency ->
                CurrencyCard(
                    text = currency,
                    isSelected = i == uiModel.selectedCurrencyIndex,
                    onClick = { onCurrencySelected(currency) }
                )
            }
        }
    }
}

@Composable
private fun CurrencyCard(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier
                .weight(1f)
                .padding(all = 16.dp)
        )

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Done,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 16.dp),
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
private fun CurrencySelectorScreenPreview() {
    MoneyConvertorTheme {
        CurrencySelectorScreen(
            uiModel = CurrencySelectorUiModel.preview,
            navigateBack = {},
            onCurrencySelected = {},
            onSearchQueryChange = {}
        )
    }
}