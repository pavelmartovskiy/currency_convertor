package com.pm.cc.domain

import com.pm.cc.core.CcResult
import com.pm.cc.di.CcComponent
import com.pm.cc.di.inject
import com.pm.cc.domain.CcRepository.GetRateFlowFailure
import kotlinx.coroutines.flow.Flow

class GetRateFlowUseCase : FlowUseCase<Unit, List<Rate>, GetRateFlowFailure>, CcComponent {

    private val repository: CcRepository by inject()

    override suspend fun execute(params: Unit): CcResult<Flow<List<Rate>>, GetRateFlowFailure> =
        repository.getRateFlow()
}