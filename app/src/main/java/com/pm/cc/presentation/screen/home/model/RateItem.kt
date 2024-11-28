package com.pm.cc.presentation.screen.home.model

import com.pm.cc.core.CurrencyCode

data class RateItem (
    val id: Long,
    val code: CurrencyCode,
    val rate: String
)

