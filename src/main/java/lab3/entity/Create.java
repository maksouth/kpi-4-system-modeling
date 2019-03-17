package lab3.entity;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Create extends DelayedTask {

    private final Function<List<Process>, Process> router;
    private final List<Process> consumers;
    private int createdEvents;

    public Create(
            double initialDelay,
            Supplier<Double> delayGenerator,
            List<Process> consumers,
            Function<List<Process>, Process> router
    ) {
        super(delayGenerator);
        this.nextEventTime = initialDelay;
        this.router = router;
        this.consumers = consumers;
    }

    public void processEvent() {
        router.apply(consumers).accept(new Object(), nextEventTime);
        createdEvents++;
        nextEventTime += delayGenerator.get();
    }

    @Override
    public void calculateStats(double delta) {}

    public void printInfo() {
        System.out.println("Created " + createdEvents + " events");
    }
}