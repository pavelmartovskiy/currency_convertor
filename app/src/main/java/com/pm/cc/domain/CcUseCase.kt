package com.pm.cc.domain

import com.pm.cc.core.CcFailure
import com.pm.cc.core.CcResult
import kotlinx.coroutines.flow.Flow

fun interface CcUseCase<P, T, F : CcFailure> {
    suspend fun execute(params: P): CcResult<T, F>
}

fun interface FlowUseCase<P, T, F : CcFailure> : CcUseCase<P, Flow<T>, F>