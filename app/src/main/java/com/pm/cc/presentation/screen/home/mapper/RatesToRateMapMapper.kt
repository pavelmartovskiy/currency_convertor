package com.pm.cc.presentation.screen.home.mapper

import com.pm.cc.core.CurrencyCode
import com.pm.cc.core.Mapper
import com.pm.cc.domain.Currency
import com.pm.cc.domain.Rate

class RatesToRateMapMapper : Mapper<List<Rate>, Map<CurrencyCode, Map<CurrencyCode, Double>>> {
    override fun map(src: List<Rate>): Map<CurrencyCode, Map<CurrencyCode, Double>> = src
        .map { rate -> rate.src.code to (rate.dst.code to rate.rate) }
        .convertToRateMap()
        .let { result ->

            val baseCurrencyCode: CurrencyCode = Currency.BASE_CURRENCY.code
            val codes = result.keys.toList()

            result + codes
                .filter { sellCode -> sellCode != baseCurrencyCode }
                .flatMap { sellCode ->
                    codes.map { receiveCode ->

                        val srcToBaseRate =
                            requireNotNull(requireNotNull(result[sellCode])[baseCurrencyCode])
                        val baseToDstRate =
                            requireNotNull(requireNotNull(result[baseCurrencyCode])[receiveCode])

                        baseToDstRate * srcToBaseRate

                        sellCode to (receiveCode to baseToDstRate * srcToBaseRate)
                    }
                }.convertToRateMap()
        }

    private fun List<Pair<CurrencyCode, Pair<CurrencyCode, Double>>>.convertToRateMap(): Map<CurrencyCode, Map<CurrencyCode, Double>> =
        groupBy(keySelector = { item -> item.first }, valueTransform = { item -> item.second })
            .mapValues { value -> value.value.toMap() }
}

