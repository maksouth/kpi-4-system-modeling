package lab3.entity;

public class Create extends Element {
    public GroupType type;

    public Create(double meanDelay, double delayDeviation) {
        super(meanDelay, delayDeviation, Distribution.Exponential);
        this.type = GroupType.First;
    }

    public Create() {
        super();
        type = GroupType.First;
    }

    @Override
    public void outputAction()
    {
        super.outputAction();
        nextTime = currentTime + getDelay();
        nextElement.inputAction(new Patient(type, currentTime));
    }
}
