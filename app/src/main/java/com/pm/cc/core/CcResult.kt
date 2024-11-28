package com.pm.cc.core

import com.pm.cc.core.CcResult.Companion.failure
import com.pm.cc.core.CcResult.Companion.success

@JvmInline
value class CcResult<out T, out F : CcFailure> internal constructor(val value: Any?) {

    val isSuccess: Boolean get() = value !is Failure<*>
    val isFailure: Boolean get() = value is Failure<*>

    @Suppress("UNCHECKED_CAST")
    fun getOrNull(): T? = if (isFailure) null else value as T

    @Suppress("UNCHECKED_CAST")
    fun getOrThrow(): T = when (value) {
        is Failure<*> -> throw CeThrowable(failure = value.failure)
        else -> value as T
    }

    fun failureOrNull(): CcFailure? =
        when (value) {
            is Failure<*> -> value.failure
            else -> null
        }

    companion object {

        @Suppress("INAPPLICABLE_JVM_NAME")
        @JvmName("failure")
        fun <T, F : CcFailure> success(value: T): CcResult<T, F> =
            CcResult(value = value)

        @Suppress("INAPPLICABLE_JVM_NAME")
        @JvmName("failure")
        fun <T, F : CcFailure> failure(failure: F): CcResult<T, F> =
            CcResult(value = Failure(failure = failure))

    }

    internal data class Failure<F : CcFailure>(val failure: F)

}

inline fun <reified T, F : CcFailure> T.toSuccess() = success<T, F>(value = this)
inline fun <reified T, F : CcFailure> F.toFailure() = failure<T, F>(failure = this)

inline fun <reified T, F : CcFailure> CcResult<T, F>.onSuccess(block: (T) -> Unit): CcResult<T, F> =
    also { if (isSuccess) block.invoke(value as T) }

inline fun <T, reified F : CcFailure> CcResult<T, F>.onFailure(block: (F) -> Unit): CcResult<T, F> =
    also { failureOrNull()?.let { failure -> block(failure as F) } }

inline fun <reified T, reified F : CcFailure, R> CcResult<T, F>.fold(
    onSuccess: (value: T) -> R,
    onFailure: (failure: F) -> R
): R = when (val failure = failureOrNull()) {
    null -> onSuccess(value as T)
    else -> onFailure(failure as F)
}

inline fun <reified T, reified F : CcFailure, reified R> CcResult<T, F>.map(
    onSuccess: (value: T) -> R,
): CcResult<R, F> = when (val failure = failureOrNull()) {
    null -> onSuccess(value as T).toSuccess<R, F>()
    else -> failure(failure = failure as F)
}

inline fun <reified T, reified F : CcFailure, R> CcResult<T, F>.foldSuccess(
    onSuccess: (value: T) -> CcResult<R, F>,
): CcResult<R, F> = when (val failure = failureOrNull()) {
    null -> onSuccess(value as T)
    else -> failure(failure = failure as F)
}

class CeThrowable(val failure: CcFailure) : Throwable()

inline fun <T, R> T.runCatching(block: T.() -> R): CcResult<R, CcFailure> {
    return try {
        success(value = block())
    } catch (throwable: CeThrowable) {
        failure(failure = throwable.failure)
    } catch (throwable: Throwable) {
        failure(failure = ThrowableFailure(throwable = throwable))
    }
}

inline fun <reified T, reified SF : CcFailure, RF : CcFailure> CcResult<T, SF>.foldFailure(
    onFailure: (failure: SF) -> CcResult<T, RF>,
): CcResult<T, RF> = when (val failure = failureOrNull()) {
    null -> success(value = value as T)
    else -> onFailure(failure as SF)
}


