package com.pm.cc.domain

import com.pm.cc.core.CcFailure
import com.pm.cc.core.CcResult
import com.pm.cc.core.foldFailure
import com.pm.cc.core.map
import com.pm.cc.core.runCatching
import com.pm.cc.core.throwThis
import com.pm.cc.core.toFailure
import com.pm.cc.di.CcComponent
import com.pm.cc.di.inject
import com.pm.cc.domain.CcRepository.ConvertParams

class ConvertUseCase : CcUseCase<ConvertUseCaseParams, ConvertUseCaseResult, ConvertUseCaseFailure>,
    CcComponent {

    private val repository: CcRepository by inject()

    override suspend fun execute(params: ConvertUseCaseParams): CcResult<ConvertUseCaseResult, ConvertUseCaseFailure> =
        runCatching {

            if (params.sellCurrency.id == params.receiveCurrency.id) {
                ConvertUseCaseFailure.SameCurrency(
                    sellCurrency = params.sellCurrency,
                    receiveCurrency = params.receiveCurrency
                ).throwThis()
            }

            val balance = repository.getBalanceBy(currency = params.sellCurrency).getOrThrow()

            val operationNumber = repository.geConvertOperationNumber().getOrThrow()
            val commission = calculateCommission(params.sellAmount, operationNumber)
            val amount = params.sellAmount + commission

            if (balance < amount) {
                ConvertUseCaseFailure
                    .InsufficientBalance(
                        realBalance = balance, need = amount, currency = params.sellCurrency
                    )
                    .throwThis()
            }

            val payload = "Convert ${params.sellAmount} ${params.sellCurrency.code} " +
                    "to ${params.receiveAmount} ${params.receiveCurrency.code}. " +
                    "Rate: ${params.rate} . " +
                    "Commission: $commission . " +
                    "Operation number: $operationNumber"

            repository.convert(
                params = ConvertParams(
                    srcCurrencyId = params.sellCurrency.id,
                    srcAmount = params.sellAmount,
                    dstCurrencyId = params.receiveCurrency.id,
                    dstAmount = params.receiveAmount,
                    commission = commission,
                    payload = payload
                )
            ).map {
                ConvertUseCaseResult(
                    sellCurrency = params.sellCurrency,
                    sellAmount = params.sellAmount,
                    receiveCurrency = params.receiveCurrency,
                    receiveAmount = params.receiveAmount,
                    commission = commission,
                )
            }
                .getOrThrow()
        }
            .foldFailure { failure -> ConvertUseCaseFailure.Unhandled(cause = failure).toFailure() }

    private fun calculateCommission(amount: Double, operationNumber: Long): Double {
        return when {
            operationNumber < 5.0 -> 0.0
            (operationNumber + 1) % 10 == 0L -> 0.0
            else -> amount * COMMISSION_FEE
        }
    }

    companion object {
        const val COMMISSION_FEE = 0.007
    }
}

data class ConvertUseCaseParams(
    val sellCurrency: Currency,
    val sellAmount: Double,
    val receiveCurrency: Currency,
    val receiveAmount: Double,
    val rate: Double
)

sealed class ConvertUseCaseFailure : CcFailure {
    data class Unhandled(val cause: CcFailure) : ConvertUseCaseFailure()
    data class InsufficientBalance(
        val realBalance: Double,
        val need: Double,
        val currency: Currency
    ) :
        ConvertUseCaseFailure()

    data class SameCurrency(val sellCurrency: Currency, val receiveCurrency: Currency) :
        ConvertUseCaseFailure()
}

data class ConvertUseCaseResult(
    val sellCurrency: Currency,
    val sellAmount: Double,
    val receiveCurrency: Currency,
    val receiveAmount: Double,
    val commission: Double,
)