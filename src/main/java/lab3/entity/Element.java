package lab3.entity;

public class Element {
    public static int NEXT_ID = 0;

    public String name;
    public double nextTime;
    public double meanDelay;
    public double delayDeviation;
    public int k;
    public Distribution distribution;
    public int quantity;
    public int priority;
    public double currentTime;
    public int state;
    public Element nextElement;
    public int id;
    public Runnable action;

    public Element()
    {
        action = () -> {};
        nextTime = 0.0;
        priority = 0;
        meanDelay = 1.0;
        delayDeviation = 1.0;
        distribution = Distribution.Exponential;
        currentTime = nextTime;
        state = 0;
        nextElement = null;
        id = NEXT_ID;
        NEXT_ID++;
        name = "element " + id;
    }

    public Element(double meanDelay, double delayDeviation, Distribution distribution)
    {
        action = () -> {};
        priority = 0;
        nextTime = 0.0;
        this.meanDelay = meanDelay;
        this.delayDeviation = delayDeviation;
        this.distribution = distribution;
        currentTime = nextTime;
        state = 0;
        nextElement = null;
        id = NEXT_ID;
        NEXT_ID++;
        name = "element " + id;
    }

    public Element(String name, double meanDelay, double delayDeviation, Distribution distribution)
    {
        action = () -> {};
        priority = 0;
        nextTime = 0.0;
        this.meanDelay = meanDelay;
        this.delayDeviation = delayDeviation;
        this.distribution = distribution;
        currentTime = nextTime;
        state = 0;
        nextElement = null;
        id = NEXT_ID;
        NEXT_ID++;
        this.name = name;
    }

    public double getDelay()
    {
//        double delay = meanDelay;
//
//        switch (distribution)
//        {
//            case Exponential:
//            {
//                delay = RndLib.GetNextExponential(meanDelay);
//                break;
//            }
//            case Uniform:
//            {
//                delay = RndLib.GetNext(meanDelay - delayDeviation, meanDelay + delayDeviation);
//                break;
//            }
//            case Normal:
//            {
//                do
//                {
//                    delay = RndLib.GetNextNormal(meanDelay, delayDeviation);
//                } while (delay < 0);
//
//                break;
//            }
//            case Erlang:
//            {
//                delay = RndLib.GetNextErlang(k, meanDelay);
//                break; ;
//            }
//            case None:
//            {
//                break;
//            }
//            default:
//            {
//                break;
//            }
//        }

        return 0;
    }

    public void inputAction(Patient myObject) { }

    public void outputAction() { quantity++; }

    public void printResult() {
        System.out.println(name + "\n\tquantity = " + quantity);
    }

    public void printInfo() {
        System.out.println(name + " state = " + state +
                " quantity = " + quantity +
                " tnext = " + getNextTime());
    }

    public void doStatistics(double delta) {}

    public boolean isQueueNotFull() {
        return true;
    }

    public double getNextTime() {
        return nextTime;
    }
}