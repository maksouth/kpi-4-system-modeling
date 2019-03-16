package lab3.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BranchRoute extends Element {
    private Patient myObject;
    private Random random = new Random();
    private List<Route> routes = new ArrayList<>();

    public BranchRoute(List<Route> routes) {
        myObject = new Patient(super.currentTime);
        this.routes = routes;
    }

    @Override
    public void inputAction(Patient myObject) {
        nextTime = currentTime;
        this.myObject = myObject;
    }

    @Override
    public void outputAction()
    {
        super.outputAction();
        nextTime = Double.MAX_VALUE;

        if (routes != null && routes.size() != 0)
        {
            double sum = 0;
            for (Route route: routes) sum += route.getProbabiity();

            double current = 0.0;
            double number = random.nextDouble() * sum;

            for (Route route : routes) {
                current += route.getProbabiity();
                if (current >= number) {
                    route.getNextElement().inputAction(myObject);
                    break;
                }
            }
        }
    }
}
