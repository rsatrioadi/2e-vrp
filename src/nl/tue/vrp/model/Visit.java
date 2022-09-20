package nl.tue.vrp.model;

import nl.tue.vrp.model.nodes.Node;

import java.util.Optional;

public class Visit {

    private final Vehicle vehicle;
    private final Node node;
    private int load = 0;
    private double legCost, accumulatedCost;
    private int arrivalTime, departureTime;

    private Visit prev, next;

    public Visit(Vehicle vehicle, Node node) {
        this.vehicle = vehicle;
        this.node = node;
        this.prev = null;
        this.next = null;
        this.legCost = 0;
        this.accumulatedCost = 0;
        this.arrivalTime = 0;
        this.departureTime = 0;
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

    public double getLegCost() {
        return legCost;
    }

    public double getAccumulatedCost() {
        return accumulatedCost;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public Visit addNextVisit(Node node) {

        Visit v = new Visit(vehicle, node);

        this.getNext().ifPresent(n -> n.prev = v);
        v.next = this.next;
        this.next = v;
        v.prev = this;

        if (node.isDelivery()) v.updatePrevVisits(node);
        if (node.isPickUp()) v.updateNextVisits(node);

        v.load = this.load - node.getDemand();
        v.legCost = node.getLocation().distance(this.getNode().getLocation());
        v.accumulatedCost = this.accumulatedCost + v.legCost;

        int travelTime = (int)Math.ceil(this.node.getLocation().distance(node.getLocation()) / vehicle.speed);
        int earliestAvailableTime = node.getEarliestAvailableTime(departureTime + travelTime);
        v.arrivalTime = earliestAvailableTime;
        v.departureTime = earliestAvailableTime + node.getServiceTime();
        v.updateNextVisitCost(node);

        return v;
    }

    private void updateNextVisitCost(Node node) {
        getNext().ifPresent(n -> {
            n.legCost = n.getNode().getLocation().distance(node.getLocation());
            n.accumulatedCost = this.accumulatedCost + n.legCost;
            n.updateNextVisitCost(n.getNode());
        });
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
        return String.format("Visit load: %2d cost: %5.2f accumulatedCost: %6.2f node: (Node id: %2d demand: %3d)",
                load,
                legCost,
                accumulatedCost,
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
