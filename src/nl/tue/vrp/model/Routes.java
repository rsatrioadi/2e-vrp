package nl.tue.vrp.model;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Routes {

    private final boolean satisfied;
    private final List<Vehicle> vehicles;
    private final List<Route> routes;

    public Routes(List<Vehicle> vehicles, List<Node> nodes, BiFunction<List<Vehicle>, List<Node>, List<Route>> routingStrategy) {
        this.vehicles = vehicles.stream().collect(Collectors.toUnmodifiableList());
        this.routes = routingStrategy.apply(vehicles, nodes).stream().collect(Collectors.toUnmodifiableList());
        this.satisfied = this.routes.stream()
                .flatMap(r -> r.getVisits().stream().map(Visit::getNode))
                .collect(Collectors.toUnmodifiableList())
                .containsAll(nodes);
    }

    public boolean isSatisfied() {
        return satisfied;
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
