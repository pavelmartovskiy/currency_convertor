package com.pm.cc.data.db

import com.pm.cc.core.CcFailure
import com.pm.cc.core.CcResult
import com.pm.cc.core.UnhandledCcFailure
import com.pm.cc.core.foldFailure
import com.pm.cc.core.runCatching
import com.pm.cc.core.toFailure
import com.pm.cc.data.db.ComplexDataSource.GetBalanceFailure
import com.pm.cc.di.CcComponent
import com.pm.cc.di.Qualifiers
import com.pm.cc.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface ComplexDataSource {

    suspend fun getBalanceBy(): CcResult<Flow<List<BalanceEntity>>, GetBalanceFailure>

    sealed class GetBalanceFailure : CcFailure {
        data class Unhandled(override val cause: CcFailure) : GetBalanceFailure(),
            UnhandledCcFailure
    }

    suspend fun convert(
        srcId: Long,
        srcAmount: Double,
        dstId: Long,
        dstAmount: Double,
        commission: Double,
        payload: String
    ): CcResult<Unit, ConvertFailure>

    sealed class ConvertFailure : CcFailure {
        data class Unhandled(override val cause: CcFailure) : ConvertFailure(),
            UnhandledCcFailure
    }

    suspend fun geConvertOperationNumber(): CcResult<Long, GeConvertOperationNumberFailure>

    sealed class GeConvertOperationNumberFailure : CcFailure {
        data class Unhandled(override val cause: CcFailure) : GeConvertOperationNumberFailure(),
            UnhandledCcFailure
    }

    suspend fun getBalanceBy(currencyId: Long): CcResult<Double, GetBalanceByFailure>

    sealed class GetBalanceByFailure : CcFailure {
        data class Unhandled(override val cause: CcFailure) : GetBalanceByFailure(),
            UnhandledCcFailure
    }

}

internal class ComplexDataSourceImpl : ComplexDataSource, CcComponent {

    private val complexDao: ComplexDao by inject()
    private val context by inject(qualifier = Qualifiers.Coroutines.Contexts.DB)

    override suspend fun getBalanceBy(): CcResult<Flow<List<BalanceEntity>>, GetBalanceFailure> =
        withContext(context = context) {
            runCatching { complexDao.getBalanceAsFlow() }
                .foldFailure { failure -> GetBalanceFailure.Unhandled(cause = failure).toFailure() }
        }

    override suspend fun getBalanceBy(currencyId: Long): CcResult<Double, ComplexDataSource.GetBalanceByFailure> =
        runCatching { complexDao.getBalanceBy(currencyId = currencyId) }
            .foldFailure { failure ->
                ComplexDataSource.GetBalanceByFailure.Unhandled(cause = failure).toFailure()
            }

    override suspend fun convert(
        srcId: Long,
        srcAmount: Double,
        dstId: Long,
        dstAmount: Double,
        commission: Double,
        payload: String
    ): CcResult<Unit, ComplexDataSource.ConvertFailure> = runCatching {
        complexDao.convert(
            srcId = srcId,
            srcAmount = srcAmount,
            dstId = dstId,
            dstAmount = dstAmount,
            commission = commission,
            payload = payload
        )
    }
        .foldFailure { failure ->
            ComplexDataSource.ConvertFailure.Unhandled(cause = failure).toFailure()
        }

    override suspend fun geConvertOperationNumber(): CcResult<Long, ComplexDataSource.GeConvertOperationNumberFailure> =
        runCatching {
            complexDao.getConvertOperationNumber()
        }
            .foldFailure { failure ->
                ComplexDataSource.GeConvertOperationNumberFailure.Unhandled(cause = failure)
                    .toFailure()
            }
}