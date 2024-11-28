package com.pm.cc.presentation.screen.home.mapper

import com.pm.cc.core.Mapper
import com.pm.cc.domain.Balance
import com.pm.cc.presentation.screen.home.model.BalanceItem
import kotlin.math.round

class BalanceToBalanceItemMapper : Mapper<Balance, BalanceItem> {
    override fun map(src: Balance): BalanceItem = BalanceItem(
        code = src.currencyCode, amount = round(x = src.amount).toString()
    )
}