package lab3.car_bank

import lab2.simsimple.FunRand
import lab3.entity.Create
import lab3.entity.Dispose
import lab3.entity.Entity
import lab3.entity.Model
import lab3.entity.kotlin.HIGH_PRIORITY
import lab3.entity.kotlin.LOW_PRIORITY
import lab3.entity.kotlin.Process
import java.util.function.Function

lateinit var generator: Create
lateinit var firstDispose: Dispose
lateinit var secondDispose: Dispose
lateinit var first: Process
lateinit var second: Process
lateinit var lineSwitcher: LineSwitcher

fun main() {
    firstDispose = Dispose()
    secondDispose = Dispose()

    first = Process(
        next = firstDispose,
        randomFunction = Function { FunRand.Exp(0.3) },
        maxQueueSize = 4,
        priority = HIGH_PRIORITY,
        initialEntities = List(2) { Entity(1, 0.0) }
    )

    second = Process(
        next = secondDispose,
        randomFunction = Function { FunRand.Exp(0.3) },
        maxQueueSize = 4,
        priority = LOW_PRIORITY,
        initialEntities = List(2) { Entity(1, 0.0) }
    )

    val router = Function { processes: List<Process> ->
        val min = processes.map { it.getQueueLength() }.min()!!

        val chosen = processes.filter { it.getQueueLength() == min }
            .maxBy { it.getPriority() }!!
        chosen
    }

    lineSwitcher = LineSwitcher(first, second)

    first.setListener(lineSwitcher)
    second.setListener(lineSwitcher)

    generator = Create(
        0.1,
        Function{ FunRand.Exp(0.5) },
        listOf(first, second),
        router
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
