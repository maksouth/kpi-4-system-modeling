package lab3.entity;

import java.util.function.Function;

public abstract class DelayedTask {

    protected final Function<Integer, Double> delayGenerator;

    public DelayedTask(Function<Integer, Double> delayGenerator) {
        this.delayGenerator = delayGenerator;
    }

    public abstract void processEvent(double currentTime);

    public abstract double getFinishEventProcessingTime();

    public abstract void calculateStats(double delta);
}
