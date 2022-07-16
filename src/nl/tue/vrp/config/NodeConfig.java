package nl.tue.vrp.config;

import java.awt.*;
import java.util.List;

public class NodeConfig {
    protected int id;
    protected Point location;
    protected int serviceTime;
    protected TimeWindowConfig[] availabilities;

    protected NodeConfig() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public TimeWindowConfig[] getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(TimeWindowConfig[] availabilities) {
        this.availabilities = availabilities;
    }
}
