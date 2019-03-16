package lab3.entity;

import java.util.ArrayList;
import java.util.List;

public class Dispose extends Element {
    public List<Patient> patients;

    public Dispose() {
        super();
        patients = new ArrayList<>();
        nextTime = Double.MAX_VALUE;
    }

    @Override
    public void inputAction(Patient patient)
    {
        quantity++;
        patient.setDeletionTime(currentTime);
        patients.add(patient);
        super.inputAction(patient);
        nextTime = Double.MAX_VALUE;
    }
}
