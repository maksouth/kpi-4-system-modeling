package lab4

import lab2.simsimple.FunRand
import lab3.entity.DelayedTask
import lab3.entity.Dispose
import lab3.entity.Entity
import lab3.entity.Model
import lab3.entity.kotlin.Create
import lab3.entity.kotlin.Operator
import java.util.function.BiConsumer
import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun main() {
    val elementsNum = 100
    val eventsNum = elementsNum + 1
    val delay = 10.0

    val firstStructure = firstStructure(elementsNum, eventsNum, delay)
    val firstTime = measureTime(firstStructure)

    val secondStructure = secondStructure(elementsNum, eventsNum, delay)
    val secondTime = measureTime(secondStructure)

    println("First Structure Execution time $firstTime")
    println("Second Structure Execution time $secondTime")
}

fun measureTime(list: List<DelayedTask>): Long = measureTimeMillis {
    val model = Model(list)
    model.simulate(Double.MAX_VALUE)
}


fun firstStructure(elementsNum: Int, eventsNum: Int, delay: Double): List<DelayedTask> {
    val dispose = Dispose()
    val tasks = mutableListOf<DelayedTask>()

    var operator = createOperator(dispose, delay)
    tasks += operator
    repeat(elementsNum - 1) {
        operator = createOperator(operator, delay)
        tasks += operator
    }

    val generator = Create(
        delayGenerator = { FunRand.Exp(delay) },
        consumers = listOf(operator),
        maxElementsNum = eventsNum
    )
    tasks += generator

    return tasks
}

fun secondStructure(elementsNum: Int, eventsNum: Int, delay: Double): List<DelayedTask> {
    val dispose = Dispose()

    val tasks = mutableListOf<DelayedTask>()
    val consumers = mutableListOf<Operator>()

    repeat(elementsNum) {
        val operator = createOperator(dispose, delay)
        tasks += operator
        consumers += operator
    }

    val router: (List<Operator>) -> Operator = {
        val random = Random.nextDouble()
        val step = 1.0 / consumers.size
        var counter = 1

        while (step * counter < random)
            counter++

        consumers[counter-1]
    }

    val generator = Create(
        delayGenerator = { FunRand.Exp(delay) },
        consumers = consumers,
        maxElementsNum = eventsNum,
        router = router
    )

    tasks += generator

    return tasks
}

fun createOperator(next: BiConsumer<Entity, Double>, delay: Double) =
        Operator(
            randomFunction = { FunRand.Exp(delay) },
            next = listOf(next)
        )