package com.duchastel.simon.simplelauncher.coreext.outcome

/**
 * A sealed interface representing the outcome of an operation that can either succeed or fail.
 * @param S The type of the success value.
 * @param F The type of the failure reason.
 */
sealed interface Outcome<out S, out F> {

    /**
     * Represents a successful outcome.
     * @param value The success value.
     */
    data class Success<out S>(val value: S) : Outcome<S, Nothing>

    /**
     * Represents a failed outcome.
     * @param reason The reason for the failure.
     */
    data class Failure<out F>(val reason: F) : Outcome<Nothing, F>

    /**
     * @return The [Success] instance if the outcome is a success, `null` otherwise.
     */
    fun get(): Success<S>? = (this as? Success<S>)

    /**
     * @return The success value if the outcome is a success, `null` otherwise.
     */
    fun unwrap(): S? = get()?.value

    /**
     * @return The [Failure] instance if the outcome is a failure, `null` otherwise.
     */
    fun failure(): Failure<F>? = (this as? Failure<F>)

    /**
     * @return The failure reason if the outcome is a failure, `null` otherwise.
     */
    fun failureReason(): F? = failure()?.reason
}

/**
 * Wraps the receiver in an [Outcome.Success].
 */
fun <S> S.asSuccess(): Outcome.Success<S> = Outcome.Success(this)

/**
 * Wraps the receiver in an [Outcome.Failure].
 */
fun <F> F.asFailure(): Outcome.Failure<F> = Outcome.Failure(this)

/**
 * A basic success outcome with no value.
 */
val BasicSuccess: Outcome.Success<Unit> = Unit.asSuccess()

/**
 * A basic failure outcome with no reason.
 */
val BasicFailure: Outcome.Failure<Unit> = Unit.asFailure()