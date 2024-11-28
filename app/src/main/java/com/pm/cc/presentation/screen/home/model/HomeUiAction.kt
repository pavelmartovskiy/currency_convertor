package com.pm.cc.presentation.screen.home.model

import com.pm.cc.core.CurrencyCode

sealed class HomeUiAction {

    data class OnSellChange(
        val value: String,
        val ratesMap: Map<CurrencyCode, Map<CurrencyCode, Double>>,
    ) : HomeUiAction()

    data class OnReceiveChange(
        val value: String,
        val ratesMap: Map<CurrencyCode, Map<CurrencyCode, Double>>,
    ) : HomeUiAction()

    data class OnSellCurrencyChange(
        val value: CurrencyItem,
        val ratesMap: Map<CurrencyCode, Map<CurrencyCode, Double>>,
    ) : HomeUiAction()

    data class OnReceiveCurrencyChange(
        val value: CurrencyItem,
        val ratesMap: Map<CurrencyCode, Map<CurrencyCode, Double>>,
    ) : HomeUiAction()
    data class OnSubmit(
        val sellValue: String,
        val receiveValue: String,
        val sellCurrency: CurrencyItem,
        val receiveCurrency: CurrencyItem,
        val rate: Double
    ) : HomeUiAction()

    data object OnDialogDismiss : HomeUiAction()

}