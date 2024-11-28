package com.pm.cc.presentation.screen.home.model

data class HomeUserInput(
    val sell: String = "",
    val sellCurrencyItem: CurrencyItem = CURRENCY_ITEM_STUB,
    val receive: String = "",
    val receiveCurrencyItem: CurrencyItem = CURRENCY_ITEM_STUB,
    val rate: Double = 1.0
) {
    companion object {
        val CURRENCY_ITEM_STUB = CurrencyItem(id = -1, code = "")
    }
}