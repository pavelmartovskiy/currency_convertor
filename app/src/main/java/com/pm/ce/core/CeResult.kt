package com.pm.ce.core

@JvmInline
value class CeResult<out T, out F : CeFailure> internal constructor(val value: Any?) {

    val isSuccess: Boolean get() = value !is Failure<*>
    val isFailure: Boolean get() = value is Failure<*>

    @Suppress("UNCHECKED_CAST")
    fun getOrNull(): T? = if (isFailure) null else value as T

    fun failureOrNull(): CeFailure? =
        when (value) {
            is Failure<*> -> value.failure
            else -> null
        }

    companion object {

        @Suppress("INAPPLICABLE_JVM_NAME")
        @JvmName("failure")
        fun <T, F : CeFailure> success(value: T): CeResult<T, F> =
            CeResult(value = value)

        @Suppress("INAPPLICABLE_JVM_NAME")
        @JvmName("failure")
        fun <T, F : CeFailure> failure(failure: F): CeResult<T, F> =
            CeResult(value = Failure(failure = failure))

    }

    internal data class Failure<F : CeFailure>(val failure: F)
}

inline fun <reified T, F : CeFailure> T.toSuccess() = CeResult.success<T, F>(value = this)
inline fun <reified T, F : CeFailure> F.toFailure() =
    CeResult.failure<T, F>(failure = this)

inline fun <reified T, F : CeFailure> CeResult<T, F>.onSuccess(block: (T) -> Unit): CeResult<T, F> =
    also { if (isSuccess) block.invoke(value as T) }

inline fun <T, reified F : CeFailure> CeResult<T, F>.onFailure(block: (F) -> Unit): CeResult<T, F> =
    also { failureOrNull()?.let { failure -> block(failure as F) } }

