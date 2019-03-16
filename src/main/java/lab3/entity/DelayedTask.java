package lab3.entity;

import java.util.function.Supplier;

public abstract class DelayedTask {

    protected final Supplier<Double> delayGenerator;
    protected double nextEventTime;

    DelayedTask(Supplier<Double> delayGenerator) {
        this.delayGenerator = delayGenerator;
    }

    public abstract void processEvent();

    public double getFinishEventProcessingTime() {
        return nextEventTime;
    }

    public abstract void calculateStats(double delta);
}
