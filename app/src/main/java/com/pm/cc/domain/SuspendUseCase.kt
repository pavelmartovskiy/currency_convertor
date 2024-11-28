package com.pm.cc.domain

import com.pm.cc.core.CcFailure
import com.pm.cc.core.CcResult

fun interface SuspendUseCase<S, D, T : CcFailure> {
    suspend fun invoke(source: S): CcResult<D, T>
}