package lab3.clinic

import lab2.simsimple.FunRand
import lab3.entity.Create
import lab3.entity.Process
import java.util.function.Function

fun main() {

    val delayGenerator: Function<Int, Double> = Function { FunRand.Exp(15.0) }
    val router: Function<MutableList<Process>, Process> = Function { it.first() }

//    val create = Create(
//        0.0,
//        delayGenerator,
//        mutableListOf<Process>(),
//        router
//    )
}