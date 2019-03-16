package lab3.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Process extends Element {

    public int workers;

    public List<Patient> queue;
    public int maxQueue;
    public int failure;
    public double meanQueue;
    public double meanLoad;

    private List<PatientThread> threads = new ArrayList<>();

    public Process(int workers, double meanDelay, double delayDeviation)
    {
        super(meanDelay, delayDeviation, Distribution.Exponential);
        priority = 0;
        this.workers = workers;
        threads = new ArrayList<>(workers);

        for (int i = 0; i < workers; i++)
            threads.add(new PatientThread());

        queue = new ArrayList<>();
        maxQueue = Integer.MAX_VALUE;
        meanQueue = 0;
    }

    public Process(
            int maxQueue,
            int workers,
            double meanDelay,
            double delayDeviation
    ) {
        super(meanDelay,
                delayDeviation, Distribution.Exponential);
        priority = 0;
        this.workers = workers;
        threads = new ArrayList<>(workers);

        for (int i = 0; i < workers; i++)
            threads.add(new PatientThread());

        queue = new ArrayList<>();
        this.maxQueue = maxQueue;
        meanQueue = 0;
    }


    public Process() {
        this.workers = 1;
        threads = new ArrayList<>(1);

        threads.add(new PatientThread());

        queue = new ArrayList<>();
        maxQueue = Integer.MAX_VALUE;
        meanQueue = 0;
    }

    @Override
    public double getNextTime() {
        double nextTime = Double.MAX_VALUE;
        if (threads != null && threads.size() != 0)
            nextTime = threads.stream()
                    .min(Comparator.comparingDouble(o -> o.nextTime))
                    .get()
                    .nextTime;
        return nextTime;
    }

    private Patient Get(List<Patient> objects)
    {
        Patient result = objects.get(0);
        objects.remove(0);
        return result;
    }

    @Override
    public void inputAction(Patient myObject)
    {
        if (threads.stream().anyMatch(item -> item.state == 0)) {
            PatientThread thread = threads.stream().filter(item -> item.state == 0).findAny().get();
            thread.state = 1;
            thread.nextTime = currentTime + getStandartDelay(myObject);
            thread.patient = myObject;
        } else if (queue.size() < maxQueue)
            queue.add(myObject);
        else {
            quantity++;
            failure++;
        }
    }

    @Override
    public void outputAction()
    {
        super.outputAction();

        PatientThread min = threads.stream()
                .min(Comparator.comparingDouble(item -> item.nextTime))
                .get();
        min.state = 0;
        min.nextTime = Double.MAX_VALUE;
        Patient minPatient = min.patient;
        if (queue.size() > 0) {
            min.patient = Get(queue);
            min.state = 1;
            min.nextTime = currentTime + getStandartDelay(min.patient);
        }

        nextElement.inputAction(minPatient);
    }

    @Override
    public void printInfo()
    {
        super.printInfo();
        System.out.println("failure = " + failure + "\nMean Load = " + meanLoad);
    }

    @Override
    public void printResult() {
        super.printResult();
        System.out.println("\tMean length of queue = " + GetMeanQueueLength()
                + "\n\tfailure probability = " + (failure / (double)quantity) +
                "\n\tMean load = " + GetMeanLoad());

    }

    public double GetMeanQueueLength() {
        return (meanQueue / currentTime);
    }

    public double GetMeanLoad()
    {
        return (meanLoad / currentTime);
    }

    @Override
    public void doStatistics(double delta) {
        meanQueue += queue.size() * delta;
        for (PatientThread state : threads)
            meanLoad += state.state * delta;
    }

    @Override
    public boolean isQueueNotFull() {
        return queue.size() < maxQueue;
    }

    public double getStandartDelay(Patient myObject) {
        return getDelay();
    }
}
