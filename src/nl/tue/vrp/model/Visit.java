package nl.tue.vrp.model;

public class Visit {

    private final Vehicle vehicle;
    private final Node node;
    private final int load;

    private Visit(Vehicle vehicle, int load, Node node) {
        if (load > vehicle.getCapacity()) {
            throw new IllegalStateException(String.format("load (%d) exceeds vehicle capacity (%d)", load, vehicle.getCapacity()));
        }
        this.vehicle = vehicle;
        this.load = load;
        this.node = node;
    }

    public Visit(Vehicle vehicle, Node node) {
        this(vehicle, node.getDemand(), node);
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getLoad() {
        return load;
    }

    public boolean canVisitNext(Node node) {
        return (vehicle.getCapacity() - load) > node.getDemand();
    }

    public Visit nextVisit(Node node) {
        return new Visit(vehicle, load + node.getDemand(), node);
    }

    @Override
    public String toString() {
        return String.format("Visit[vehicle.id= %d, vehicle.capacity= %d, load= %d, node.id= %d, node.demand= %d]", vehicle.getId(), vehicle.getCapacity(), load, node.getId(), node.getDemand());
    }
}
