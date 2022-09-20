package nl.tue.vrp.model.nodes;

import nl.tue.vrp.config.DepotConfig;
import nl.tue.vrp.config.VehicleConfig;
import nl.tue.vrp.model.Vehicle;
import nl.tue.vrp.model.VehicleOwner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Depot extends Node implements VehicleOwner {

    private final List<Vehicle> vehicles = new ArrayList<>();

    public Depot(int id, int x, int y, int serviceTime) {
        super(id, x, y, 0, serviceTime);
    }

    public Depot(DepotConfig config) {
        super(config);
        for (VehicleConfig vehicleConfig: config.getVehicles()) {
            vehicles.add(new Vehicle(vehicleConfig));
        }
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

    @Override
    public String toString() {
        return String.format("Depot id: %2d location: %s vehicleCapacities: #(%s) vehicleSpeeds: #(%s)",
                id,
                String.format("(Point x: %3d y: %3d)", location.x, location.y),
                vehicles.stream().map(v -> String.valueOf(v.getCapacity())).collect(Collectors.joining(" ")),
                vehicles.stream().map(v -> String.valueOf(v.getSpeed())).collect(Collectors.joining(" ")));
    }
}
