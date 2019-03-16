package lab2.simsimple;

public class Create extends Element {

    Create(double delay) {
        super(delay, "CREATOR");
        state = State.BUSY;
    }

    @Override
    public void finishExecution() {
        super.finishExecution();
        nextEventTime = getCurrentModelTime() + getDelay();
        getNextElement().startExecution();
    }
}