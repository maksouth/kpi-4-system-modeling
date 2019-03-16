package entity;

public class Process extends Element {
    private double amountOfElementsToProcess;
    private double maxAmountOfElementsToProcess;
    private double amountOfDroppedEvents;
    private double meanQueue;

    public Process(double delay, double maxAmountOfElementsToProcess) {
        super(delay, "PROCESSOR");
        this.maxAmountOfElementsToProcess = maxAmountOfElementsToProcess;
        amountOfElementsToProcess = 0;
        meanQueue = 0.0;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("dropped = " + this.getAmountOfDroppedEvents());
    }

    @Override
    public void doStatistics(double delta) {
        super.doStatistics(delta);
        meanQueue += amountOfElementsToProcess * delta;
    }

    @Override
    public void startExecution() {
        if (state == State.FREE) {
            state = State.BUSY;
            nextEventTime = getCurrentModelTime() + getDelay();

        } else if (amountOfElementsToProcess < getMaxAmountOfElementsToProcess()) {
            amountOfElementsToProcess += 1;
        } else {
            amountOfDroppedEvents++;
        }
    }

    @Override
    public void finishExecution() {
        super.finishExecution();

        if (amountOfElementsToProcess > 0) {
            amountOfElementsToProcess -= 1;
            state = State.BUSY;
            nextEventTime = getCurrentModelTime() + getDelay();
        } else {
            state = State.FREE;
            nextEventTime = Double.MAX_VALUE;
        }

        startNextElement();
    }

    private void startNextElement() {
        Element nextElement = getNextElement();
        if (nextElement != null)
            nextElement.startExecution();
    }

    public double getAmountOfDroppedEvents() {
        return amountOfDroppedEvents;
    }

    private double getMaxAmountOfElementsToProcess() {
        return maxAmountOfElementsToProcess;
    }

    public double getMeanQueue() {
        return meanQueue;
    }
}