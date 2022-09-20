package nl.tue.vrp.model;

import nl.tue.vrp.model.nodes.Depot;
import nl.tue.vrp.model.nodes.Node;
import nl.tue.vrp.model.nodes.Satellite;
import nl.tue.vrp.output.RouteOutput;
import nl.tue.vrp.strategy.nodesearch.NodeSearch;
import nl.tue.vrp.strategy.packagesearch.PackageSearch;
import nl.tue.vrp.strategy.selectvehicle.SelectVehicle;

import java.util.*;
import java.util.stream.Collectors;

public class Routes {

    private final boolean satisfied;
    private final List<Vehicle> vehicles;
    private final List<Route> routes;

    public Routes(Satellite origin,
                  SelectVehicle selectVehicleStrategy,
                  PackageSearch packageSearchStrategy,
                  EnumSet<Constraints> checkConstraints
    ) {

        this.vehicles = origin.getVehicles().stream()
                .collect(Collectors.toUnmodifiableList());

        List<VehicleAvailability> remainingVehicles = vehicles.stream().map(VehicleAvailability::new).collect(Collectors.toList());
        List<PackageAvailability> remainingPackages = new ArrayList<>(origin.listPackages());
        List<Route> tRoutes = new ArrayList<>();

        while (!remainingVehicles.isEmpty() && !remainingPackages.isEmpty()) {
            VehicleAvailability currentVehicle = selectVehicleStrategy.getVehicle(remainingVehicles);
            Route currentRoute = new Route(origin, currentVehicle, remainingPackages, checkConstraints, packageSearchStrategy);
//            System.out.printf("routePackages: %d remainingVehicles: %d remainingPackages: %d\n",
//                    currentRoute.getPackages().size(), remainingVehicles.size(), remainingPackages.size());
            if (currentRoute.getVisits().size() == 2) {
                // there's no valid route, remove the vehicle
                remainingVehicles.remove(currentVehicle);
            } else {
                currentVehicle.setAvailableFrom(currentRoute.getLastVisit().getArrivalTime());
                tRoutes.add(currentRoute);
                remainingPackages.removeAll(currentRoute.getVisits().stream()
                        .parallel()
                        .map(v -> v.getPackage().orElse(null)).filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            }
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

    public Routes(Depot origin,
                  SelectVehicle selectVehicleStrategy,
                  PackageSearch packageSearchStrategy,
                  EnumSet<Constraints> checkConstraints) {

        this.vehicles = origin.getVehicles().stream()
                .collect(Collectors.toUnmodifiableList());

        List<VehicleAvailability> remainingVehicles = vehicles.stream().map(VehicleAvailability::new).collect(Collectors.toList());
        List<PackageAvailability> remainingPackages = new ArrayList<>(origin.listPackagesAvailability());
        List<Route> tRoutes = new ArrayList<>();

        while (!remainingVehicles.isEmpty() && !remainingPackages.isEmpty()) {
            VehicleAvailability currentVehicle = selectVehicleStrategy.getVehicle(remainingVehicles);
            Route currentRoute = new Route(origin, currentVehicle, remainingPackages, checkConstraints, packageSearchStrategy);

            if (currentRoute.getVisits().size() == 2) {
                // there's no valid route, remove the vehicle
                remainingVehicles.remove(currentVehicle);
            } else {
                currentVehicle.setAvailableFrom(currentRoute.getLastVisit().getArrivalTime());
                tRoutes.add(currentRoute);
                remainingPackages.removeAll(currentRoute.getVisits().stream()
                        .parallel()
                        .map(v -> v.getPackage().orElse(null)).filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            }
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
                .collect(Collectors.toList());
    }

    public List<Route> getRoutes() {
        return routes.stream()
                .parallel()
                .collect(Collectors.toList());
    }

    public List<Route> getRoutesFromVehicle(Vehicle vehicle) {
        return routes.stream().filter(v -> v.getVehicle() == vehicle).collect(Collectors.toList());
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

    public List<RouteOutput> toOutput() {
        return routes.stream().map(Route::toOutput).collect(Collectors.toList());
    }
}
