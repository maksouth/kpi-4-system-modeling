package lab3.clinic

import lab2.simsimple.FunRand
import lab3.entity.Dispose
import lab3.entity.Entity
import lab3.entity.Model
import lab3.entity.kotlin.Create
import lab3.entity.kotlin.Operator
import java.util.function.BiConsumer
import kotlin.random.Random

fun main() {

    val exit = Dispose()
    val changeType = changeType()
    val laboratory = laboratory(exit, changeType)
    val laboratoryRegistry = laboratoryRegistry(laboratory)
    val wayToLaboratory = wayToLaboratory(laboratoryRegistry)
    val wayToPalata = wayToPalata(exit)
    val waitingRoom = waitingRoom(wayToPalata, wayToLaboratory)
    val generator = generator(waitingRoom)

    val wayToWaitingRoom = wayToWaitingRoom(waitingRoom)
    changeType.next = wayToWaitingRoom

    val tasks = listOf(
        generator,
        waitingRoom,
        wayToPalata,
        wayToLaboratory,
        laboratory,
        laboratoryRegistry,
        wayToWaitingRoom
    )

    val model = Model(tasks)

    val simulationTime = 10000.0

    model.simulate(simulationTime)

    generator.printInfo()
    exit.printInfo()
}

fun changeType() = ChangeType()

fun laboratory(exit: Dispose, changeType: BiConsumer<Entity, Double>): Operator {

    val router: (List<BiConsumer<Entity, Double>>, Int) -> BiConsumer<Entity, Double> = { next, type ->
        when(type) {
            2 -> changeType
            else -> exit
        }
    }

    return Operator(
        randomFunction = { FunRand.Erlang(4.0, 2.0) },
        next = listOf(changeType, exit),
        router = router
    )
}

fun laboratoryRegistry(laboratory: Operator) = Operator(
    randomFunction = { FunRand.Erlang(4.5, 3.0) },
    next = listOf(laboratory)
)

fun wayToLaboratory(laboratoryRegistry: Operator) = Operator(
    randomFunction = { FunRand.Unif(2.0, 5.0) },
    workers = Int.MAX_VALUE,
    next = listOf(laboratoryRegistry)
)

fun wayToPalata(palata: Dispose) = Operator(
    randomFunction = { FunRand.Unif(3.0, 8.0) },
    workers = 3,
    next = listOf(palata)
)

fun waitingRoom(palata: Operator, wayToLaboratory: Operator): Operator {
    val randomProcessing: (Int) -> Double = { type ->
        val delay = when(type) {
            1 -> 15.0
            2 -> 40.0
            else -> 30.0
        }

        FunRand.Exp(delay)
    }

    val router: (List<BiConsumer<Entity, Double>>, Int) -> BiConsumer<Entity, Double> = { nextRooms, type ->
        when(type) {
            1 -> nextRooms[0]
            else -> nextRooms[1]
        }
    }

    //todo order is important
    val next = listOf(palata, wayToLaboratory)

    val entityComparator = Comparator<Entity> { o1, o2 -> o2.type - o1.type }

    return Operator(
        randomFunction = randomProcessing,
        entityComparator = entityComparator,
        router = router,
        next = next,
        workers = 2
    )
}

fun wayToWaitingRoom(waitingRoom: Operator) = Operator(
    randomFunction = { FunRand.Unif(2.0, 5.0) },
    workers = Int.MAX_VALUE,
    next = listOf(waitingRoom)
)

fun generator(waitingRoom: Operator): Create {
    val typeProducer = {
        val rand = Random.nextDouble()
        when {
            rand < 0.1 -> 2
            rand < 0.5 -> 3
            else -> 1
        }
    }

    val router = { processes: List<Operator> -> processes.first() }

    val generator = Create(
        delayGenerator = { FunRand.Exp(15.0) },
        router = router,
        typeProducer = typeProducer,
        consumers = listOf(waitingRoom)
    )

    return generator
}