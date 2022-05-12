package nl.tue.vrp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Route {

    private final Vehicle vehicle;
    private final List<Visit> visits;

    public Route(Vehicle vehicle, List<Node> nodes) {
        this.vehicle = vehicle;
        List<Visit> tempVisits = new ArrayList<>();
        tempVisits.add(new Visit(vehicle, nodes.get(0)));
        int lastIdx = 0;
        for (int i = 1; i < nodes.size(); i++) {
            Visit prev = tempVisits.get(lastIdx);
            tempVisits.add(prev.nextVisit(nodes.get(i)));
            lastIdx++;
        }
        visits = Collections.unmodifiableList(tempVisits);
    }

    public Route(Vehicle vehicle, Node ... nodes) {
        this(vehicle, List.of(nodes));
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    @Override
    public String toString() {
        return String.format("Route[%s]", visits.stream()
                .map(Visit::toString)
                .collect(Collectors.joining(",\n")));
    }
}
