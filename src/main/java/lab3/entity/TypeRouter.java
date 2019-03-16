package lab3.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TypeRouter extends Element {

    public Patient patient;
    private Random random = new Random();
    private List<TypeRoute> routes;

    public TypeRouter(List<TypeRoute> routes) {
        super();
        patient = new Patient(currentTime);
        this.routes = routes;
    }

    @Override
    public void inputAction(Patient patient) {
        nextTime = currentTime;
        this.patient = patient;
    }

    @Override
    public void outputAction()
    {
        super.outputAction();
        nextTime = Double.MAX_VALUE;

        if (routes != null && routes.size() != 0) {
            TypeRoute typeRoute = routes.stream()
                    .filter(item -> item.getType() == patient.getType())
                    .findFirst()
                    .get();
            typeRoute.getNextElement().inputAction(patient);
        }
    }
}
