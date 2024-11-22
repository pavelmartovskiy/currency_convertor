package com.pm.ce.domain

import com.pm.ce.core.CeFailure
import com.pm.ce.core.CeResult

fun interface SuspendUseCase<S, D, T : CeFailure> {
    suspend fun invoke(source: S): CeResult<D, T>
}