package com.pm.cc.data.db

import com.pm.cc.core.CcFailure
import com.pm.cc.core.CcResult
import com.pm.cc.core.UnhandledCcFailure
import com.pm.cc.core.foldFailure
import com.pm.cc.core.runCatching
import com.pm.cc.core.toFailure
import com.pm.cc.data.db.RateDataSource.*
import com.pm.cc.di.CcComponent
import com.pm.cc.di.Qualifiers
import com.pm.cc.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


interface RateDataSource {

    sealed class GetCurrencyCodesFailure : CcFailure {
        data class Unhandled(
            override val cause: CcFailure
        ) : GetCurrencyCodesFailure(), UnhandledCcFailure
    }

    suspend fun create(rates: List<RateEntity>): CcResult<Unit, CreateFailure>

    sealed class CreateFailure : CcFailure {
        data class Unhandled(
            override val cause: CcFailure
        ) : CreateFailure(), UnhandledCcFailure
    }

    suspend fun getAllAsFlow(): CcResult<Flow<List<ExtRateEntity>>, GetAllFailure>

    sealed class GetAllFailure : CcFailure {
        data class Unhandled(
            override val cause: CcFailure
        ) : GetAllFailure(), UnhandledCcFailure
    }
}

internal class RateDataSourceImpl : RateDataSource, CcComponent {

    private val rateDao: RateDao by inject()
    private val coroutineContext by inject(qualifier = Qualifiers.Coroutines.Contexts.DB)

    override suspend fun create(rates: List<RateEntity>): CcResult<Unit, CreateFailure> =
        withContext(context = coroutineContext) {
            runCatching { rateDao.create(rates = rates) }
                .foldFailure { failure -> CreateFailure.Unhandled(cause = failure).toFailure() }
        }

    override suspend fun getAllAsFlow(): CcResult<Flow<List<ExtRateEntity>>, GetAllFailure> =
        withContext(context = coroutineContext) {
            runCatching { rateDao.getAllAsFlow() }
                .foldFailure { failure -> GetAllFailure.Unhandled(cause = failure).toFailure() }
        }

}



