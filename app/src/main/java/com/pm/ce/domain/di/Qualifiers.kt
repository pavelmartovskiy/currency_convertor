package com.pm.ce.domain.di

import com.pm.ce.domain.RateSubscription
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named

object CeUseCaseQualifiers {
}

object CeSubscriptionQualifiers {
    val RATE: Qualifier = named<RateSubscription>()
}

