package lab3.entity;

import java.util.function.Function;

public abstract class DelayedTask {

    protected final Function<Integer, Double> delayGenerator;
    protected double nextEventTime;

    DelayedTask(Function<Integer, Double> delayGenerator) {
        this.delayGenerator = delayGenerator;
    }

    public abstract void processEvent();

    public double getFinishEventProcessingTime() {
        return nextEventTime;
    }

    public abstract void calculateStats(double delta);
}
