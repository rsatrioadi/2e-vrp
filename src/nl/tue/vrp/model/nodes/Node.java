package nl.tue.vrp.model.nodes;

import nl.tue.vrp.config.NodeConfig;
import nl.tue.vrp.model.TimeWindow;
import nl.tue.vrp.output.NodeOutput;

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
    protected TimeWindow availability;

    protected Node(int id, int x, int y, int demand, int serviceTime) {
        this.id = id;
        this.location = new Point(x, y);
        this.demand = demand;
        this.serviceTime = serviceTime;
        this.availability = null;
    }

    protected Node(NodeConfig config) {
        this.id = config.getId();
        this.location = config.getLocation();
        this.demand = 0;
        this.serviceTime = config.getServiceTime();
        if (config.getAvailability() == null) {
            this.availability = null;
        } else {
            this.availability = new TimeWindow(config.getAvailability());
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

    public TimeWindow getAvailability() {
        return availability;
    }

    public void setAvailability(TimeWindow availability) {
        this.availability = availability;
    }

    public int getEarliestAvailableTime(int currentTime) {
        if (availability == null) {
            return currentTime;
        }
        int earliestTime = Math.max(currentTime, availability.getStartTime());
        if (earliestTime + serviceTime <= availability.getEndTime()) {
            return earliestTime;
        }
        return -1;
    }

    @Override
    public String toString() {
        return String.format("Node id: %2d location: %s demand: %d availabilities: %s",
                id,
                String.format("(Point x: %3d y: %3d)", location.x, location.y),
                demand,
                availability);
    }
}
