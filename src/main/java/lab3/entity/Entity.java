package lab3.entity;

public class Entity {
    private int type;
    private double creationTime;
    private double deletionTime;

    public Entity(int type, double creationTime) {
        this.type = type;
        this.creationTime = creationTime;
    }

    public Entity() { }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(double creationTime) {
        this.creationTime = creationTime;
    }

    public double getDeletionTime() {
        return deletionTime;
    }

    public void setDeletionTime(double deletionTime) {
        this.deletionTime = deletionTime;
    }
}
