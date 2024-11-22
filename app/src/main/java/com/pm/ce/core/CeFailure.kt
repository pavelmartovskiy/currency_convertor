package com.pm.ce.core

interface CeFailure

interface InheritedCeFailure : CeFailure {
    val cause: CeFailure
}

interface UnhandledCeFailure : InheritedCeFailure

interface HttpFailure {
    val code: Int
    val message: String
}

data class ThrowableFailure(val throwable: Throwable) : CeFailure

fun Throwable.asFailure() = ThrowableFailure(throwable = this)

