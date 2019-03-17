package lab3.car_bank;

import lab2.simsimple.FunRand;
import lab3.entity.*;
import lab3.entity.Process;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CarBankModel {

    public static final int MIN_PRIORITY = 1;
    public static final int MAX_PRIORITY = 5;

    Create generator;
    Dispose firstDispose;
    Dispose secondDispose;
    private Process first;
    private Process second;
    private LineSwitcher lineSwitcher;
    private List<DelayedTask> tasks;

    public CarBankModel() {
        firstDispose = new Dispose();
        secondDispose = new Dispose();

        first = new Process(
                4,
                (ignored) -> FunRand.Exp(0.3),
                firstDispose,
                MAX_PRIORITY
        );
        second = new Process(
                4,
                (ignored) -> FunRand.Exp(0.3),
                secondDispose,
                MIN_PRIORITY
        );

        Function<List<Process>, Process> router = (processes) -> {
            double min = processes.stream().map(Process::getQueueLength).min(Comparator.naturalOrder()).get();
            return processes.stream()
                    .filter(item -> item.getQueueLength() == min)
                    .max(Comparator.comparingInt(Process::getPriority))
                    .get();
        };

        lineSwitcher = new LineSwitcher(first, second);

        first.setListener(lineSwitcher);
        second.setListener(lineSwitcher);

        generator = new Create(
                0.1,
                (ignored) -> FunRand.Exp(0.5),
                Arrays.asList(first, second),
                router
        );

        tasks = Arrays.asList(generator, first, second);
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
                item.processEvent();
        }

        printInfo(currentTime);
    }

    private void printInfo(double currentTime) {
        generator.printInfo();
        lineSwitcher.printInfo();

        first.printInfo(currentTime);
        firstDispose.printInfo();

        second.printInfo(currentTime);
        secondDispose.printInfo();
    }
}
