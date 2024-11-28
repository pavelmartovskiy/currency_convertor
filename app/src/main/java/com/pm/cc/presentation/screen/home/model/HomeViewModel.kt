package com.pm.cc.presentation.screen.home.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pm.cc.core.onFailure
import com.pm.cc.core.onSuccess
import com.pm.cc.core.runCatching
import com.pm.cc.di.CcComponent
import com.pm.cc.di.Qualifiers
import com.pm.cc.di.inject
import com.pm.cc.domain.Balance
import com.pm.cc.domain.ConvertUseCaseFailure
import com.pm.cc.domain.ConvertUseCaseParams
import com.pm.cc.domain.ConvertUseCaseResult
import com.pm.cc.domain.Currency
import com.pm.cc.domain.Rate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.ext.getFullName
import java.util.Locale

class HomeViewModel : ViewModel(), CcComponent {

    private val getRateFlow by inject(qualifier = Qualifiers.UseCases.GET_RATE_FLOW)
    private val getBalanceFlow by inject(qualifier = Qualifiers.UseCases.GET_BALANCE_FLOW)
    private val getCurrencyFlow by inject(qualifier = Qualifiers.UseCases.GET_CURRENCY_FLOW)
    private val convert by inject(qualifier = Qualifiers.UseCases.CONVERT)

    private val balanceToBalanceItemMapper by inject(qualifier = Qualifiers.Mappers.BALANCE_TO_BALANCE_ITEM)
    private val currencyToCurrencyItemMapper by inject(qualifier = Qualifiers.Mappers.CURRENCY_TO_CURRENCY_ITEM)
    private val currencyItemToCurrencyMapper by inject(qualifier = Qualifiers.Mappers.CURRENCY_ITEM_TO_CURRENCY)
    private val ratesToRateMapper by inject(qualifier = Qualifiers.Mappers.RATES_TO_RATE_MAP_ITEM)

    var uiData: HomeState by mutableStateOf(value = HomeState.Loading)
        private set
    var userInput: HomeUserInput by mutableStateOf(value = HomeUserInput())
        private set
    var dialogState: HomeDialogState? by mutableStateOf(value = null)
        private set

    init {

        viewModelScope.launch {
            runCatching {
                val rateFlow = getRateFlow.execute(params = Unit).getOrThrow()
                val balanceFlow = getBalanceFlow.execute(params = Unit).getOrThrow()
                val currencyFlow = getCurrencyFlow.execute(params = Unit).getOrThrow()
                combineFlow(
                    balanceFlow = balanceFlow,
                    currencyFlow = currencyFlow,
                    ratesFlow = rateFlow
                )
            }
                .onSuccess { flow ->
                    flow.collect { state ->
                        if (state.currencies.isNotEmpty()) {
                            if (userInput.sellCurrencyItem == HomeUserInput.CURRENCY_ITEM_STUB) {
                                userInput = userInput.copy(sellCurrencyItem = state.currencies[0])
                            }
                            if (userInput.receiveCurrencyItem == HomeUserInput.CURRENCY_ITEM_STUB) {
                                userInput =
                                    userInput.copy(receiveCurrencyItem = state.currencies[0])
                            }
                        }
                        uiData = state
                    }
                }
                .onFailure {
                    uiData =
                        HomeState.Error(message = "Something went wrong (${it::class.simpleName})}), ")
                }
        }
    }

    private fun combineFlow(
        balanceFlow: Flow<List<Balance>>,
        currencyFlow: Flow<List<Currency>>,
        ratesFlow: Flow<List<Rate>>
    ): Flow<HomeState.Success> = combine(
        flow = balanceFlow,
        flow2 = currencyFlow,
        flow3 = ratesFlow
    ) { balances, currencies, rates ->
        HomeState.Success(
            balance = balances
                .map { item -> balanceToBalanceItemMapper.map(src = item) },
            currencies = currencies.map { item -> currencyToCurrencyItemMapper.map(src = item) },
            rates = ratesToRateMapper.map(src = rates)
        )
    }


    fun onUiAction(action: HomeUiAction) {
        when (action) {
            is HomeUiAction.OnSellChange -> handleUiAction(action = action)
            is HomeUiAction.OnReceiveChange -> handleUiAction(action = action)
            is HomeUiAction.OnSellCurrencyChange -> handleUiAction(action = action)
            is HomeUiAction.OnReceiveCurrencyChange -> handleUiAction(action = action)
            is HomeUiAction.OnSubmit -> handleUiAction(action = action)
            is HomeUiAction.OnDialogDismiss -> dialogState = null
        }
    }

    private fun getSelectedCellCurrencyItemOrThrow(): CurrencyItem =
        userInput.sellCurrencyItem
            .takeIf { item -> item != HomeUserInput.CURRENCY_ITEM_STUB }
            ?: throw IllegalStateException("Sell currency item is not selected")

    private fun getSelectedReceiveCurrencyItemOrThrow(): CurrencyItem =
        userInput.receiveCurrencyItem
            .takeIf { item -> item != HomeUserInput.CURRENCY_ITEM_STUB }
            ?: throw IllegalStateException("Receive currency item is not selected")


