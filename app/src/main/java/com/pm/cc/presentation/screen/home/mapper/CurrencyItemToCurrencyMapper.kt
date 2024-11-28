package com.pm.cc.presentation.screen.home.mapper

import com.pm.cc.core.Mapper
import com.pm.cc.domain.Currency
import com.pm.cc.presentation.screen.home.model.CurrencyItem

class CurrencyItemToCurrencyMapper : Mapper<CurrencyItem, Currency> {
    override fun map(src: CurrencyItem): Currency = Currency(
        id = src.id,
        code = src.code
    )
}