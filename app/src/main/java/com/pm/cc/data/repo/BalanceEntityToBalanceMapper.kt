package com.pm.cc.data.repo

import com.pm.cc.core.Mapper
import com.pm.cc.data.db.BalanceEntity
import com.pm.cc.domain.Balance

class BalanceEntityToBalanceMapper : Mapper<BalanceEntity, Balance> {

    override fun map(src: BalanceEntity): Balance = Balance(
        currencyId = src.currencyId,
        currencyCode = src.currencyCode,
        amount = src.amount,
    )

}