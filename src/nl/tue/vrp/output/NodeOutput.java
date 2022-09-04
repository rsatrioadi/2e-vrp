package nl.tue.vrp.output;

import java.awt.Point;

public class NodeOutput {
    protected int id;
    protected Point location;
    protected int serviceTime;
    protected TimeWindowOutput availability;

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

    public TimeWindowOutput getAvailability() {
        return availability;
    }

    public void setAvailability(TimeWindowOutput availability) {
        this.availability = availability;
    }
}
