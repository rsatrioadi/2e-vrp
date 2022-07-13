package nl.tue.vrp.model;

import nl.tue.vrp.model.nodes.Node;
import nl.tue.vrp.model.nodes.Satellite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Routes {

    private final boolean satisfied;
    private final List<Vehicle> vehicles;
    private final List<Route> routes;

    public Routes(Satellite origin,
                  BiFunction<Visit, List<Node>, Node> nodeSearchStrategy) {

        this.vehicles = origin.getVehicles().stream()
                .parallel()
                .collect(Collectors.toUnmodifiableList());

        List<Vehicle> remainingVehicles = new ArrayList<>(vehicles);
        List<Node> remainingNodes = new ArrayList<>(origin.listCustomers());
        List<Route> tRoutes = new ArrayList<>();

        while (!remainingVehicles.isEmpty() && !remainingNodes.isEmpty()) {
            Vehicle currentVehicle = remainingVehicles.remove(0);
            Route currentRoute = new Route(origin, currentVehicle, remainingNodes, Route.Constraints.CHECK_ALL, nodeSearchStrategy);
            tRoutes.add(currentRoute);
            remainingNodes.removeAll(currentRoute.getVisits().stream()
                    .parallel()
                    .map(Visit::getNode)
                    .collect(Collectors.toList()));
        }

        this.routes = tRoutes.stream().parallel().collect(Collectors.toUnmodifiableList());
        this.satisfied = new HashSet<>(this.routes.stream()
                .parallel()
                .flatMap(r -> r.getVisits().stream()
                        .parallel()
                        .map(Visit::getNode))
                .collect(Collectors.toUnmodifiableList()))
                .containsAll(origin.listCustomers());
    }

    public boolean isSatisfied() {
        return satisfied;
    }

    public List<Vehicle> getVehicles() {
        return vehicles.stream()
                .parallel()
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Route> getRoutes() {
        return routes.stream()
                .parallel()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String toString() {
        String s = String.format("Routes satisfied: %s routes: #(\n%s)",
                satisfied,
                routes.stream()
                        .parallel()
                        .map(route -> String.format("  %s", route.toString()))
                        .collect(Collectors.joining(",\n")));
        return s;
    }
}
