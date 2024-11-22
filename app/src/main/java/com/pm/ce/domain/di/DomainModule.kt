package com.pm.ce.domain.di

import com.pm.ce.domain.CeSubscription
import com.pm.ce.domain.RateSubscription
import com.pm.ce.domain.Rates
import org.koin.dsl.module

object DomainModule {
    fun modules() = module {
        single<CeSubscription<Unit, Rates>>(qualifier = CeSubscriptionQualifiers.RATE) {
            RateSubscription(repository = get())
        }
    }
}