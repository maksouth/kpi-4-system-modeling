package lab1.simsimple;

public class Create extends Element {

    Create(double delay) {
        super(delay, "CREATOR");
        setState(1);
    }


    @Override
    public void outAct() {
        super.outAct();

        setState(0);
        setTnext(getTcurr() + getDelay());
        getNextElement().inAct();
    }
}