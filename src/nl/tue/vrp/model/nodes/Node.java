package nl.tue.vrp.model.nodes;

import java.awt.*;

public abstract class Node {

    protected final int id;
    protected final Point location;
    protected final int demand;
    protected final int serviceTime;

    protected Node(int id, int x, int y, int demand, int serviceTime) {
        this.id = id;
        this.location = new Point(x, y);
        this.demand = demand;
        this.serviceTime = serviceTime;
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

    @Override
    public String toString() {
        return String.format("Node[id= %d, location= %s, demand= %d]", id, location, demand);
    }
}
