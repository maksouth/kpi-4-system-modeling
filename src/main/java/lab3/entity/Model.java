package lab3.entity;

import java.util.Comparator;
import java.util.List;

public class Model {
    private List<Element> list;
    double nextTime, currentTime;

    public Model(List<Element> elements) {
        list = elements;
        nextTime = 0.0;
        currentTime = nextTime;
    }


    public void Simulate(double time, boolean verbose) {
        while (currentTime < time)
        {
            nextTime = Double.MAX_VALUE;
            Element min = list.stream().min(Comparator.comparingDouble(Element::getNextTime)).get();
            nextTime = min.getNextTime();

            if (verbose)
                System.out.println("\nIt's time for event in " + min.name + ", time =   " + nextTime);

            for (Element e : list)
                e.doStatistics(nextTime - currentTime);


            currentTime = nextTime;
            for (Element e : list)
            {
                e.currentTime = currentTime;
                e.action.run();
            }

            min.outputAction();
            for (Element e : list)
                if (e.getNextTime() == currentTime)
                    e.outputAction();

            if (verbose) { printInfo(); }
        }

        if (verbose) { printResult(); }
    }

    public void printInfo()
    {
        for (Element e : list)
            e.printInfo();
    }

    public void printResult()
    {
        System.out.println("\n-------------RESULTS-------------");
        for (Element e : list)
            e.printResult();
    }
}
