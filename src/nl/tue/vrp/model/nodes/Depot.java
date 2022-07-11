package nl.tue.vrp.model.nodes;

import nl.tue.vrp.model.Vehicle;
import nl.tue.vrp.model.VehicleOwner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Depot extends Node implements VehicleOwner {

    private final List<Vehicle> vehicles = new ArrayList<>();

    public Depot(int id, int x, int y, int demand, int serviceTime) {
        super(id, x, y, demand, serviceTime);
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
        return String.format("Depot[id= %d, location= %s, vehicleCapacities= %s, vehicleSpeeds= %s]",
                id,
                location,
                vehicles.stream().map(Vehicle::getCapacity).collect(Collectors.toUnmodifiableList()),
                vehicles.stream().map(Vehicle::getSpeed).collect(Collectors.toUnmodifiableList()));
    }
}
