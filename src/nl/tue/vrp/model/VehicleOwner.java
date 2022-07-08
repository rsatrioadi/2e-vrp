package nl.tue.vrp.model;

import java.util.Collection;
import java.util.List;

public interface VehicleOwner {
    void addVehicle(Vehicle v);

    void addVehicles(Collection<Vehicle> vs);

    List<Vehicle> getVehicles();
}
