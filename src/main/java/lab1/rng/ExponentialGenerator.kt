package main.rng

import kotlin.math.ln

class ExponentialGenerator(
    private val lambda: Double,
    private val generator: RandomNumberGenerator
): RandomNumberGenerator {

    override fun generate(): Double =
            -ln(generator.generate()) / lambda

}