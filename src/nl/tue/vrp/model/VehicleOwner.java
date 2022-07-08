package nl.tue.vrp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface VehicleOwner {
    void addVehicle(Vehicle v);
    void addVehicles(Collection<Vehicle> vs);
    List<Vehicle> getVehicles();
}
