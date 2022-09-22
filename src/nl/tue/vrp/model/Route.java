package nl.tue.vrp.model;

import nl.tue.vrp.model.nodes.Node;
import nl.tue.vrp.output.RouteOutput;
import nl.tue.vrp.strategy.nodesearch.NodeSearch;
import nl.tue.vrp.strategy.packagesearch.PackageSearch;

import java.util.*;
import java.util.stream.Collectors;

public class Route {

    private final VehicleAvailability vehicle;
    private final Visit firstVisit, lastVisit;
    private final List<Visit> visits;
    private final EnumSet<Constraints> constraints;

    public Route(Node origin, VehicleAvailability vehicle, List<PackageAvailability> packages, EnumSet<Constraints> constraints, PackageSearch packageSearchStrategy) {
        this.vehicle = vehicle;
        this.constraints = constraints;
        this.firstVisit = new Visit(vehicle, origin, null);
        this.lastVisit = this.firstVisit.addNextVisit(origin);
        Visit currentVisit = this.firstVisit;

        List<PackageAvailability> remainingPackages = new ArrayList<>(packages);
        List<PackageAvailability> skippedPackages = new ArrayList<>();

        boolean skip;
        while (!remainingPackages.isEmpty()) {
            PackageAvailability nextPackage = packageSearchStrategy.getNextPackage(currentVisit, remainingPackages);
//            System.out.printf("current visit %s, next package %s, visible %b\n", currentVisit, nextPackage, visitFeasible(currentVisit, nextPackage));
            if (visitFeasible(currentVisit, nextPackage)) {
                currentVisit = currentVisit.addNextVisit(nextPackage);
                remainingPackages.remove(nextPackage);
                skip = false;
            } else {
                skip = true;
                remainingPackages.remove(nextPackage);
                skippedPackages.add(nextPackage);
            }
            if (!skip) {
                remainingPackages.addAll(skippedPackages);
                skippedPackages.clear();
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
//        System.out.printf("packages %d, rem %d, skipped %d, visits %d\n", packages.size(), remainingPackages.size(), skippedPackages.size(), this.visits.size() - 2);
    }

    private boolean visitFeasible(Visit currentVisit, PackageAvailability aPackage) {
        boolean feasible = true;
        if (constraints.contains(Constraints.CHECK_CAPACITY)) {
            feasible &= checkCapacity(currentVisit, aPackage);
        }
        if (constraints.contains(Constraints.CHECK_TIME)) {
            feasible &= checkTimeHard(currentVisit, aPackage);
        }
        if (constraints.contains(Constraints.CHECK_FUEL)) {
            //TODO: fill
            feasible &= checkFuel(currentVisit, aPackage);
        }
        return feasible;
    }

    private boolean checkCapacity(Visit currentVisit, PackageAvailability packageAvailability) {
        Visit v = currentVisit;
        if (packageAvailability.getPackage().getType() == Package.ServiceType.DELIVERY) {
            boolean safe = v.getLoad() + packageAvailability.getPackage().getDemand() <= vehicle.getVehicle().getCapacity();
            while (safe && v.getPrev().isPresent()) {
                v = v.getPrev().get();
                safe = v.getLoad() + packageAvailability.getPackage().getDemand() <= vehicle.getVehicle().getCapacity();
            }
            return safe;
        } else if (packageAvailability.getPackage().getType() == Package.ServiceType.PICKUP) {
            boolean safe = true;
            while (safe && v.getNext().isPresent()) {
                v = v.getNext().get();
                safe = v.getLoad() - packageAvailability.getPackage().getDemand() <= vehicle.getVehicle().getCapacity();
            }
            return safe;
        } else {
            return true;
        }
    }

    private boolean checkTimeHard(Visit currentVisit, PackageAvailability packageAvailability) {
        int travelTime = (int)Math.ceil(currentVisit.getNode().getLocation().distance(packageAvailability.getNode().getLocation()) / vehicle.getVehicle().speed);
        int earliestArrivalTime = currentVisit.getDepartureTime() + travelTime;
        int earliestAvailableTime = packageAvailability.getEarliestAvailableTime(earliestArrivalTime);
        if (earliestAvailableTime == -1) {
            return false;
        }
        return true;
    }

    private boolean checkFuel(Visit currentVisit, PackageAvailability packageAvailability) {
        double totalDistance = currentVisit.getNode().getLocation().distance(packageAvailability.getNode().getLocation()) + lastVisit.getNode().getLocation().distance(packageAvailability.getNode().getLocation());
        double usedFuel = totalDistance / vehicle.getVehicle().getFuelPerDistance();
        if (usedFuel > currentVisit.getRemainingFuel()) {
            return false;
        }
        return true;
    }

    public Vehicle getVehicle() {
        return vehicle.getVehicle();
    }

    public List<Visit> getVisits() {
        return visits.stream()
                .parallel()
                .collect(Collectors.toList());
    }

    public List<Package> getPackages() {
        return visits.stream().map(v -> v.getPackage().map(PackageAvailability::getPackage).orElse(null)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public Visit getFirstVisit() {
        return firstVisit;
    }

    public Visit getLastVisit() {
        return lastVisit;
    }

    @Override
    public String toString() {
        return String.format("Route vehicle: (Vehicle id: %d capacity: %d) maxLoad: %2d totalCost: %6.2f visits: #(\n%s)",
                vehicle.getVehicle().getId(),
                vehicle.getVehicle().getCapacity(),
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

    public RouteOutput toOutput() {
        RouteOutput out = new RouteOutput();
        out.setVisits(visits.stream().map(Visit::toOutput).collect(Collectors.toList()));
        out.setVehicleID(vehicle.getVehicle().getId());
        out.setVehicleAvailableFrom(vehicle.getAvailableFrom());
        return out;
    }
}