    private fun handleUiAction(action: HomeUiAction.OnSellChange) = runCatching {
        val sellValue = action.value.asDoubleValue()
        val sellCurrencyItem = getSelectedCellCurrencyItemOrThrow()
        val receiveCurrencyItem = getSelectedReceiveCurrencyItemOrThrow()
        val rateMapForSelectedCurrencyItem = requireNotNull(action.ratesMap[sellCurrencyItem.code])
        val rate = requireNotNull(rateMapForSelectedCurrencyItem[receiveCurrencyItem.code])
        val receiveValue = sellValue * rate

        val sell = sellValue.format()
        val receive = receiveValue.format()

        userInput.copy(sell = sell, receive = receive)
    }
        .onSuccess { newInput -> userInput = newInput }

    private fun handleUiAction(action: HomeUiAction.OnReceiveChange) = runCatching {
        val receiveValue = action.value.asDoubleValue()

        val sellCurrencyItem = getSelectedCellCurrencyItemOrThrow()
        val receiveCurrencyItem = getSelectedReceiveCurrencyItemOrThrow()
        val rateMapForSelectedCurrencyItem =
            requireNotNull(action.ratesMap[receiveCurrencyItem.code])
        val rate = requireNotNull(rateMapForSelectedCurrencyItem[sellCurrencyItem.code])

        val sellValue = receiveValue * rate

        val sell = sellValue.format()
        val receive = receiveValue.format()

        userInput.copy(sell = sell, receive = receive)
    }
        .onSuccess { newInput -> userInput = newInput }

    private fun handleUiAction(action: HomeUiAction.OnSellCurrencyChange) = runCatching {

        val sellValue = userInput.sell.asDoubleValue()
        val sellCurrencyItem = action.value
        val receiveCurrencyItem = getSelectedReceiveCurrencyItemOrThrow()
        val rateMapForSelectedCurrencyItem = requireNotNull(action.ratesMap[sellCurrencyItem.code])
        val rate = requireNotNull(rateMapForSelectedCurrencyItem[receiveCurrencyItem.code])
        val receiveValue = sellValue * rate

        val sell = sellValue.format()
        val receive = receiveValue.format()

        userInput.copy(
            sell = sell,
            receive = receive,
            sellCurrencyItem = sellCurrencyItem,
            rate = rate
        )
    }
        .onSuccess { newInput -> userInput = newInput }

    private fun handleUiAction(action: HomeUiAction.OnReceiveCurrencyChange) = runCatching {
        val receiveValue = userInput.receive.asDoubleValue()

        val sellCurrencyItem = getSelectedCellCurrencyItemOrThrow()
        val receiveCurrencyItem = action.value
        val rateMapForSelectedCurrencyItem =
            requireNotNull(action.ratesMap[receiveCurrencyItem.code])
        val rate = requireNotNull(rateMapForSelectedCurrencyItem[sellCurrencyItem.code])

        val sellValue = receiveValue * rate

        val sell = sellValue.format()
        val receive = receiveValue.format()

        userInput.copy(
            sell = sell,
            receive = receive,
            receiveCurrencyItem = receiveCurrencyItem,
            rate = 1 / rate
        )
    }
        .onSuccess { newInput -> userInput = newInput }

    private fun handleUiAction(action: HomeUiAction.OnSubmit) {
        viewModelScope.launch {

            uiData = HomeState.Loading

            runCatching {
                convert.execute(
                    params = ConvertUseCaseParams(
                        sellCurrency = currencyItemToCurrencyMapper.map(src = action.sellCurrency),
                        sellAmount = action.sellValue.asDoubleValue(),
                        receiveCurrency = currencyItemToCurrencyMapper.map(src = action.receiveCurrency),
                        receiveAmount = action.receiveValue.asDoubleValue(),
                        rate = action.rate
                    )
                ).getOrThrow()
            }
                .onSuccess { result: ConvertUseCaseResult ->
                    userInput = userInput.copy(
                        sell = "",
                        receive = "",
                        sellCurrencyItem = action.sellCurrency,
                        receiveCurrencyItem = action.receiveCurrency
                    )

                    dialogState = HomeDialogState(
                        title = "Currency converted",
                        message = "You have successfully converted " +
                                "${result.sellAmount} ${result.sellCurrency.code} " +
                                "to ${result.receiveAmount} ${action.receiveCurrency.code}. " +
                                "Commission is ${result.commission} ${result.sellAmount}"
                    )
                }
                .onFailure { failure ->
                    when (failure) {
                        is ConvertUseCaseFailure.InsufficientBalance -> {
                            dialogState = HomeDialogState(
                                title = "Error",
                                message = "Insufficient balance. " +
                                        "Need ${failure.need} ${failure.currency.code}. " +
                                        "Balance: ${failure.realBalance} ${failure.currency.code}"
                            )
                        }

                        is ConvertUseCaseFailure.SameCurrency -> {
                            dialogState = HomeDialogState(
                                title = "Error",
                                message = "Same currency. Sell and Receive currency can't be the same"
                            )
                        }

                        else -> dialogState = HomeDialogState(
                            title = "Error",
                            message = "Unhandled error: ${failure::class.getFullName()}"
                        )
                    }
                }
        }
    }

    private fun String.asDoubleValue() = if (isEmpty()) 0.0 else toDouble()

    private fun Double.format(): String =
        if (this == 0.0) "" else String.format(Locale.US, "%.4f", this)

}