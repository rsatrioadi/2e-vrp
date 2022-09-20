package nl.tue.vrp.strategy.selectvehicle;

import nl.tue.vrp.model.Vehicle;
import nl.tue.vrp.model.VehicleAvailability;

import java.util.List;

public interface SelectVehicle {
    VehicleAvailability getVehicle(List<VehicleAvailability> vehicles);
}
