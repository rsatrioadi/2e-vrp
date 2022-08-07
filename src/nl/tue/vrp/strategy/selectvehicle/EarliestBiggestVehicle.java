package nl.tue.vrp.strategy.selectvehicle;

import nl.tue.vrp.model.VehicleAvailability;

import java.util.List;

public class EarliestBiggestVehicle implements SelectVehicle {
    private static VehicleAvailability earliestBiggestVehicle(VehicleAvailability a, VehicleAvailability b) {
        if (a.getAvailableFrom() != b.getAvailableFrom())  {
            return a.getAvailableFrom() < b.getAvailableFrom() ? a : b;
        }
        return a.getVehicle().getCapacity() > b.getVehicle().getCapacity() ? a : b;
    }

    @Override
    public VehicleAvailability getVehicle(List<VehicleAvailability> vehicles) {
        return vehicles.stream().parallel().reduce(EarliestBiggestVehicle::earliestBiggestVehicle).orElse(null);
    }
}
