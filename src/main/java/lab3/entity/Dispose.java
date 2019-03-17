package lab3.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Dispose implements BiConsumer<Entity, Double> {

    private List<Entity> entities = new ArrayList<>();

    @Override
    public void accept(Entity client, Double deletionTime) {
        client.setDeletionTime(deletionTime);
        entities.add(client);
    }

    public void printInfo() {
        double averageProcessingTime = entities.stream()
                .mapToDouble(entity -> entity.getDeletionTime() - entity.getCreationTime())
                .sum() / entities.size();

        double leaveTimes = 0;
        for (int i = 1; i < entities.size(); i++)
            leaveTimes += entities.get(i).getDeletionTime() - entities.get(i-1).getDeletionTime();
        leaveTimes /= (entities.size() - 1);

        System.out.println("Average processing time " + averageProcessingTime +
                " average iterval between leaves " + leaveTimes);
    }
}
