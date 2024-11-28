package com.pm.cc.domain

import com.pm.cc.core.CurrencyCode

data class Currency(
    val id: Long,
    val code: CurrencyCode,
) {
    companion object {
        val BASE_CURRENCY = Currency(1, "EUR")
    }
}