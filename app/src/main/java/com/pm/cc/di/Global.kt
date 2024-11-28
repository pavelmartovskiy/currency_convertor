package com.pm.cc.di

import android.util.Log
import androidx.room.Room
import com.pm.cc.data.db.CcDatabase
import com.pm.cc.data.db.CcDatabaseCallback
import com.pm.cc.data.db.ComplexDataSource
import com.pm.cc.data.db.ComplexDataSourceImpl
import com.pm.cc.data.db.CurrencyCodeDao
import com.pm.cc.data.db.CurrencyDataSource
import com.pm.cc.data.db.CurrencyDataSourceImpl
import com.pm.cc.data.db.RateDao
import com.pm.cc.data.db.RateDataSource
import com.pm.cc.data.db.RateDataSourceImpl
import com.pm.cc.data.repo.BalanceEntityToBalanceMapper
import com.pm.cc.data.repo.CurrencyEntityToCurrencyMapper
import com.pm.cc.data.repo.ExtRateEntityToRateMapper
import com.pm.cc.domain.ConvertUseCase
import com.pm.cc.domain.GetBalanceFlowUseCase
import com.pm.cc.domain.GetCurrencyFlowUseCase
import com.pm.cc.domain.GetRateFlowUseCase
import com.pm.cc.presentation.screen.home.mapper.BalanceToBalanceItemMapper
import com.pm.cc.presentation.screen.home.mapper.CurrencyItemToCurrencyMapper
import com.pm.cc.presentation.screen.home.mapper.CurrencyToCurrencyItemMapper
import com.pm.cc.presentation.screen.home.mapper.RatesToRateMapMapper
import com.pm.cc.presentation.screen.home.model.HomeViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

object Global {

    fun coroutineContextModule() = module {
        single(qualifier = Qualifiers.Coroutines.Contexts.MAIN) { Dispatchers.Main }
        single(qualifier = Qualifiers.Coroutines.Contexts.IO) { Dispatchers.IO }
        single(qualifier = Qualifiers.Coroutines.Contexts.COMPUTATION) { Dispatchers.Default }
        single(qualifier = Qualifiers.Coroutines.Contexts.DB) { Dispatchers.Default }
    }

    fun coroutineScopeModule() = module {
        single(qualifier = Qualifiers.Coroutines.Scopes.APP) {
            CoroutineScope(
                context = SupervisorJob()
                        + get<CoroutineContext>(qualifier = Qualifiers.Coroutines.Contexts.MAIN.koinQualifier)
                        + CoroutineExceptionHandler { _, throwable ->
                    Log.e("GLOBAL", "Coroutine exception :", throwable)
                })
        }
    }

    fun dbModule() = module {
        single<CcDatabase> {
            Room
                .databaseBuilder(get(), CcDatabase::class.java, "ce_database.db")
                .addCallback(CcDatabaseCallback())
                .build()
        }
        single<RateDao> { get<CcDatabase>().rateDao() }
        single<CurrencyCodeDao> { get<CcDatabase>().currencyCodeDao() }
        single { get<CcDatabase>().complexDao() }
    }

    fun dataSourceModule() = module {
        single<CurrencyDataSource> { CurrencyDataSourceImpl() }
        single<RateDataSource> { RateDataSourceImpl() }
        single<ComplexDataSource> { ComplexDataSourceImpl() }
    }

    fun mapperModule() = module {
        single(qualifier = Qualifiers.Mappers.EXT_RATE_ENTITY_TO_RATE) { ExtRateEntityToRateMapper() }
        single(qualifier = Qualifiers.Mappers.RATES_TO_RATE_MAP_ITEM) { RatesToRateMapMapper() }
        single(qualifier = Qualifiers.Mappers.BALANCE_ENTITY_TO_BALANCE) { BalanceEntityToBalanceMapper() }
        single(qualifier = Qualifiers.Mappers.BALANCE_TO_BALANCE_ITEM) { BalanceToBalanceItemMapper() }
        single(qualifier = Qualifiers.Mappers.CURRENCY_ENTITY_TO_CURRENCY) { CurrencyEntityToCurrencyMapper() }
        single(qualifier = Qualifiers.Mappers.CURRENCY_TO_CURRENCY_ITEM) { CurrencyToCurrencyItemMapper() }
        single(qualifier = Qualifiers.Mappers.CURRENCY_ITEM_TO_CURRENCY) { CurrencyItemToCurrencyMapper() }
    }

    fun useCaseModule() = module {
        single(qualifier = Qualifiers.UseCases.GET_RATE_FLOW) { GetRateFlowUseCase() }
        single(qualifier = Qualifiers.UseCases.GET_BALANCE_FLOW) { GetBalanceFlowUseCase() }
        single(qualifier = Qualifiers.UseCases.GET_CURRENCY_FLOW) { GetCurrencyFlowUseCase() }
        single(qualifier = Qualifiers.UseCases.CONVERT) { ConvertUseCase() }
    }

    fun screensModule() = module {
        single { HomeViewModel() }
    }

}