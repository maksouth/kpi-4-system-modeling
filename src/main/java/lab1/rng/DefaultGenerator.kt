package main.rng

import kotlin.random.Random

class DefaultGenerator: RandomNumberGenerator {
    override fun generate() = Random.nextDouble()
}