package main.rng

class NormalGenerator(
    private val alpha: Double,
    private val sigma: Double,
    private val generator: RandomNumberGenerator
): RandomNumberGenerator {

    override fun generate() =
        sigma * mu() + alpha

    private fun mu() =
        generateSequence { generator.generate() }
            .take(12)
            .sum()
            .minus(6)
}