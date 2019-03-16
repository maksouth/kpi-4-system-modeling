package entity;

public class Create extends Element {

    public Create(double delay) {
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