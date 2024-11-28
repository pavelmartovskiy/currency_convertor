package com.pm.cc.data.repo

import com.pm.cc.core.Mapper
import com.pm.cc.data.db.ExtRateEntity
import com.pm.cc.domain.Currency
import com.pm.cc.domain.Rate
import java.util.Date

class ExtRateEntityToRateMapper : Mapper<ExtRateEntity, Rate> {

    override fun map(src: ExtRateEntity): Rate = Rate(
        id = src.id,
        date = Date(src.date),
        src = Currency(id = src.srcId, code = src.srcCode),
        dst = Currency(id = src.dstId, code = src.dstCode),
        rate = src.rate
    )

}