package com.pm.cc.data

import com.pm.cc.core.CurrencyCode
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyExchangeRates(
    val base: CurrencyCode,
    val date: String,
    val rates: Map<CurrencyCode, Double>
)