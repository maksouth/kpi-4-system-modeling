package lab3.entity.kotlin

import lab3.entity.DelayedTask
import lab3.entity.Entity

class Create(
    delayGenerator: (Int) -> Double,
    private val consumers: List<Operator>,
    private val router: (List<Operator>) -> Operator = { it.first() },
    private val typeProducer: () -> Int = {1},
    private val maxElementsNum: Int = Int.MAX_VALUE,
    initialDelay: Double = 0.0
): DelayedTask(delayGenerator) {

    private var nextEventTime = initialDelay
    private var createdEvents: Int = 0

    override fun processEvent(currentTime: Double) {
        val type = typeProducer()
        val entity = Entity(type, nextEventTime)
        router(consumers).accept(entity, nextEventTime)

        createdEvents++
        if (createdEvents < maxElementsNum)
            nextEventTime += delayGenerator.apply(0)
        else nextEventTime = Double.MAX_VALUE
    }

    override fun getFinishEventProcessingTime() = nextEventTime

    override fun calculateStats(delta: Double) {}

    fun printInfo() = println("Created $createdEvents events")
}
