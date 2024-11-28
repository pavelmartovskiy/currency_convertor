package com.pm.cc.data.repo

import android.util.Log
import com.pm.cc.core.CcResult
import com.pm.cc.core.CurrencyCode
import com.pm.cc.core.fold
import com.pm.cc.core.foldFailure
import com.pm.cc.core.foldSuccess
import com.pm.cc.core.map
import com.pm.cc.core.onFailure
import com.pm.cc.core.onSuccess
import com.pm.cc.core.runCatching
import com.pm.cc.core.toFailure
import com.pm.cc.core.toSuccess
import com.pm.cc.data.CurrencyExchangeRates
import com.pm.cc.data.db.ComplexDataSource
import com.pm.cc.data.db.CurrencyDataSource
import com.pm.cc.data.db.CurrencyEntity
import com.pm.cc.data.db.RateDataSource
import com.pm.cc.data.db.RateEntity
import com.pm.cc.data.network.NetworkRatesDataSource
import com.pm.cc.di.CcComponent
import com.pm.cc.di.Qualifiers
import com.pm.cc.di.inject
import com.pm.cc.domain.Balance
import com.pm.cc.domain.CcRepository
import com.pm.cc.domain.CcRepository.*
import com.pm.cc.domain.Currency
import com.pm.cc.domain.Rate
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CcRepositoryImpl : CcRepository, CcComponent {

    private val TAG = "CeRepositoryImpl"

    private val scope by inject(qualifier = Qualifiers.Coroutines.Scopes.APP)

    private val networkRatesDataSource: NetworkRatesDataSource by inject()
    private val currencyDataSource: CurrencyDataSource by inject()
    private val complexDataSource: ComplexDataSource by inject()

    private val rateDataSource: RateDataSource by inject()

    private val extRateEntityToRateMapper by inject(qualifier = Qualifiers.Mappers.EXT_RATE_ENTITY_TO_RATE)
    private val balanceEntityBalanceMapper by inject(qualifier = Qualifiers.Mappers.BALANCE_ENTITY_TO_BALANCE)
    private val currencyEntityToCurrencyMapper by inject(qualifier = Qualifiers.Mappers.CURRENCY_ENTITY_TO_CURRENCY)

    init {
        scope.launch {
            while (isActive) {
                Log.v(TAG, " updateRates ")
                networkRatesDataSource.rates()
                    .onFailure { failure -> Log.v(TAG, "onFailure: $failure") }
                    .onSuccess { result -> handleSuccess(rates = result) }
                delay(5_000)
            }
        }
    }


    private suspend fun handleSuccess(rates: CurrencyExchangeRates) {
        updateCurrencyCodes(rates = rates)
        updateRates(rates)
    }

    private suspend fun updateRates(rates: CurrencyExchangeRates) {
        currencyDataSource
            .getAll()
            .foldSuccess { currencies ->
                runCatching {
                    currencies.associate { currency -> currency.code to requireNotNull(currency.id) }
                }
            }
            .foldSuccess { idMap ->
                runCatching {

                    val date = parseDate(string = rates.date).getOrThrow()
                    val baseCurrencyId = requireNotNull(value = idMap[rates.base])

                    rates.rates.flatMap { entry ->
                        val currencyId: Long = requireNotNull(value = idMap[entry.key])
                        listOf(
                            RateEntity(
                                date = date,
                                src = baseCurrencyId,
                                dst = currencyId,
                                rate = entry.value
                            ),
                            RateEntity(
                                date = date,
                                src = currencyId,
                                dst = baseCurrencyId,
                                rate = 1.0 / entry.value
                            )
                        )
                    }
                }
            }
            .onSuccess { entities -> rateDataSource.create(rates = entities) }
            .onFailure { failure -> Log.v(TAG, "failure updateRates: $failure") }
    }

    private suspend fun updateCurrencyCodes(rates: CurrencyExchangeRates) {
        currencyDataSource
            .getAll()
            .map { currencies ->
                val codes: List<CurrencyCode> = currencies.map { currency -> currency.code }
                rates.rates.keys
                    .filter { code -> code !in codes }
                    .map { newCode -> CurrencyEntity(code = newCode) }
            }
            .onSuccess { newCodes ->
                if (newCodes.isNotEmpty()) {
                    currencyDataSource.create(currencies = newCodes)
                }
            }
            .onFailure { failure -> Log.v(TAG, "failure updateCurrencyCodes: $failure") }
    }

    companion object {

        private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        fun parseDate(string: String): Result<Date> = kotlin.runCatching {
            dateFormat.parse(string)
                ?: throw IllegalArgumentException(" Invalid date format : $string")
        }
    }

    override suspend fun getRateFlow(): CcResult<Flow<List<Rate>>, GetRateFlowFailure> =
        rateDataSource
            .getAllAsFlow()
            .fold(
                onFailure = { failure ->
                    GetRateFlowFailure.Unhandled(cause = failure).toFailure()
                },
                onSuccess = { flow ->
                    flow
                        .map { entities ->
                            entities.map { entity ->
                                extRateEntityToRateMapper.map(src = entity)
                            }
                        }
                        .toSuccess()
                }
            )

    override suspend fun getBalanceFlow(): CcResult<Flow<List<Balance>>, GetBalanceFlowFailure> =
        complexDataSource.getBalanceBy()
            .fold(
                onSuccess = { flow ->
                    flow
                        .map { balance ->
                            balance
                                .map { entity -> balanceEntityBalanceMapper.map(src = entity) }
                                .sortedWith { balance1, balance2 ->
                                    when {
                                        balance1.amount > 0.0 && balance2.amount <= 0.0 -> -1
                                        balance1.amount <= 0.0 && balance2.amount > 0.0 -> 1
                                        else -> balance1.currencyCode.compareTo(other = balance2.currencyCode)
                                    }
                                }
                        }
                        .toSuccess()
                },
                onFailure = { failure ->
                    GetBalanceFlowFailure.Unhandled(cause = failure).toFailure()
                }
            )

    override suspend fun getCurrencyFlow(): CcResult<Flow<List<Currency>>, GetCurrencyFlowFailure> {

        return currencyDataSource
            .getAllAsFlow()
            .fold(
                onSuccess = { flow ->
                    flow.map { currencies ->
                        currencies.map { currencyEntity ->
                            currencyEntityToCurrencyMapper.map(src = currencyEntity)
                        }
                    }.toSuccess()
                },
                onFailure = { failure ->
                    GetCurrencyFlowFailure.Unhandled(cause = failure).toFailure()
                }
            )
    }

    override suspend fun convert(params: ConvertParams): CcResult<Unit, ConvertFailure> =
        complexDataSource.convert(
            srcId = params.srcCurrencyId,
            srcAmount = params.srcAmount,
            dstId = params.dstCurrencyId,
            dstAmount = params.dstAmount,
            commission = params.commission,
            payload = params.payload
        )
            .foldFailure { failure -> ConvertFailure.Unhandled(cause = failure).toFailure() }

    override suspend fun geConvertOperationNumber(): CcResult<Long, GetConvertOperationNumberFailure> =
        complexDataSource.geConvertOperationNumber()
            .foldFailure { failure ->
                GetConvertOperationNumberFailure.Unhandled(cause = failure).toFailure()
            }

    override suspend fun getBalanceBy(currency: Currency): CcResult<Double, GetBalanceByFailure> =
        complexDataSource.getBalanceBy(currencyId = currency.id)
            .foldFailure { failure ->
                GetBalanceByFailure.Unhandled(cause = failure).toFailure()
            }

}

