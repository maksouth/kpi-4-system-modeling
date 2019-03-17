package lab3.car_bank

import lab2.simsimple.FunRand
import lab3.entity.kotlin.Create
import lab3.entity.Dispose
import lab3.entity.Entity
import lab3.entity.Model
import lab3.entity.kotlin.HIGH_PRIORITY
import lab3.entity.kotlin.LOW_PRIORITY
import lab3.entity.kotlin.Operator
import java.util.function.Function

lateinit var generator: Create
lateinit var firstDispose: Dispose
lateinit var secondDispose: Dispose
lateinit var first: Operator
lateinit var second: Operator
lateinit var lineSwitcher: LineSwitcher

fun main() {
    firstDispose = Dispose()
    secondDispose = Dispose()

    first = Operator(
        next = listOf(firstDispose),
        randomFunction = { FunRand.Exp(0.3) },
        maxQueueSize = 4,
        priority = HIGH_PRIORITY,
        initialEntities = List(2) { Entity(1, 0.0) }
    )

    second = Operator(
        next = listOf(secondDispose),
        randomFunction = { FunRand.Exp(0.3) },
        maxQueueSize = 4,
        priority = LOW_PRIORITY,
        initialEntities = List(2) { Entity(1, 0.0) }
    )

    val router = { processes: List<Operator> ->
        val min = processes.map { it.getQueueLength() }.min()!!

        val chosen = processes.filter { it.getQueueLength() == min }
            .maxBy { it.getPriority() }!!
        chosen
    }

    lineSwitcher = LineSwitcher(first, second)

    first.setListener(lineSwitcher)
    second.setListener(lineSwitcher)

    generator = Create(
        initialDelay = 0.1,
        delayGenerator = { FunRand.Exp(0.5) },
        consumers = listOf(first, second),
        router = router
    )

    val tasks = listOf(generator, first, second)
    val model = Model(tasks)

    model.simulate(10000.0)
    printInfo(10000.0)
}

private fun printInfo(currentTime: Double) {
    generator.printInfo()
    lineSwitcher.printInfo()

    first.printInfo(currentTime)
    firstDispose.printInfo()

    second.printInfo(currentTime)
    secondDispose.printInfo()
}
