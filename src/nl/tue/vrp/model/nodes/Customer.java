package nl.tue.vrp.model.nodes;

public class Customer extends Node {

    private final int earliest;
    private final int latest;
    private final Depot depot;

    public Customer(int id, int x, int y, int demand, int serviceTime, int earliest, int latest, Depot depot) {
        super(id, x, y, demand, serviceTime);
        this.earliest = earliest;
        this.latest = latest;
        this.depot = depot;
    }

    public int getEarliest() {
        return earliest;
    }

    public int getLatest() {
        return latest;
    }

    public Depot getDepot() {
        return depot;
    }

    @Override
    public String toString() {
        return String.format("Customer[id= %d, location= %s, demand= %d]", id, location, demand);
    }
}
