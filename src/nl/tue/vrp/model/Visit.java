package nl.tue.vrp.model;

import nl.tue.vrp.model.nodes.Node;

import java.util.Optional;

public class Visit {

    private final Vehicle vehicle;
    private final Node node;
    private int load = 0;

    private Optional<Visit> prev, next;

    public Visit(Vehicle vehicle, Node node) {
        if (load > vehicle.getCapacity()) {
            throw new IllegalStateException(String.format("load (%d) exceeds vehicle capacity (%d)", load, vehicle.getCapacity()));
        }
        this.vehicle = vehicle;
        this.node = node;
        this.prev = Optional.empty();
        this.next = Optional.empty();
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getLoad() {
        return load;
    }

    public Node getNode() {
        return node;
    }

    public Visit addNextVisit(Node node) {
        Visit v = new Visit(vehicle, node);
        this.next.ifPresent(n -> n.prev = Optional.of(v));
        v.next = this.next;
        this.next = Optional.of(v);
        v.prev = Optional.of(this);
        if (node.isDelivery()) v.updatePrevVisits(node);
        if (node.isPickUp()) v.updateNextVisits(node);
        v.load = this.load - node.getDemand();
        return v;
    }

    private void updatePrevVisits(Node node) {
        prev.ifPresent(p -> {
            p.load += node.getDemand();
            p.updatePrevVisits(node);
        });
    }

    private void updateNextVisits(Node node) {
        next.ifPresent(n -> {
            n.load -= node.getDemand();
            n.updateNextVisits(node);
        });
    }

    @Override
    public String toString() {
        return String.format("Visit[vehicle.id= %d, vehicle.capacity= %d, load= %d, node.id= %d, node.demand= %d]", vehicle.getId(), vehicle.getCapacity(), load, node.getId(), node.getDemand());
    }

    public Optional<Visit> getPrev() {
        return prev;
    }

    public Optional<Visit> getNext() {
        return next;
    }
}
