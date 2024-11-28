package com.pm.cc.core

interface CcFailure

interface InheritedCcFailure : CcFailure {
    val cause: CcFailure
}

interface UnhandledCcFailure : InheritedCcFailure

interface HttpFailure {
    val code: Int
    val message: String
}

data class ThrowableFailure(val throwable: Throwable) : CcFailure

fun Throwable.asFailure() = ThrowableFailure(throwable = this)


fun CcFailure.throwThis() {
    throw CeThrowable(failure = this)
}