package lab3.entity;

import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Process extends DelayedTask implements BiConsumer<Object, Double> {

    private static final int BUSY = 1;
    private static final int FREE = 0;

    private final int maxQueueLength;
    private final int priority;

    private int state = FREE;

    private LinkedList<Object> clientsQueue = new LinkedList<>();
    private int droppedEvents;
    private int processedEvents;

    protected double totalProcessTime;
    private BiConsumer<Object, Double> next;
    private Consumer<Double> listener;

    private double meanTime;
    private double meanQueueLength;

    public Process(
            int maxQueueLength,
            Supplier<Double> delayGenerator,
            BiConsumer<Object, Double> next,
            int priority
    ) {
        this(maxQueueLength, delayGenerator, next, (ignored) -> {}, priority);
    }

    public Process(
            int maxQueueLength,
            Supplier<Double> delayGenerator,
            BiConsumer<Object, Double> next,
            Consumer<Double> listener,
            int priority
    ) {
        super(delayGenerator);
        this.next = next;
        this.listener = listener;
        this.maxQueueLength = maxQueueLength;
        this.priority = priority;

        accept(new Object(), 0.0);
        accept(new Object(), 0.0);
        scheduleProcessing(0);
    }

    public void accept(Object client, Double modelTime) {
        if (clientsQueue.size() < maxQueueLength)
            clientsQueue.addLast(client);
        else droppedEvents++;

        scheduleProcessing(modelTime);
    }

    private void scheduleProcessing(double modelTime) {
        if (clientsQueue.size() > 0 && state == FREE) {
            state = BUSY;
            calculateProcessingTime(modelTime);
        } else {
            state = FREE;
            nextEventTime = Double.MAX_VALUE;
        }
    }

    public void calculateProcessingTime(double modelTime) {
        double processingTime = delayGenerator.get();

        nextEventTime = modelTime + processingTime;
    }

    public void processEvent() {
        Object processed = clientsQueue.removeFirst();
        processedEvents++;
        next.accept(processed, nextEventTime);
        listener.accept(nextEventTime);

        scheduleProcessing(nextEventTime);
    }

    @Override
    public void calculateStats(double delta) {
        meanTime += state * delta;
        meanQueueLength += state * clientsQueue.size();
    }

    public double getMeanTime() {
        return meanTime;
    }

    public double getMeanQueueLength() {
        return meanQueueLength;
    }

    public int getQueueLength() {
        return clientsQueue.size();
    }

    public Object removeLastFromQueue() {
        return clientsQueue.removeLast();
    }

    public int getPriority() {
        return priority;
    }

    public void setListener(Consumer<Double> listener) {
        this.listener = listener;
    }

    public void printInfo(double currentTime) {
        System.out.println("Mean time " + meanTime / currentTime +
                " mean queue length " + meanQueueLength / currentTime +
                " processed events " + processedEvents +
                " failure probability " + (double) droppedEvents / processedEvents);

    }
}
