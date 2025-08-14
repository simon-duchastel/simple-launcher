package com.duchastel.simon.simplelauncher.coreext.outcome

/**
 * Represents an asynchronous operation that will eventually produce an [Outcome].
 * @param O The type of the [Outcome].
 */
interface AsyncOutcome<O: Outcome<*, *>> {
    /**
     * Awaits the result of the asynchronous operation.
     * @return The [Outcome] of the operation.
     */
    suspend fun await(): O
}

/**
 * Creates an [AsyncOutcome] from a suspendable action.
 * @param action The suspendable action that produces the [Outcome].
 * @return An [AsyncOutcome] that will execute the action when awaited.
 */
fun <O: Outcome<*, *>> asyncOutcome(action: suspend () -> O): AsyncOutcome<O> {
    return object : AsyncOutcome<O> {
        override suspend fun await(): O {
            return action()
        }
    }
}