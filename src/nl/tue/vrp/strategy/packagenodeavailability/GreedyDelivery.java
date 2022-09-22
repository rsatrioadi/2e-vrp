package nl.tue.vrp.strategy.packagenodeavailability;

import nl.tue.vrp.model.Pair;
import nl.tue.vrp.model.Route;
import nl.tue.vrp.model.TimeWindow;
import nl.tue.vrp.model.Vehicle;

public class GreedyDelivery implements PackageNodeAvailability {
    @Override
    public Pair<TimeWindow, TimeWindow> getPackagesAvailability(Vehicle vehicle, Route route, TimeWindow vehicleAvailability) {
        return null;
    }
}
