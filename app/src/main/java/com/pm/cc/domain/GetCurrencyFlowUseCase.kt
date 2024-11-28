package com.pm.cc.domain

import com.pm.cc.core.CcResult
import com.pm.cc.di.CcComponent
import com.pm.cc.di.inject
import kotlinx.coroutines.flow.Flow

class GetCurrencyFlowUseCase : FlowUseCase<Unit, List<Currency>, CcRepository.GetCurrencyFlowFailure>,
    CcComponent {

    private val repository: CcRepository by inject()

    override suspend fun execute(params: Unit): CcResult<Flow<List<Currency>>, CcRepository.GetCurrencyFlowFailure> =
        repository.getCurrencyFlow()
}