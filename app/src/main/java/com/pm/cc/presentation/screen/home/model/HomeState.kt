package com.pm.cc.presentation.screen.home.model

import com.pm.cc.core.CurrencyCode

sealed class HomeState {
    data class Success(
        val balance: List<BalanceItem>,
        val rates: Map<CurrencyCode, Map<CurrencyCode, Double>>,
        val currencies: List<CurrencyItem>,
    ) : HomeState()

    data object Loading : HomeState()
    data class Error(val message: String) : HomeState()
}