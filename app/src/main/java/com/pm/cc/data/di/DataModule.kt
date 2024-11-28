package com.pm.cc.data.di

import com.pm.cc.data.repo.CcRepositoryImpl
import com.pm.cc.data.network.NetworkRatesDataSource
import com.pm.cc.data.network.NetworkRatesDataSourceImpl
import com.pm.cc.domain.CcRepository
import org.koin.dsl.module

object DataModule {
    fun modules() = module {
        single<NetworkRatesDataSource> { NetworkRatesDataSourceImpl() }
        single<CcRepository> { CcRepositoryImpl() }
    }
}