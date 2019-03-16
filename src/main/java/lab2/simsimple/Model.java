package lab2.simsimple;

import java.util.ArrayList;
import java.util.Comparator;

public class Model {
    private ArrayList<Element> list;
    private double currentTime;
    private int event;

    Model(ArrayList<Element> elements) {
        list = elements;
        currentTime = 0.0;
        event = 0;
    }

    public void simulate(double totalSimulationTime) {
        double delta;

        while (currentTime < totalSimulationTime) {
            Element closestEventElement = findClosestEventElement(currentTime);

            delta = closestEventElement.getNextEventTime() - currentTime;
            currentTime = closestEventElement.getNextEventTime();

            System.out.println("\nIt's time for event in " + closestEventElement.getName() + ", time = " + currentTime);

            for (Element e : list)
                e.doStatistics(delta);

            for (Element e : list)
                e.setCurrentModelTime(currentTime);

            for (Element e : list)
                if (e.getNextEventTime() == currentTime && e.state == Element.State.BUSY)
                    e.finishExecution();

            printInfo();
        }
        printResult(currentTime);
    }

    private Element findClosestEventElement(double currentTime) {
        return list.stream()
                .filter(element -> element.getNextEventTime() >= currentTime)
                .filter(element -> element.state == Element.State.BUSY)
                .min(Comparator.comparingDouble(Element::getNextEventTime))
                .get();
    }

    private void printInfo() {
        for (Element e : list) e.printInfo();
    }

    private void printResult(double totalTime) {
        System.out.println("\n-------------RESULTS-------------");
        for (Element e : list) {
            e.printResult(totalTime);
            if (e instanceof Process) {
                Process p = (Process) e;
                System.out.println("mean length of queue = " +
                        p.getMeanQueue() / totalTime

                        + "\nfailure probability = " +
                        p.getAmountOfDroppedEvents() / p.getProcessedEventsCount());

            }
        }
    }
}