package lab3.entity;

import lab3.entity.kotlin.Operator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Create extends DelayedTask {

    private final Function<List<Operator>, Operator> router;
    private final List<Operator> consumers;
    private final Supplier<Integer> typeProducer;

    private int createdEvents;
    protected double nextEventTime;

    public Create(
            double initialDelay,
            Function<Integer, Double> delayGenerator,
            List<Operator> consumers,
            Function<List<Operator>, Operator> router,
            Supplier<Integer> typeProducer
    ) {
        super(delayGenerator);
        this.nextEventTime = initialDelay;
        this.router = router;
        this.consumers = consumers;
        this.typeProducer = typeProducer;
    }

    public Create(
            double initialDelay,
            Function<Integer, Double> delayGenerator,
            List<Operator> consumers,
            Function<List<Operator>, Operator> router
    ) {
        this(initialDelay, delayGenerator, consumers, router, () -> 1);
    }

    public double getFinishEventProcessingTime() {
        return nextEventTime;
    }

    public void processEvent(double currentTime) {
        Entity entity = new Entity();
        entity.setCreationTime(nextEventTime);
        entity.setType(typeProducer.get());
        router.apply(consumers).accept(entity, nextEventTime);

        createdEvents++;
        nextEventTime += delayGenerator.apply(0);
    }

    @Override
    public void calculateStats(double delta) {}

    public void printInfo() {
        System.out.println("Created " + createdEvents + " events");
    }
}