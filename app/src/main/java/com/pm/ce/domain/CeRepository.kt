package com.pm.ce.domain

import kotlinx.coroutines.flow.Flow

interface CeRepository {

    fun subscribeToRates() : Flow<Rates>

}

