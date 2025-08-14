package com.duchastel.simon.simplelauncher.coreext.outcome

interface AsyncOutcome<O: Outcome<*, *>> {
    suspend fun await(): O
}

fun <O: Outcome<*, *>> asyncOutcome(action: suspend () -> O): AsyncOutcome<O> {
    return object : AsyncOutcome<O> {
        override suspend fun await(): O {
            return action()
        }
    }
}