package com.pm.cc.domain

data class Balance(
    val currencyId: Long,
    val currencyCode: String,
    val amount: Double
)