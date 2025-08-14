package com.duchastel.simon.simplelauncher.coreext.standard

import kotlinx.coroutines.delay
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

/**
 * Calls the specified function [block] and returns its result, suspending for at least
 * [atLeast] amount of time. If [block] takes less than that amount of time to run, this function
 * does not return until the total amount of time elapsed is [atLeast].
 *
 * @param clock [Clock] to use to measure time. Defaults to [Clock.System].
 */
@OptIn(ExperimentalTime::class, ExperimentalContracts::class)
private suspend inline fun <R> runForAtLeast(
    atLeast: Duration,
    clock: Clock = Clock.System,
    block: suspend () -> R,
): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    val startedAt = clock.now()
    val result = block()
    val timeElapsed = clock.now() - startedAt
    if (timeElapsed < atLeast) {
        delay(atLeast - timeElapsed)
    }

    return result
}