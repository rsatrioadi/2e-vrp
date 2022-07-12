package nl.tue.vrp.model.nodes;

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
        return String.format("Depot[id= %d, location= %s, vehicleCapacities= %s, vehicleSpeeds= %s]",
                id,
                location,
                vehicles.stream()
                        .parallel()
                        .map(Vehicle::getCapacity)
                        .collect(Collectors.toUnmodifiableList()),
                vehicles.stream()
                        .parallel()
                        .map(Vehicle::getSpeed)
                        .collect(Collectors.toUnmodifiableList()));
    }
}
