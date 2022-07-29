package nl.tue.vrp.model.nodes;

import nl.tue.vrp.config.NodeConfig;
import nl.tue.vrp.model.TimeWindow;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Node {

    protected final int id;
    protected final Point location;
    protected final int demand;
    protected final int serviceTime;
    protected TimeWindow[] availabilities;

    protected Node(int id, int x, int y, int demand, int serviceTime) {
        this.id = id;
        this.location = new Point(x, y);
        this.demand = demand;
        this.serviceTime = serviceTime;
        this.availabilities = new TimeWindow[0];
    }

    protected Node(NodeConfig config) {
        this.id = config.getId();
        this.location = config.getLocation();
        this.demand = config.getId();
        this.serviceTime = config.getServiceTime();
        if (config.getAvailabilities() == null) {
            this.availabilities = new TimeWindow[0];
        } else {
            this.availabilities = Stream.of(config.getAvailabilities()).map(TimeWindow::new).toArray(TimeWindow[]::new);
            Arrays.sort(this.availabilities);
        }
    }

    public int getId() {
        return id;
    }

    public Point getLocation() {
        return location;
    }

    public int getDemand() {
        return demand;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public int getEarliest() {
        return Integer.MIN_VALUE;
    }

    public int getLatest() {
        return Integer.MAX_VALUE;
    }

    public boolean isDelivery() {
        return demand > 0;
    }

    public boolean isPickUp() {
        return demand < 0;
    }

    public void setAvailabilities(TimeWindow[] availabilities) {
        this.availabilities = availabilities;
    }

    public TimeWindow[] getAvailabilities() {
        return availabilities;
    }

    public int getEarliestAvailableTime(int currentTime) {
        if (availabilities.length == 0) {
            return currentTime;
        }
        for (TimeWindow availability :  availabilities) {
            int earliestTime = Math.max(currentTime, availability.getStartTime());
            if (earliestTime + serviceTime <= availability.getEndTime()) {
                return earliestTime;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return String.format("Node id: %2d location: %s demand: %d availabilities: %s",
                id,
                String.format("(Point x: %3d y: %3d)", location.x, location.y),
                demand,
                Arrays.stream(availabilities).map(TimeWindow::toString).collect(Collectors.joining(", ")));
    }
}
