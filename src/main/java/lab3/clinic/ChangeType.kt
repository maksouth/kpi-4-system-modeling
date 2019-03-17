package lab3.clinic

import lab3.entity.Entity
import java.util.function.BiConsumer

class ChangeType: BiConsumer<Entity, Double> {

    lateinit var next: BiConsumer<Entity, Double>

    override fun accept(t: Entity, u: Double) {
        t.type = 1
        next.accept(t, u)
    }

}