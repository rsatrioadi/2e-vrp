package nl.tue.vrp.strategy.selectvehicle;

import nl.tue.vrp.model.Vehicle;
import nl.tue.vrp.model.VehicleAvailability;

import java.util.List;
import java.util.Random;

public class RandomVehicle implements SelectVehicle {
    protected final Random rand;

    RandomVehicle() {
        rand = new Random();
    }

    RandomVehicle(int seed) {
        rand = new Random(seed);
    }

    @Override
    public VehicleAvailability getVehicle(List<VehicleAvailability> vehicles) {
        if (vehicles.size() == 0) {
            return null;
        }
        return vehicles.get(rand.nextInt(vehicles.size()));
    }
}
