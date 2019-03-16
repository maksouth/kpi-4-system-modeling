package lab3.entity;

public class PatientThread
{
    public Patient patient;
    public double nextTime;
    public int state;

    public PatientThread() {
        patient = new Patient(0);
        nextTime = Double.MAX_VALUE;
        state = 0;
    }
}