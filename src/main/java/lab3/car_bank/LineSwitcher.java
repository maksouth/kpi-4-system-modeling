package lab3.car_bank;

import lab3.entity.Entity;
import lab3.entity.kotlin.Operator;

import java.util.function.Consumer;

public class LineSwitcher implements Consumer<Double> {

    private final Operator firstProcess;
    private final Operator secondProcess;

    private double switchedCount;

    public LineSwitcher(Operator firstProcess, Operator secondProcess) {
        this.firstProcess = firstProcess;
        this.secondProcess = secondProcess;
    }

    @Override
    public void accept(Double modelTime) {
        compareAndSwitch(firstProcess, secondProcess, modelTime);
        compareAndSwitch(secondProcess, firstProcess, modelTime);
    }

    private void compareAndSwitch(Operator secondProcess, Operator firstProcess, double modelTime) {
        if (firstProcess.getQueueLength() - secondProcess.getQueueLength() >= 2) {
            Entity removed = firstProcess.removeLastFromQueue();
            secondProcess.accept(removed, modelTime);
            switchedCount++;
        }
    }

    public void printInfo() {
        System.out.println("Number of queue switches " + switchedCount);
    }
}
