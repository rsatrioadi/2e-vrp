package nl.tue.vrp.model.nodes;

import nl.tue.vrp.config.DepotConfig;
import nl.tue.vrp.config.VehicleConfig;
import nl.tue.vrp.model.*;
import nl.tue.vrp.model.Package;
import nl.tue.vrp.output.DepotOutput;
import nl.tue.vrp.output.TimeWindowOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Depot extends Node implements VehicleOwner {

    private final List<Vehicle> vehicles = new ArrayList<>();
    private final List<PackageAvailability> packages = new ArrayList<>();

    public Depot(int id, int x, int y, int serviceTime) {
        super(id, x, y, 0, serviceTime);
    }

    public Depot(DepotConfig config) {
        super(config);
//        for (VehicleConfig vehicleConfig: config.getVehicles()) {
//            vehicles.add(new Vehicle(vehicleConfig));
//        }
    }

    public void addVehicle(Vehicle v) {
        vehicles.add(v);
    }

    public void addVehicles(Collection<Vehicle> vs) {
        vehicles.addAll(vs);
    }

    public List<Vehicle> getVehicles() {
        return vehicles.stream().collect(Collectors.toUnmodifiableList());
    }

    public void addPackage(PackageAvailability aPackage) {
        packages.add(aPackage);
    }

    public List<Customer> listCustomers() {
        return packages.stream()
                .parallel().map(PackageAvailability::getPackage).map(Package::getCustomer)
                .collect(Collectors.toList());
    }

    public List<PackageAvailability> listPackagesAvailability() {
        return packages.stream()
                .parallel()
                .collect(Collectors.toList());
    }

    public void clearPackages() {
        packages.clear();
    }

    public void removePackages() {
        packages.clear();
    }

    @Override
    public String toString() {
        return String.format("Depot id: %2d location: %s vehicleCapacities: #(%s) vehicleSpeeds: #(%s) packages(%d): %s",
                id,
                String.format("(Point x: %3d y: %3d)", location.x, location.y),
                vehicles.stream().map(v -> String.valueOf(v.getCapacity())).collect(Collectors.joining(" ")),
                vehicles.stream().map(v -> String.valueOf(v.getSpeed())).collect(Collectors.joining(" ")),
                packages.size(),
                packages.stream().map(PackageAvailability::toString).collect(Collectors.joining("\n")));
    }

    public DepotOutput toOutput() {
        DepotOutput out = new DepotOutput();
        out.setServiceTime(serviceTime);
        out.setLocation(location);
        out.setVehicles(vehicles.stream().map(Vehicle::toOutput).collect(Collectors.toList()));
        out.setId(id);
        if (availability != null) {
            out.setAvailability(availability.toOutput());
        }
        return out;
    }
}
