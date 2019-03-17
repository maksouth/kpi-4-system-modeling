package lab3.entity;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Create extends DelayedTask {

    private final Function<List<Process>, Process> router;
    private final List<Process> consumers;
    private final Supplier<Integer> typeProducer;

    private int createdEvents;

    public Create(
            double initialDelay,
            Supplier<Double> delayGenerator,
            List<Process> consumers,
            Function<List<Process>, Process> router,
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
            Supplier<Double> delayGenerator,
            List<Process> consumers,
            Function<List<Process>, Process> router
    ) {
        this(initialDelay, delayGenerator, consumers, router, () -> 1);
    }

    public void processEvent() {
        Entity entity = new Entity();
        entity.setCreationTime(nextEventTime);
        entity.setType(typeProducer.get());
        router.apply(consumers).accept(entity, nextEventTime);

        createdEvents++;
        nextEventTime += delayGenerator.get();
    }

    @Override
    public void calculateStats(double delta) {}

    public void printInfo() {
        System.out.println("Created " + createdEvents + " events");
    }
}