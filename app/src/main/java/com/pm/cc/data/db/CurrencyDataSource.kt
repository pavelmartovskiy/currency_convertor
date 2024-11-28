package com.pm.cc.data.db

import com.pm.cc.core.CcFailure
import com.pm.cc.core.CcResult
import com.pm.cc.core.UnhandledCcFailure
import com.pm.cc.core.foldFailure
import com.pm.cc.core.runCatching
import com.pm.cc.core.toFailure
import com.pm.cc.data.db.CurrencyDataSource.*
import com.pm.cc.di.CcComponent
import com.pm.cc.di.Qualifiers
import com.pm.cc.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


interface CurrencyDataSource {

    suspend fun getAll(): CcResult<List<CurrencyEntity>, GetAllFailure>

    sealed class GetAllFailure : CcFailure {
        data class Unhandled(override val cause: CcFailure) : GetAllFailure(),
            UnhandledCcFailure
    }

    suspend fun getAllAsFlow(): CcResult<Flow<List<CurrencyEntity>>, GetAllFailure>

    suspend fun create(currencies: List<CurrencyEntity>): CcResult<Unit, CreateFailure>

    sealed class CreateFailure : CcFailure {
        data class Unhandled(override val cause: CcFailure) : CreateFailure(),
            UnhandledCcFailure
    }
}

internal class CurrencyDataSourceImpl : CurrencyDataSource, CcComponent {

    private val currencyCodeDao: CurrencyCodeDao by inject()
    private val coroutineContext by inject(qualifier = Qualifiers.Coroutines.Contexts.DB)

    override suspend fun getAll(): CcResult<List<CurrencyEntity>, GetAllFailure> =
        withContext(context = coroutineContext) {
            runCatching { currencyCodeDao.getAll() }
                .foldFailure { failure -> GetAllFailure.Unhandled(cause = failure).toFailure() }
        }

    override suspend fun getAllAsFlow(): CcResult<Flow<List<CurrencyEntity>>, GetAllFailure> =
        withContext(context = coroutineContext) {
            runCatching { currencyCodeDao.getAllAsFlow() }
                .foldFailure { failure -> GetAllFailure.Unhandled(cause = failure).toFailure() }
        }

    override suspend fun create(currencies: List<CurrencyEntity>): CcResult<Unit, CreateFailure> =
        withContext(context = coroutineContext) {
            runCatching { currencyCodeDao.create(currencies = currencies) }
                .foldFailure { failure -> CreateFailure.Unhandled(cause = failure).toFailure() }
        }
}



