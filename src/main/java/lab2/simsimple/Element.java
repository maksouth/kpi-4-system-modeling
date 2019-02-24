package lab2.simsimple;

import java.util.Comparator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Element {

    private static int uniqueId = 0;

    private final String name;
    private final double delayMean;
    private final Random random = new Random();
    private final TreeMap<Double, Element> probabilityToNextElement;
    private double tnext;
    private String distribution;
    private int quantity;
    private double tcurr;
    private int state;
    private int id;
    private int loadCounter = 0;
    private int totalEventCounter;

    Element(double delay, String name){
        tnext = 0.0;
        delayMean = delay;
        distribution = "";
        tcurr = tnext;
        state=0;
        probabilityToNextElement = new TreeMap<>(Comparator.reverseOrder());

        id = uniqueId++;
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

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public int getQuantity() {
        return quantity;
    }

    double getTcurr() {
        return tcurr;
    }

    public void setTcurr(double tcurr) {
        this.tcurr = tcurr;
    }

    int getState() {
        return state;
    }

    void setState(int state) {
        this.state = state;
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

    public void addNextElement(Element nextElement, double probability) {
        double currentStackProbability = probabilityToNextElement.isEmpty() ? 0.0 : probabilityToNextElement.firstKey();
        double stackProbability = currentStackProbability + probability;
        probabilityToNextElement.put(stackProbability, nextElement);
    }

    public void inAct() {}

    public void outAct() {
        quantity++;
    }

    public double getTnext() {
        return tnext;
    }

    void setTnext(double tnext) {
        this.tnext = tnext;
    }
    
    public int getId() {
        return id;
    }
    
    public void printResult(){
        System.out.println(getName()+ " quantity = "+ quantity + " average load = " + averageLoad());
    }
    
    public void printInfo(){
        System.out.println(getName()+ " state= " +state+
                " quantity = "+ quantity +
                " tnext= "+tnext);
    }
    
    public String getName() {
        return name;
    }
    
    public void doStatistics(double delta){
        loadCounter += state;
        totalEventCounter++;
    }

    private double averageLoad() {
        return (double) loadCounter / totalEventCounter;
    }
}