package lab3.entity;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Model {

    private List<DelayedTask> tasks;

    public Model(List<DelayedTask> tasks) {
        this.tasks = tasks;
    }

    public void simulate(double totalSimulationTime) {
        double currentTime = 0.0;

        while (currentTime < totalSimulationTime) {
            Double nextEventTime = tasks.stream()
                    .map(DelayedTask::getFinishEventProcessingTime)
                    .min(Comparator.naturalOrder()).get();

            List<DelayedTask> processingItems = tasks.stream()
                    .filter(item -> item.getFinishEventProcessingTime() == nextEventTime)
                    .collect(Collectors.toList());

            for (DelayedTask item: tasks)
                item.calculateStats(nextEventTime - currentTime);

            currentTime = nextEventTime;

            for (DelayedTask item: processingItems)
                item.processEvent(currentTime);
        }
    }
}
