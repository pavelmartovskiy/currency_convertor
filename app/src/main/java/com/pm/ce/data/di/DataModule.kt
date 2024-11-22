package com.pm.ce.data.di

import com.pm.ce.data.CeRepositoryImpl
import com.pm.ce.domain.CeRepository
import org.koin.dsl.module

object DataModule {
    fun modules() = module {
        single<CeRepository> { CeRepositoryImpl() }
    }
}