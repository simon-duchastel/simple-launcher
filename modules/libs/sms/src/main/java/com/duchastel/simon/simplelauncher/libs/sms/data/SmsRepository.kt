package com.duchastel.simon.simplelauncher.libs.sms.data

/**
 * Repository for sending, receiving, and managing SMS messages.
 */
interface SmsRepository {

    /**
     * Sends an SMS message to the specified phone number.
     *
     * @param phoneNumber The recipient's phone number in international or local format.
     * @param message The content of the SMS to be sent.
     * @return `true` if the message was sent successfully, `false` otherwise.
     */
    suspend fun sendSms(phoneNumber: String, message: String): SendSmsResult
}

typealias SendSmsResult = Outcome<AsyncOutcome<DeliveredSmsResult>, Unit>
typealias DeliveredSmsResult = Outcome<Boolean, Unit>

interface AsyncOutcome<O: Outcome<*, *>> {
    suspend operator fun invoke(): O
}

fun <O: Outcome<*, *>> createAsyncOutcome(action: suspend () -> O): AsyncOutcome<O> {
    return object : AsyncOutcome<O> {
        override suspend fun invoke(): O {
            return action()
        }
    }
}

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