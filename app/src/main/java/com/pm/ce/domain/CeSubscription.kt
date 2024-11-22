package com.pm.ce.domain

import kotlinx.coroutines.flow.Flow

fun interface CeSubscription<P, T> {
    fun subscribe(params: P): Flow<T>
}