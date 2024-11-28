package com.pm.cc.data.repo

import com.pm.cc.core.Mapper
import com.pm.cc.data.db.CurrencyEntity
import com.pm.cc.domain.Currency

class CurrencyEntityToCurrencyMapper : Mapper<CurrencyEntity, Currency> {
    override fun map(src: CurrencyEntity): Currency = Currency(
        id = requireNotNull(src.id),
        code = src.code
    )
}