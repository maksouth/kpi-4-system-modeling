package lab3.car_bank;

import lab3.entity.*;
import lab3.entity.kotlin.Operator;

public class Simulation {

    public static final int MIN_PRIORITY = 1;
    public static final int MAX_PRIORITY = 5;

    static Create generator;
    static Dispose firstDispose;
    static Dispose secondDispose;
    static private Operator first;
    static private Operator second;
    static private LineSwitcher lineSwitcher;

    public static void main(String[] args) {

//        firstDispose = new Dispose();
//        secondDispose = new Dispose();
//
//        first = new Operator(
//                firstDispose,
//                (ignored) -> FunRand.Exp(0.3),
//                4,
//                MAX_PRIORITY
//        );
//        second = new Operator(
//                4,
//                (ignored) -> FunRand.Exp(0.3),
//                secondDispose,
//                MIN_PRIORITY
//        );
//
//        Function<List<Operator>, Operator> router = (processes) -> {
//            double min = processes.stream().map(Operator::getQueueLength).min(Comparator.naturalOrder()).get();
//            return processes.stream()
//                    .filter(item -> item.getQueueLength() == min)
//                    .max(Comparator.comparingInt(Operator::getPriority))
//                    .get();
//        };
//
//        lineSwitcher = new LineSwitcher(first, second);
//
//        first.setListener(lineSwitcher);
//        second.setListener(lineSwitcher);
//
//        generator = new Create(
//                0.1,
//                (ignored) -> FunRand.Exp(0.5),
//                Arrays.asList(first, second),
//                router
//        );
//
//        List<DelayedTask> tasks = Arrays.asList(generator, first, second);
//        Model model = new Model(tasks);
//
//        model.simulate(100);
//        printInfo(100);
    }

    private static void printInfo(double currentTime) {
        generator.printInfo();
        lineSwitcher.printInfo();

        first.printInfo(currentTime);
        firstDispose.printInfo();

        second.printInfo(currentTime);
        secondDispose.printInfo();
    }
}
