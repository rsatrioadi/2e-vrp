package nl.tue.vrp.model.nodes;

import nl.tue.vrp.config.SatelliteConfig;
import nl.tue.vrp.model.*;
import nl.tue.vrp.model.Package;
import nl.tue.vrp.output.SatelliteOutput;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Satellite extends Node implements VehicleOwner {

    private final List<PackageAvailability> packages = new ArrayList<>();
    private final List<Vehicle> vehicles = new ArrayList<>();

    public Satellite(int id, int x, int y, int serviceTime) {
        super(id, x, y, 0, serviceTime);
    }

    public Satellite(SatelliteConfig config) {
        super(config);
    }

    public void addVehicle(Vehicle v) {
        vehicles.add(v);
    }

    public void addVehicles(Collection<Vehicle> vs) {
        vehicles.addAll(vs);
    }

    public List<Vehicle> getVehicles() {
        return vehicles.stream()
                .parallel()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String toString() {
        return String.format("Satellite id: %2d location: %s vehicleCapacities: #(%s) vehicleSpeeds: #(%s), packages(%d):\n%s",
                id,
                String.format("(Point x: %3d y: %3d)", location.x, location.y),
                vehicles.stream()
                        .parallel()
                        .map(v -> String.valueOf(v.getCapacity()))
                        .collect(Collectors.joining(" ")),
                vehicles.stream()
                        .parallel()
                        .map(v -> String.valueOf(v.getSpeed()))
                        .collect(Collectors.joining(" ")),
                packages.size(),
                packages.stream().map(PackageAvailability::toString).collect(Collectors.joining("\n")));
    }

    public void addPackage(PackageAvailability aPackage) {
        packages.add(aPackage);
    }

    public List<Customer> listCustomers() {
        return packages.stream()
                .parallel().map(PackageAvailability::getPackage).map(Package::getCustomer)
                .collect(Collectors.toList());
    }

    public List<PackageAvailability> listPackages() {
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

    public List<Node> listNodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(this);
        nodes.addAll(listCustomers());
        return nodes.stream()
                .parallel()
                .collect(Collectors.toList());
    }

    public SatelliteOutput toOutput() {
        SatelliteOutput out = new SatelliteOutput();
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
