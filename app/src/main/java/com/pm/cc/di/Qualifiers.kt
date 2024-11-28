package com.pm.cc.di

import com.pm.cc.core.CurrencyCode
import com.pm.cc.core.Mapper
import com.pm.cc.data.db.BalanceEntity
import com.pm.cc.data.db.CurrencyEntity
import com.pm.cc.data.db.ExtRateEntity
import com.pm.cc.domain.Balance
import com.pm.cc.domain.CcRepository.GetBalanceFlowFailure
import com.pm.cc.domain.CcRepository.GetCurrencyFlowFailure
import com.pm.cc.domain.CcRepository.GetRateFlowFailure
import com.pm.cc.domain.CcUseCase
import com.pm.cc.domain.ConvertUseCaseFailure
import com.pm.cc.domain.ConvertUseCaseParams
import com.pm.cc.domain.ConvertUseCaseResult
import com.pm.cc.domain.Currency
import com.pm.cc.domain.Rate
import com.pm.cc.presentation.screen.home.model.BalanceItem
import com.pm.cc.presentation.screen.home.model.CurrencyItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

object Qualifiers {
    object Coroutines {
        object Contexts {
            val MAIN = q<CoroutineContext>(name = "MAIN")
            val IO = q<CoroutineContext>(name = "IO")
            val COMPUTATION = q<CoroutineContext>(name = "COMPUTATION")
            val DB = q<CoroutineContext>(name = "DB")
        }

        object Scopes {
            val APP = q<CoroutineScope>(name = "APP")
        }
    }

    object Mappers {

        val EXT_RATE_ENTITY_TO_RATE =
            q<Mapper<ExtRateEntity, Rate>>(name = "EXT_RATE_ENTITY_TO_RATE")
        val BALANCE_ENTITY_TO_BALANCE =
            q<Mapper<BalanceEntity, Balance>>(name = "BALANCE_ENTITY_TO_BALANCE")
        val BALANCE_TO_BALANCE_ITEM =
            q<Mapper<Balance, BalanceItem>>(name = "BALANCE_TO_BALANCE_ITEM")
        val RATES_TO_RATE_MAP_ITEM =
            q<Mapper<List<Rate>, Map<CurrencyCode, Map<CurrencyCode, Double>>>>(name = "RATES_TO_RATE_MAP_ITEM")
        val CURRENCY_ENTITY_TO_CURRENCY =
            q<Mapper<CurrencyEntity, Currency>>(name = "CURRENCY_ENTITY_TO_CURRENCY")
        val CURRENCY_TO_CURRENCY_ITEM =
            q<Mapper<Currency, CurrencyItem>>(name = "CURRENCY_TO_CURRENCY_ITEM")
        val CURRENCY_ITEM_TO_CURRENCY =
            q<Mapper<CurrencyItem, Currency>>(name = "CURRENCY_ITEM_TO_CURRENCY")
    }

    object UseCases {
        val GET_RATE_FLOW =
            q<CcUseCase<Unit, Flow<List<Rate>>, GetRateFlowFailure>>(name = "GET_RATE_FLOW")
        val GET_BALANCE_FLOW =
            q<CcUseCase<Unit, Flow<List<Balance>>, GetBalanceFlowFailure>>(name = "GET_BALANCE_FLOW")
        val GET_CURRENCY_FLOW =
            q<CcUseCase<Unit, Flow<List<Currency>>, GetCurrencyFlowFailure>>(name = "GET_CURRENCY_FLOW")
        val CONVERT =
            q<CcUseCase<ConvertUseCaseParams, ConvertUseCaseResult, ConvertUseCaseFailure>>(name = "CONVERT")
    }
}