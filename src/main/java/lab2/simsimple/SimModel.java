package lab2.simsimple;

import entity.Create;
import entity.Element;
import entity.Process;

import java.util.ArrayList;

public class SimModel {
    public static void main(String[] args) {

        Create create = new Create(2.0);
        Process process1 = new Process(1.0, 10);
        Process process2 = new Process(2.0, 10);
        Process process3 = new Process(3.0, 10);
        Process process4 = new Process(3.0, 10);

        create.addNextElement(process1, 1.0);
        process1.addNextElement(process2, 1.0);
        process2.addNextElement(process3, 0.3);
        process2.addNextElement(process4, 0.7);

        create.setDistribution("exp");
        process1.setDistribution("exp");
        process2.setDistribution("exp");
        process3.setDistribution("exp");
        process3.setDistribution("norm");

        ArrayList<Element> list = new ArrayList<>();
        list.add(create);
        list.add(process1);
        list.add(process2);
        list.add(process3);
        list.add(process4);

        Model model = new Model(list);
        model.simulate(10000.0);
    }
}
