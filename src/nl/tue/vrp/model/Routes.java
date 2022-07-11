package nl.tue.vrp.model;

import nl.tue.vrp.model.nodes.Node;
import nl.tue.vrp.model.nodes.Satellite;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Routes {

    private final boolean satisfied;
    private final List<Vehicle> vehicles;
    private final List<Route> routes;

    public Routes(Satellite origin,
                  BiFunction<Visit, List<Node>, Node> nodeSearchStrategy) {

        this.vehicles = origin.getVehicles().parallelStream().collect(Collectors.toUnmodifiableList());

        List<Vehicle> remainingVehicles = new ArrayList<>(vehicles);
        List<Node> remainingNodes = new ArrayList<>(origin.listCustomers());
        List<Route> tRoutes = new ArrayList<>();

        while (!remainingVehicles.isEmpty() && !remainingNodes.isEmpty()) {
            Vehicle currentVehicle = remainingVehicles.remove(0);
            Route currentRoute = new Route(origin, currentVehicle, remainingNodes, Route.Constraints.CHECK_ALL, nodeSearchStrategy);
            tRoutes.add(currentRoute);
            remainingNodes.removeAll(currentRoute.getVisits().parallelStream()
                    .map(Visit::getNode)
                    .collect(Collectors.toList()));
        }


        this.routes = tRoutes.parallelStream().collect(Collectors.toUnmodifiableList());
        this.satisfied = this.routes.stream()
                .flatMap(r -> r.getVisits().stream().map(Visit::getNode))
                .collect(Collectors.toUnmodifiableList())
                .containsAll(origin.listCustomers());
    }

    public boolean isSatisfied() {
        return satisfied;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    @Override
    public String toString() {
        String s = String.format("Routes[satisfied= %s, routes=[\n%s\n]", satisfied, routes.stream().map(Route::toString).collect(Collectors.joining(",\n")));
        return s;
    }
}
