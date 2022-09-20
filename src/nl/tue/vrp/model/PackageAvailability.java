package nl.tue.vrp.model;

import nl.tue.vrp.model.nodes.Node;

public class PackageAvailability {
    private final Package aPackage;
    private final TimeWindow availability;
    private final Node node;

    public PackageAvailability(Package aPackage, Node node, TimeWindow availability) {
        this.aPackage = aPackage;
        this.availability = availability;
        this.node = node;
    }

    public Package getPackage() {
        return aPackage;
    }

    public TimeWindow getAvailability() {
        return availability;
    }

    public Node getNode() {
        return node;
    }

    public int getEarliestAvailableTime(int currentTime) {
        int earliestTime = Math.max(currentTime, availability.getStartTime());
        if (earliestTime + this.node.getServiceTime() <= availability.getEndTime()) {
            return earliestTime;
        }
        return -1;
    }

    @Override
    public String toString() {
        return String.format("package: (%s), availability: %s, location: %d",
                aPackage,
                availability,
                node.getId());
    }
}
