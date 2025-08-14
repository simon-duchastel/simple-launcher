package com.duchastel.simon.simplelauncher.coreext.outcome

sealed interface Outcome<out S, out F> {

    data class Success<out S>(val value: S) : Outcome<S, Nothing>
    data class Failure<out F>(val reason: F) : Outcome<Nothing, F>

    fun get(): Success<S>? = (this as? Success<S>)
    fun unwrap(): S? = get()?.value

    fun failure(): Failure<F>? = (this as? Failure<F>)
    fun failureReason(): F? = failure()?.reason
}

fun <S> S.asSuccess(): Outcome.Success<S> = Outcome.Success(this)
fun <F> F.asFailure(): Outcome.Failure<F> = Outcome.Failure(this)

val BasicSuccess: Outcome.Success<Unit> = Unit.asSuccess()
val BasicFailure: Outcome.Failure<Unit> = Unit.asFailure()