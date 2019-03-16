package lab3.entity;

public class Patient {
    private GroupType type;
    private GroupType startType;
    private double creationTime;
    private double deletionTime;

    public Patient(double creationTime) {
        type = GroupType.First;
        startType = GroupType.First;
        this.creationTime = creationTime;
    }

    public Patient(GroupType type, double creationTime) {
        this.type = type;
        this.startType = type;
        this.creationTime = creationTime;
    }

    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) {
        this.type = type;
    }

    public GroupType getStartType() {
        return startType;
    }

    public void setStartType(GroupType startType) {
        this.startType = startType;
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
