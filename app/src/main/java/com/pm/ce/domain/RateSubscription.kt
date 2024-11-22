package com.pm.ce.domain

import kotlinx.coroutines.flow.Flow

class RateSubscription(private val repository: CeRepository) : CeSubscription<Unit, Rates> {
    override fun subscribe(params: Unit): Flow<Rates> = repository.subscribeToRates()
}