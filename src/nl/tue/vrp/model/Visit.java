package nl.tue.vrp.model;

import nl.tue.vrp.model.nodes.Node;
import nl.tue.vrp.output.VisitOutput;

import java.util.Optional;

public class Visit {

    private final Vehicle vehicle;
    private final Node node;
    private final PackageAvailability aPackage;
    private int load = 0;
    private double legCost, accumulatedCost;
    private double remainingFuel;
    private int arrivalTime, departureTime;

    private Visit prev, next;

    public Visit(Vehicle vehicle, Node node, PackageAvailability aPackage) {
        this.vehicle = vehicle;
        this.node = node;
        this.prev = null;
        this.next = null;
        this.aPackage = aPackage;
        this.legCost = 0;
        this.accumulatedCost = 0;
        this.arrivalTime = 0;
        this.departureTime = node.getServiceTime();
        this.load = 0;
    }

    public Visit(VehicleAvailability vehicle, Node node, PackageAvailability aPackage) {
        this.vehicle = vehicle.getVehicle();
        this.node = node;
        this.prev = null;
        this.next = null;
        this.aPackage = aPackage;
        this.legCost = 0;
        this.accumulatedCost = 0;
        this.arrivalTime = vehicle.getAvailableFrom();
        this.departureTime = vehicle.getAvailableFrom() + node.getServiceTime();
        this.remainingFuel = vehicle.getRemainingFuel();
        this.load = 0;
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

    public double getRemainingFuel() {
        return remainingFuel;
    }

    public Visit addNextVisit(Node node) {
        Visit v = new Visit(vehicle, node, null);

        this.getNext().ifPresent(n -> n.prev = v);
        v.next = this.next;
        this.next = v;
        v.prev = this;

        v.load = this.load;
        if (node.isDelivery()) v.updatePrevVisits(node);
        if (node.isPickUp()) v.updateNextVisits(node);

        v.legCost = node.getLocation().distance(this.getNode().getLocation());
        v.accumulatedCost = this.accumulatedCost + v.legCost;

        double distance = this.node.getLocation().distance(node.getLocation());
        int travelTime = 0;
        if (vehicle.getSpeed() > 0) {
            travelTime = (int)Math.ceil(distance / vehicle.speed);
        }
        double fuelUsed = 0;
        if (vehicle.getFuelPerDistance() > 0) {
            fuelUsed = distance / vehicle.getFuelPerDistance();
        }
        v.remainingFuel = this.remainingFuel - fuelUsed;
        int earliestAvailableTime = departureTime + travelTime;
        v.arrivalTime = earliestAvailableTime;
        v.departureTime = earliestAvailableTime + node.getServiceTime();
        v.updateNextVisitCost(node);

        return v;
    }

    public Visit addNextVisit(PackageAvailability aPackage) {
        Visit v = new Visit(vehicle, aPackage.getNode(), aPackage);

        this.getNext().ifPresent(n -> n.prev = v);
        v.next = this.next;
        this.next = v;
        v.prev = this;

        v.load = this.load;
        if (aPackage.getPackage().isDelivery()) {
            v.updatePrevVisits(aPackage.getPackage());
        }
        if (aPackage.getPackage().isPickup()) {
            v.load += aPackage.getPackage().getDemand();
            v.updateNextVisits(aPackage.getPackage());
        }

        v.legCost = aPackage.getNode().getLocation().distance(this.getNode().getLocation());
        v.accumulatedCost = this.accumulatedCost + v.legCost;
        double distance = this.node.getLocation().distance(aPackage.getNode().getLocation());
        int travelTime = 0;
        if (vehicle.getSpeed() > 0) {
            travelTime = (int)Math.ceil(distance / vehicle.getSpeed());
        }
        double fuelUsed = 0;
        if (vehicle.getFuelPerDistance() > 0) {
            fuelUsed = distance / vehicle.getFuelPerDistance();
        }
        v.remainingFuel = this.remainingFuel - fuelUsed;
        int earliestAvailableTime = aPackage.getEarliestAvailableTime(departureTime + travelTime);
        v.arrivalTime = earliestAvailableTime;
        v.departureTime = earliestAvailableTime + aPackage.getNode().getServiceTime();
        v.updateNextVisitCost(aPackage.getNode());
        v.updateNextVisitArrival();
        return v;
    }

    private void updateNextVisitArrival() {
        getNext().ifPresent(n -> {
            int travelTime = (int)Math.ceil(this.node.getLocation().distance(n.node.getLocation()) / vehicle.speed);
            n.arrivalTime = this.departureTime + travelTime;
            n.departureTime = n.arrivalTime + n.getNode().getServiceTime();
            n.updateNextVisitArrival();
        });
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

    private void updatePrevVisits(Package aPackage) {
        getPrev().ifPresent(p -> {
            p.load += aPackage.getDemand();
            p.updatePrevVisits(aPackage);
        });
    }

    private void updateNextVisits(Node node) {
        getNext().ifPresent(n -> {
            n.load += node.getDemand();
            n.updateNextVisits(node);
        });
    }

    private void updateNextVisits(Package aPackage) {
        getNext().ifPresent(n -> {
            n.load += aPackage.getDemand();
            n.updateNextVisits(aPackage);
        });
    }

    @Override
    public String toString() {
        return String.format("Visit load: %d cost: %.2f accumulatedCost: %.2f remfuel: %.2f node: (Node id: %d demand: %d), time: (arrival: %d, departure: %d), type: %s",
                load,
                legCost,
                accumulatedCost,
                remainingFuel,
                node.getId(),
                aPackage != null ? aPackage.getPackage().getDemand() : 0,
                arrivalTime,
                departureTime,
                aPackage != null ? aPackage.getPackage().getType().toString() : "-");
    }

    public Optional<Visit> getPrev() {
        return Optional.ofNullable(prev);
    }


    public Optional<Visit> getNext() {
        return Optional.ofNullable(next);
    }

    public Optional<PackageAvailability> getPackage() {
        return Optional.ofNullable(this.aPackage);
    }

    public VisitOutput toOutput() {
        VisitOutput vo = new VisitOutput();
        vo.setAccumulatedCost(accumulatedCost);
        if (aPackage != null) {
            vo.setCustomerID(aPackage.getPackage().getCustomer().getId());
            vo.setDemand(aPackage.getPackage().getDemand());
        }
        vo.setLegCost(legCost);
        vo.setRemainingFuel(remainingFuel);
        vo.setLoad(load);
        vo.setVehicleID(vehicle.getId());
        vo.setArrivalTime(arrivalTime);
        vo.setDepartureTime(departureTime);
        vo.setNodeID(node.getId());
        return vo;
    }
}
