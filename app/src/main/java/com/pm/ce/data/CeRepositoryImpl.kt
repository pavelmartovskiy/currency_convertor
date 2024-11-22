package com.pm.ce.data

import com.pm.ce.domain.CeRepository
import com.pm.ce.domain.Rates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CeRepositoryImpl : CeRepository {

    private val ratesState = MutableStateFlow(value = Rates(data = emptyMap()))

    override fun subscribeToRates(): Flow<Rates> = ratesState.asStateFlow()

}