package com.pm.cc.presentation.screen.home

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pm.cc.R
import com.pm.cc.presentation.screen.ArrowIcon
import com.pm.cc.presentation.screen.BodyMedium
import com.pm.cc.presentation.screen.HeaderMediumGrey
import com.pm.cc.presentation.screen.HeaderMediumWhite
import com.pm.cc.presentation.screen.Screens
import com.pm.cc.presentation.screen.home.model.CurrencyItem
import com.pm.cc.presentation.screen.home.model.HomeState
import com.pm.cc.presentation.screen.home.model.HomeUiAction
import com.pm.cc.presentation.screen.home.model.HomeUserInput
import com.pm.cc.presentation.screen.home.model.HomeViewModel
import com.pm.cc.presentation.screen.home.uicomponent.CcAlertDialog
import com.pm.cc.presentation.screen.home.uicomponent.CurrencyDropDownList
import com.pm.cc.presentation.screen.home.uicomponent.HomeHeader
import com.pm.cc.presentation.screen.home.uicomponent.MyBalances
import com.pm.cc.presentation.theme.Blue
import com.pm.cc.presentation.theme.Gray
import com.pm.cc.presentation.theme.Green
import com.pm.cc.presentation.theme.Red
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen() {

    val viewModel = koinViewModel<HomeViewModel>()

    val uiData = viewModel.uiData
    val userInput = viewModel.userInput
    val dialogState = viewModel.dialogState

    if (dialogState != null) {
        CcAlertDialog(
            onDismissRequest = { viewModel.onUiAction(action = HomeUiAction.OnDialogDismiss) },
            title = dialogState.title,
            text = dialogState.message
        )
    }

    when (uiData) {
        is HomeState.Success -> {
            Success(
                state = uiData,
                userInput = userInput,
                onSellChange = { value ->
                    viewModel.onUiAction(
                        action = HomeUiAction.OnSellChange(
                            value = value,
                            ratesMap = uiData.rates
                        )
                    )
                },
                onSellCurrencyChange = { value ->
                    viewModel.onUiAction(
                        action = HomeUiAction.OnSellCurrencyChange(
                            value = value,
                            ratesMap = uiData.rates
                        )
                    )
                },
                onReceiveChange = { value ->
                    viewModel.onUiAction(
                        action = HomeUiAction.OnReceiveChange(
                            value = value,
                            ratesMap = uiData.rates
                        )
                    )
                },
                onReceiveCurrencyChange = { value ->
                    Log.v("HomeScreen", "onReceiveCurrencyChange: $value")
                    viewModel.onUiAction(
                        action = HomeUiAction.OnReceiveCurrencyChange(
                            value = value,
                            ratesMap = uiData.rates
                        )
                    )
                },
                onSubmit = {
                    viewModel.onUiAction(
                        action = HomeUiAction.OnSubmit(
                            sellValue = userInput.sell,
                            sellCurrency = userInput.sellCurrencyItem,
                            receiveValue = userInput.receive,
                            receiveCurrency = userInput.receiveCurrencyItem,
                            rate = userInput.rate
                        )
                    )
                }
            )
        }

        is HomeState.Loading -> Loading()
        is HomeState.Error -> Error(state = uiData)
    }
}

fun NavGraphBuilder.composableHomeScreen() {
    composable(route = Screens.HOME.route) {
        HomeScreen()
    }
}


@Composable
fun CurrencyExchange(
    currencies: List<CurrencyItem>,
    userInput: HomeUserInput,
    onSellChange: (String) -> Unit,
    onSellCurrencyChange: (CurrencyItem) -> Unit,
    onReceiveChange: (String) -> Unit,
    onReceiveCurrencyChange: (CurrencyItem) -> Unit,
    onSubmit: () -> Unit
) {

    Log.w("HomeScreen", "CurrencyExchange: $userInput")

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        HeaderMediumGrey(
            text = stringResource(id = R.string.currency_exchange).uppercase(),
            modifier = Modifier.padding(all = 8.dp)
        )
        ExchangeLine(
            iconId = R.drawable.baseline_arrow_upward_24,
            iconBgColor = Red,
            label = stringResource(id = R.string.sell),
            currencies = currencies,
            amount = userInput.sell,
            currencyItem = userInput.sellCurrencyItem,
            onAmountChange = onSellChange,
            onCurrencyChange = onSellCurrencyChange
        )

        HorizontalDivider(
            color = Gray,
            modifier = Modifier.padding(start = 48.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
        )
        ExchangeLine(
            iconId = R.drawable.baseline_arrow_downward_24,
            iconBgColor = Green,
            label = stringResource(id = R.string.receive),
            currencies = currencies,
            amount = userInput.receive,
            currencyItem = userInput.receiveCurrencyItem,
            onAmountChange = onReceiveChange,
            onCurrencyChange = onReceiveCurrencyChange
        )
        Button(
            onClick = onSubmit,
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue,
                contentColor = Color.White
            ),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth(fraction = 0.8f)
        ) {
            HeaderMediumWhite(
                text = stringResource(R.string.submit).uppercase(),
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun ExchangeLine(
    @DrawableRes iconId: Int,
    iconBgColor: Color,
    currencies: List<CurrencyItem>,
    label: String,
    amount: String,
    currencyItem: CurrencyItem,
    onAmountChange: (String) -> Unit,
    onCurrencyChange: (CurrencyItem) -> Unit
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(all = 8.dp)
        .height(height = 64.dp)
) {

    ArrowIcon(
        iconId = iconId,
        bgColor = iconBgColor,
        modifier = Modifier
            .align(alignment = Alignment.CenterVertically)
    )
    BodyMedium(
        text = label,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(fraction = 0.25f)
            .padding(horizontal = 8.dp)
            .align(alignment = Alignment.CenterVertically)
    )
    TextField(
        value = amount,
        onValueChange = onAmountChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(fraction = 0.55f),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent
        )
    )
    CurrencyDropDownList(
        selectedValue = currencyItem,
        options = currencies,
        onValueChangedEvent = onCurrencyChange,
        modifier = Modifier
            .wrapContentHeight()
    )
}

@Composable
fun Loading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(64.dp)
                .align(Alignment.Center),
            color = Blue,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
fun Error(state: HomeState.Error) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = state.message,
            modifier = Modifier
                .align(Alignment.Center),
        )
    }
}

@Composable
fun Success(
    state: HomeState.Success,
    userInput: HomeUserInput,
    onSellChange: (String) -> Unit,
    onSellCurrencyChange: (CurrencyItem) -> Unit,
    onReceiveChange: (String) -> Unit,
    onReceiveCurrencyChange: (CurrencyItem) -> Unit,
    onSubmit: () -> Unit
) {
    Column {
        HomeHeader()
        MyBalances(items = state.balance)
        CurrencyExchange(
            currencies = state.currencies,
            userInput = userInput,
            onSellChange = onSellChange,
            onSellCurrencyChange = onSellCurrencyChange,
            onReceiveChange = onReceiveChange,
            onReceiveCurrencyChange = onReceiveCurrencyChange,
            onSubmit = onSubmit
        )
    }
}