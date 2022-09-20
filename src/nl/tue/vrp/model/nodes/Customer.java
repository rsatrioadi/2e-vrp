package nl.tue.vrp.model.nodes;

import nl.tue.vrp.config.CustomerConfig;
import nl.tue.vrp.model.TimeWindow;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Customer extends Node {

    protected Depot depot;

    public Customer(int id, int x, int y, int demand, int serviceTime, int earliest, int latest, Depot depot) {
        super(id, x, y, demand, serviceTime);
        this.availabilities = new TimeWindow[1];
        this.availabilities[0] = new TimeWindow(earliest, latest);
        this.depot = depot;
    }

    public Customer(CustomerConfig config) {
        super(config);
    }

    public Depot getDepot() {
        return depot;
    }

    public void setDepot(Depot depot) {
        this.depot = depot;
    }

    @Override
    public String toString() {
        return String.format("Customer id: %2d location: %s demand: %3d availabilities: %s",
                id,
                String.format("(Point x: %3d y: %3d)", location.x, location.y),
                demand,
                Arrays.stream(availabilities).map(TimeWindow::toString).collect(Collectors.joining(", ")));
    }
}
