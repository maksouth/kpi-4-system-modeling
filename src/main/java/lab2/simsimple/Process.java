package lab2.simsimple;

public class Process extends Element {
    private int queue, maxqueue, failure;
    private double meanQueue;

    Process(double delay) {
        super(delay, "PROCESSOR");
        queue = 0;
        maxqueue = Integer.MAX_VALUE;
        meanQueue = 0.0;

    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("failure = " + this.getFailure());
    }

    @Override
    public void doStatistics(double delta) {
        super.doStatistics(delta);
        meanQueue = getMeanQueue() + queue * delta;
    }

    @Override
    public void inAct() {
        if (getState() == 0) {
            setState(1);
            setTnext(getTcurr() + getDelay());
        } else {
            if (getQueue() < getMaxqueue()) {
                setQueue(getQueue() + 1);
            } else {
                failure++;
            }
        }
    }

    @Override
    public void outAct() {
        super.outAct();
        setTnext(Double.MAX_VALUE);
        setState(0);
        if (getQueue() > 0) {
            setQueue(getQueue() - 1);
            setState(1);
            setTnext(getTcurr() + getDelay());
            if (getNextElement() != null)
                getNextElement().inAct();
        }
    }

    public int getFailure() {
        return failure;
    }

    private int getQueue() {
        return queue;
    }

    private void setQueue(int queue) {
        this.queue = queue;
    }

    private int getMaxqueue() {
        return maxqueue;
    }

    public void setMaxqueue(int maxqueue) {
        this.maxqueue = maxqueue;
    }

    public double getMeanQueue() {
        return meanQueue;
    }
}