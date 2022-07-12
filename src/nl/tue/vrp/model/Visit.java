package nl.tue.vrp.model;

import nl.tue.vrp.model.nodes.Node;

import java.util.Optional;

public class Visit {

    private final Vehicle vehicle;
    private final Node node;
    private int load = 0;
    private int arrivalTime, departureTime;

    private Visit prev, next;

    public Visit(Vehicle vehicle, Node node) {
        this.vehicle = vehicle;
        this.node = node;
        this.prev = null;
        this.next = null;
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

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public Visit addNextVisit(Node node) {
        Visit v = new Visit(vehicle, node)
                ;
        this.getNext().ifPresent(n -> n.prev = v);
        v.next = this.next;
        this.next = v;
        v.prev = this;

        if (node.isDelivery()) v.updatePrevVisits(node);
        if (node.isPickUp()) v.updateNextVisits(node);

        v.load = this.load - node.getDemand();
        return v;
    }

    private void updatePrevVisits(Node node) {
        getPrev().ifPresent(p -> {
            p.load += node.getDemand();
            p.updatePrevVisits(node);
        });
    }

    private void updateNextVisits(Node node) {
        getNext().ifPresent(n -> {
            n.load -= node.getDemand();
            n.updateNextVisits(node);
        });
    }

    @Override
    public String toString() {
        return String.format("Visit[vehicle.id= %d, vehicle.capacity= %d, load= %d, node.id= %d, node.demand= %d]",
                vehicle.getId(),
                vehicle.getCapacity(),
                load,
                node.getId(),
                node.getDemand());
    }

    public Optional<Visit> getPrev() {
        return Optional.ofNullable(prev);
    }

    public Optional<Visit> getNext() {
        return Optional.ofNullable(next);
    }
}
