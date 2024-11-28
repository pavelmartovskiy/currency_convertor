package com.pm.cc.domain

import com.pm.cc.core.CcFailure
import com.pm.cc.core.CcResult
import com.pm.cc.core.UnhandledCcFailure
import kotlinx.coroutines.flow.Flow

interface CcRepository {

    suspend fun getRateFlow(): CcResult<Flow<List<Rate>>, GetRateFlowFailure>

    sealed class GetRateFlowFailure : CcFailure {
        data class Unhandled(
            override val cause: CcFailure
        ) : GetRateFlowFailure(), UnhandledCcFailure
    }

    suspend fun getBalanceFlow(): CcResult<Flow<List<Balance>>, GetBalanceFlowFailure>

    sealed class GetBalanceFlowFailure : CcFailure {
        data class Unhandled(
            override val cause: CcFailure
        ) : GetBalanceFlowFailure(), UnhandledCcFailure
    }

    suspend fun getCurrencyFlow(): CcResult<Flow<List<Currency>>, GetCurrencyFlowFailure>

    sealed class GetCurrencyFlowFailure : CcFailure {
        data class Unhandled(
            override val cause: CcFailure
        ) : GetCurrencyFlowFailure(), UnhandledCcFailure
    }

    suspend fun convert(params: ConvertParams): CcResult<Unit, ConvertFailure>

    data class ConvertParams(
        val srcCurrencyId: Long,
        val srcAmount: Double,
        val dstCurrencyId: Long,
        val dstAmount: Double,
        val commission: Double,
        val payload: String
    )

    sealed class ConvertFailure : CcFailure {
        data class Unhandled(override val cause: CcFailure) : ConvertFailure(), UnhandledCcFailure
    }

    suspend fun geConvertOperationNumber(): CcResult<Long, GetConvertOperationNumberFailure>

    sealed class GetConvertOperationNumberFailure : CcFailure {
        data class Unhandled(override val cause: CcFailure) : GetConvertOperationNumberFailure(),
            UnhandledCcFailure
    }

    suspend fun getBalanceBy(currency: Currency): CcResult<Double, GetBalanceByFailure>

    sealed class GetBalanceByFailure : CcFailure {
        data class Unhandled(override val cause: CcFailure) : GetBalanceByFailure(),
            UnhandledCcFailure
    }
}

