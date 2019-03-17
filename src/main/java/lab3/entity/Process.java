package lab3.entity;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class Process extends DelayedTask implements BiConsumer<Entity, Double> {

    private static final int BUSY = 1;
    private static final int FREE = 0;

    private final int maxQueueLength;
    private final int priority;
//    private final int workers;

    private int state = FREE;
    protected double nextEventTime;

    private PriorityQueue<Entity> clientsQueue;
    private int droppedEvents;
    private int processedEvents;

    private BiConsumer<Entity, Double> next;
    private Consumer<Double> listener;

    private double meanTime;
    private double meanQueueLength;

    public Process(
            int maxQueueLength,
            Function<Integer, Double> delayGenerator,
            BiConsumer<Entity, Double> next,
            int priority
    ) {
        this(maxQueueLength,
                delayGenerator,
                next,
                (ignored) -> {
                },
                priority,
                Comparator.comparingDouble(Entity::getCreationTime)
        );
    }

    public Process(
            int maxQueueLength,
            Function<Integer, Double> delayGenerator,
            BiConsumer<Entity, Double> next,
            int priority,
            Comparator<Entity> comparator,
            int workers
    ) {
        this(maxQueueLength,
                delayGenerator,
                next,
                (ignored) -> {},
                priority,
                comparator
        );
    }

    public Process(
            int maxQueueLength,
            Function<Integer, Double> delayGenerator,
            BiConsumer<Entity, Double> next,
            Consumer<Double> listener,
            int priority,
            Comparator<Entity> comparator
    ) {
        super(delayGenerator);
        this.next = next;
        this.listener = listener;
        this.maxQueueLength = maxQueueLength;
        this.priority = priority;
        clientsQueue = new PriorityQueue<>(comparator);
        initialLaunch();
    }

    public double getFinishEventProcessingTime() {
        return nextEventTime;
    }

    private void initialLaunch() {
        Entity entity = new Entity();
        entity.setCreationTime(0.0);
        accept(entity, 0.0);

        entity = new Entity();
        entity.setCreationTime(0.0);
        accept(entity, 0.0);

        scheduleProcessing(entity, 0);
    }

    public void accept(Entity client, Double modelTime) {
        if (clientsQueue.size() < maxQueueLength)
            clientsQueue.add(client);
        else droppedEvents++;

        scheduleProcessing(client, modelTime);
    }

    private void scheduleProcessing(Entity entity, double modelTime) {
        if (clientsQueue.size() > 0 && state == FREE) {
            state = BUSY;
            calculateProcessingTime(entity, modelTime);
        } else {
            state = FREE;
            nextEventTime = Double.MAX_VALUE;
        }
    }

    public void calculateProcessingTime(Entity entity, double modelTime) {
        double processingTime = delayGenerator.apply(entity.getType());
        nextEventTime = modelTime + processingTime;
    }

    public void processEvent(double currentTime) {
        Entity processed = clientsQueue.remove();
        processedEvents++;
        next.accept(processed, nextEventTime);
        listener.accept(nextEventTime);

        scheduleProcessing(clientsQueue.peek(), nextEventTime);
    }

    @Override
    public void calculateStats(double delta) {
        meanTime += state * delta;
        meanQueueLength += state * clientsQueue.size();
    }

    public int getQueueLength() {
        return clientsQueue.size();
    }

    public Entity removeLastFromQueue() {
        Entity removed = clientsQueue.stream()
                .max(Comparator.comparingDouble(Entity::getCreationTime))
                .get();
        clientsQueue.remove(removed);
        return removed;
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
