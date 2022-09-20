package nl.tue.vrp.model;

import nl.tue.vrp.model.nodes.Node;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Route {

    private final Vehicle vehicle;
    private final Visit firstVisit, lastVisit;
    private final List<Visit> visits;
    private final EnumSet<Constraints> constraints;

    public Route(Node origin, Vehicle vehicle, List<Node> nodes, EnumSet<Constraints> constraints, BiFunction<Visit, List<Node>, Node> nodeSearchStrategy) {
        this.vehicle = vehicle;
        this.constraints = constraints;
        this.firstVisit = new Visit(vehicle, origin);
        this.lastVisit = this.firstVisit.addNextVisit(origin);
        Visit currentVisit = this.firstVisit;

        List<Node> remainingNodes = new ArrayList<>(nodes);
        List<Node> skippedNodes = new ArrayList<>();

        boolean skip;
        while (!remainingNodes.isEmpty()) {
            Node nextNode = nodeSearchStrategy.apply(currentVisit, remainingNodes);
            if (visitFeasible(currentVisit, nextNode)) {
                currentVisit = currentVisit.addNextVisit(nextNode);
                remainingNodes.remove(nextNode);
                skip = false;
            } else {
                skip = true;
                remainingNodes.remove(nextNode);
                skippedNodes.add(nextNode);
            }
            if (!skip) {
                remainingNodes.addAll(skippedNodes);
                skippedNodes.clear();
            }
        }

        List<Visit> tVisits = new ArrayList<>();
        Visit tVisit = this.firstVisit;
        tVisits.add(tVisit);
        while (tVisit.getNext().isPresent()) {
            tVisit = tVisit.getNext().get();
            tVisits.add(tVisit);
        }
        this.visits = tVisits.stream()
                .parallel()
                .collect(Collectors.toUnmodifiableList());
    }

    private boolean visitFeasible(Visit currentVisit, Node nextNode) {
        boolean feasible = true;
        if (constraints.contains(Constraints.CHECK_CAPACITY)) {
            feasible &= checkCapacity(currentVisit, nextNode);
        }
        if (constraints.contains(Constraints.CHECK_TIME)) {
            feasible &= checkTimeHard(currentVisit, nextNode);
        }
        return feasible;
    }

    private boolean checkCapacity(Visit currentVisit, Node nextNode) {
        Visit v = currentVisit;
        if (nextNode.isDelivery()) {
            boolean safe = v.getLoad() + nextNode.getDemand() <= vehicle.getCapacity();
            while (safe && v.getPrev().isPresent()) {
                v = v.getPrev().get();
                safe = v.getLoad() + nextNode.getDemand() <= vehicle.getCapacity();
            }
            return safe;
        } else if (nextNode.isPickUp()) {
            boolean safe = true;
            while (safe && v.getNext().isPresent()) {
                v = v.getNext().get();
                safe = v.getLoad() - nextNode.getDemand() <= vehicle.getCapacity();
            }
            return safe;
        } else {
            return true;
        }
    }

    private boolean checkTimeHard(Visit currentVisit, Node nextNode) {
        int travelTime = (int)Math.ceil(currentVisit.getNode().getLocation().distance(nextNode.getLocation()) / vehicle.speed);
        int earliestArrivalTime = currentVisit.getDepartureTime() + travelTime;
        int earliestAvailableTime = nextNode.getEarliestAvailableTime(earliestArrivalTime);
        if (earliestAvailableTime == -1) {
            return false;
        }
        return true;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public List<Visit> getVisits() {
        return visits.stream()
                .parallel()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String toString() {
        return String.format("Route vehicle: (Vehicle id: %d capacity: %d) maxLoad: %2d totalCost: %6.2f visits: #(\n%s)",
                vehicle.getId(),
                vehicle.getCapacity(),
                getVisits().stream()
                        .parallel()
                        .mapToInt(Visit::getLoad)
                        .max().getAsInt(),
                lastVisit.getAccumulatedCost(),
                getVisits().stream()
                        .parallel()
                        .map(visit -> String.format("    (%s)", visit.toString()))
                        .collect(Collectors.joining(",\n")));
    }

    public enum Constraints {
        CHECK_CAPACITY, CHECK_TIME;
        public static final EnumSet<Constraints> CHECK_ALL = EnumSet.allOf(Constraints.class);
    }
}
