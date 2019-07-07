package de.codecentric.hikaku.reporters

/**
 * Receives the result and does nothing.
 */
@Suppress("unused")
class NoOperationReporter : Reporter {
    override fun report(endpointMatchResult: MatchResult) { }
}