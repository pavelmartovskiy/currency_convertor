package com.pm.cc.presentation.screen.home.mapper

import com.pm.cc.core.Mapper
import com.pm.cc.domain.Currency
import com.pm.cc.presentation.screen.home.model.CurrencyItem

class CurrencyToCurrencyItemMapper : Mapper<Currency, CurrencyItem> {
    override fun map(src: Currency): CurrencyItem = CurrencyItem(
        id = src.id,
        code = src.code
    )
}