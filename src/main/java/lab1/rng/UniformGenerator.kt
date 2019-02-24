package main.rng

import kotlin.math.pow

class UniformGenerator(
    private val a: Double = 5.0.pow(3),
    private val c: Double = 2.0.pow(12)
): RandomNumberGenerator {

    var z = 1.0

    override fun generate(): Double {
        z = (a * z) % c
        return z / c
    }

}