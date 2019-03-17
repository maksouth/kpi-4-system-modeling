package lab3.car_bank;

import lab3.entity.Process;

import java.util.function.Consumer;

public class LineSwitcher implements Consumer<Double> {

    private final Process firstProcess;
    private final Process secondProcess;

    private double switchedCount;

    public LineSwitcher(Process firstProcess, Process secondProcess) {
        this.firstProcess = firstProcess;
        this.secondProcess = secondProcess;
    }

    @Override
    public void accept(Double modelTime) {
        compareAndSwitch(firstProcess, secondProcess, modelTime);
        compareAndSwitch(secondProcess, firstProcess, modelTime);
    }

    private void compareAndSwitch(Process secondProcess, Process firstProcess, double modelTime) {
        if (firstProcess.getQueueLength() - secondProcess.getQueueLength() >= 2) {
            Object removed = firstProcess.removeLastFromQueue();
            secondProcess.accept(removed, modelTime);
            switchedCount++;
        }
    }

    public void printInfo() {
        System.out.println("Number of queue switches " + switchedCount);
    }
}
