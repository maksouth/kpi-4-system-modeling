package lab3.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PriorityRoute extends Element {

    private Patient patient;
    private Random random = new Random();
    public List<Element> routes;

    public PriorityRoute(List<Element> routes)
    {
        super();
        List<Element> Routes = new ArrayList<>();
        patient = new Patient(currentTime);
        this.routes = routes;
    }

    @Override
    public void inputAction(Patient patient)
    {
        nextTime = currentTime;
        this.patient = patient;
    }

    @Override
    public void outputAction()
    {
        super.outputAction();
        nextTime = Double.MAX_VALUE;

        if (routes != null && routes.size() != 0)
            getPriority(routes).inputAction(patient);
    }

    public Element getPriority(List<Element> elements)
    {
        final int maxPriority = elements.stream().max(Comparator.comparingInt(o -> o.priority)).get().priority;
        AtomicInteger maxPriorityMutable = new AtomicInteger(maxPriority);
        List<Element> list1 = elements.stream().filter(Element::isQueueNotFull).collect(Collectors.toList());

        if (list1.size() == 0)
            return elements.stream().filter(item -> item.priority == maxPriority).findFirst().get();

        List<Element> list2 = elements.stream()
                .filter(item -> item.priority == maxPriority && item.isQueueNotFull())
                .collect(Collectors.toList());

        while (list2.size() == 0 && maxPriority > 0)
        {
            list2 = elements
                    .stream()
                    .filter(item -> item.priority == maxPriorityMutable.get() && item.isQueueNotFull())
                    .collect(Collectors.toList());
            maxPriorityMutable.decrementAndGet();
        }
        return list2.get(random.nextInt(list2.size()));
    }
}
