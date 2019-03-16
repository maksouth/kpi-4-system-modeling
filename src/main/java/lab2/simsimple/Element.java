package lab2.simsimple;

import java.util.Comparator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public abstract class Element {

    public enum State {
        BUSY(1), FREE(0);

        private final int value;

        State(int value) {
            this.value = value;
        }
    }

    private static int uniqueId = 0;

    protected State state;
    protected double nextEventTime;

    private final String name;
    private final double delayMean;
    private final Random random = new Random();
    private final TreeMap<Double, Element> probabilityToNextElement;
    private String distribution;
    private double processedEventsCount;
    private double currentModelTime;
    private double loadedTime = 0;

    Element(double delay, String name){
        nextEventTime = 0.0;
        delayMean = delay;
        distribution = "";
        state = State.FREE;
        probabilityToNextElement = new TreeMap<>(Comparator.reverseOrder());

        int id = uniqueId++;
        this.name = name + "-" + id;
    }

    double getDelay() {
        double delay = delayMean;
        double delayDev = 0.0;
        if ("exp".equalsIgnoreCase(distribution)) {
            delay = FunRand.Exp(delayMean);
        } else if ("norm".equalsIgnoreCase(distribution)) {
            delay = FunRand.Norm(delayMean, delayDev);
        } else if ("unif".equalsIgnoreCase(distribution)) {
            delay = FunRand.Unif(delayMean, delayDev);
        }

        return delay;
    }

    void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    double getProcessedEventsCount() {
        return processedEventsCount;
    }

    double getCurrentModelTime() {
        return currentModelTime;
    }

    void setCurrentModelTime(double currentModelTime) {
        this.currentModelTime = currentModelTime;
    }

    Element getNextElement() {
        double probability = random.nextDouble();

        if (probabilityToNextElement.isEmpty())
            return null;

        Map.Entry<Double, Element> next = probabilityToNextElement.floorEntry(probability);
        if (next == null)
            next = probabilityToNextElement.firstEntry();

        return next.getValue();
    }

    void addNextElement(Element nextElement, double probability) {
        double currentStackProbability = probabilityToNextElement.isEmpty() ? 0.0 : probabilityToNextElement.firstKey();
        double stackProbability = currentStackProbability + probability;
        probabilityToNextElement.put(stackProbability, nextElement);
    }

    public void startExecution() {}

    public void finishExecution() {
        processedEventsCount++;
    }

    public double getNextEventTime() {
        return nextEventTime;
    }
    
    public void printResult(double totalTime){
        System.out.println(getName()+ " processedEventsCount = "+ processedEventsCount + " average load = " + loadPercentage(totalTime));
    }
    
    public void printInfo(){
        System.out.println(getName()+ " " +state+
                " processed = "+ processedEventsCount +
                " next event time = "+ nextEventTime);
    }
    
    public String getName() {
        return name;
    }
    
    public void doStatistics(double delta){
        loadedTime += state.value * delta;
    }

    private double loadPercentage(double totalTime) {
        return loadedTime / totalTime;
    }
}