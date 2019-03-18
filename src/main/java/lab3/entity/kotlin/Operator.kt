package lab3.entity.kotlin

import lab3.entity.DelayedTask
import lab3.entity.Entity
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Function

const val HIGH_PRIORITY = 5
const val LOW_PRIORITY = 1

var idCounter = 1

class Operator(
    private val next: List<BiConsumer<Entity, Double>>,
    randomFunction: (Int) -> Double,
    initialEntities: List<Entity> = emptyList(),
    private var listener: Consumer<Double> = Consumer {},
    entityComparator: Comparator<Entity> = Comparator.comparingDouble { it.creationTime },
    private val maxQueueSize: Int = Int.MAX_VALUE,
    private val priority: Int = HIGH_PRIORITY,
    private val workers: Int = 1,
    private val router: (List<BiConsumer<Entity, Double>>, Int) -> BiConsumer<Entity, Double> = { list, _ -> list.first() },
    private val name: String = "Operator ${idCounter++}"
): DelayedTask(randomFunction), BiConsumer<Entity, Double> {

    private val workerThreads = PriorityQueue<WorkerThread>(kotlin.Comparator { o1, o2 -> o1.eventTime.compareTo(o2.eventTime) })
    private val clientsQueue = PriorityQueue<Entity>(entityComparator)

    private var meanTime: Double = 0.0
    private var meanQueueLength: Double = 0.0

    var processedEvents: Int = 0
    var droppedEvents: Int = 0

    val nextEventTime: Double
        get() = workerThreads.peek()?.eventTime ?: Double.MAX_VALUE

    init {
        initialEntities.forEach {
            clientsQueue.add(it)
            scheduleProcessing(0.0)
        }
    }

    override fun processEvent(currentTime: Double) {
        if(nextEventTime == currentTime && workerThreads.size > 0) {
            val processed = workerThreads.remove()
            processedEvents++

            router(next, processed.entity.type).accept(processed.entity, currentTime)
            listener.accept(currentTime)

            scheduleProcessing(currentTime)
        }
    }

    override fun calculateStats(delta: Double) {
        meanTime += workerThreads.size * delta
        meanQueueLength += clientsQueue.size.toDouble()
    }

    override fun getFinishEventProcessingTime(): Double =
        nextEventTime

    override fun accept(entity: Entity, time: Double) {
        if(clientsQueue.size < maxQueueSize) {
            clientsQueue.add(entity)
            scheduleProcessing(time)
        } else droppedEvents++
    }

    private fun scheduleProcessing(modelTime: Double) {
        if(workerThreads.size < workers && clientsQueue.size > 0) {
            val entity = clientsQueue.remove()
            val completionTime = calculateCompletionTime(entity, modelTime)
            workerThreads.add(WorkerThread(entity, completionTime))
        }
    }

    fun calculateCompletionTime(entity: Entity, modelTime: Double): Double {
        val processingTime = delayGenerator.apply(entity.type)
        return modelTime + processingTime
    }

    fun getPriority(): Int = priority

    fun getQueueLength() = clientsQueue.size

    fun setListener(listener: Consumer<Double>) {
        this.listener = listener
    }

    fun printInfo(currentTime:Double) = println(
        "Name $name Mean time " + meanTime / currentTime +
        " mean queue length " + meanQueueLength / currentTime +
        " processed events " + processedEvents +
        " failure probability " + droppedEvents.toDouble() / processedEvents
    )

    fun removeLastFromQueue(): Entity {
        val removed = clientsQueue.maxBy { it.creationTime }!!
        clientsQueue.remove(removed)
        return removed
    }

    data class WorkerThread(
        val entity: Entity,
        val eventTime: Double
    )
}