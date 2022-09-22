package nl.tue.vrp.strategy.packagenodeavailability;

import nl.tue.vrp.model.*;
import nl.tue.vrp.model.nodes.Node;

public class SimpleStrategy implements PackageNodeAvailability {
    private static int travelTime(Vehicle v, Node a, Node b) {
        return (int) Math.ceil(a.getLocation().distance(b.getLocation()) / v.getSpeed());
    }

    @Override
    public Pair<TimeWindow, TimeWindow> getPackagesAvailability(Vehicle vehicle, Route route, TimeWindow vehicleAvailability) {
        Visit firstVisit = route.getFirstVisit();
        Visit lastVisit = route.getLastVisit();
        Visit secondVisit = firstVisit.getNext().orElse(null);
        TimeWindow deliveryPackageAvailability = null;
        if (secondVisit == null) {
            deliveryPackageAvailability = new TimeWindow(0, firstVisit.getDepartureTime());
        } else {
            deliveryPackageAvailability = new TimeWindow(0, secondVisit.getArrivalTime() - travelTime(vehicle, firstVisit.getNode(), secondVisit.getNode()));
        }

        TimeWindow pickupPackageAvailability = new TimeWindow(lastVisit.getArrivalTime(), Integer.MAX_VALUE);

        return new Pair<>(deliveryPackageAvailability, pickupPackageAvailability);
    }
}
