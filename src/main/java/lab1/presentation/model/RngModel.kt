package main.presentation.model

import kotlin.math.pow
import kotlin.math.roundToInt

class RngModel {


    fun calculateDispersion(randomSequence: List<Double>): Double {
        val average = calculateAverage(randomSequence)

        return randomSequence.map { it - average }
            .map { it.pow(2) }
            .sum()
            .div(randomSequence.size - 1)
    }

    fun calculateAverage(randomSequence: List<Double>): Double =
        randomSequence.average()


    fun calculateHistograms(
        randomSequence: List<Double>,
        intervalsNumber: Int
    ): List<Pair<Double, Int>> {


        val min = randomSequence.min()!!
        val max = randomSequence.max()!!

        val intervals = Array(intervalsNumber) {0}

        println("Min: $min Max: $max")

        randomSequence.forEach {
            val index = getIntervalIndex(it, min, max, intervalsNumber)
            println("Element: $it\tindex: $index")
            intervals[index]++
        }

        val histograms = intervals.mapIndexed{ index, hits ->
            getIntervalMiddle(index, min, max, intervalsNumber) to hits
        }

        return histograms
    }

    fun getIntervalIndex(
        element: Double,
        min: Double,
        max: Double,
        intervalsNumber: Int
    ): Int {

        val step = (max - min) / intervalsNumber
        val intervalStart = element - element % step

        val intervalIndex = (intervalStart / step)

        return Math.min(intervalIndex.roundToInt(), intervalsNumber - 1)
    }

    private fun getIntervalMiddle(
        index: Int,
        min: Double,
        max: Double,
        intervalsNumber: Int
    ): Double {
        val step = getStep(min, max, intervalsNumber)
        return (index + 0.5) * step
    }

    private fun getStep(
        min: Double,
        max: Double,
        intervalsNumber: Int
    ) = (max - min) / intervalsNumber
}