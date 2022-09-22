package nl.tue.vrp.strategy.packagenodeavailability;

import nl.tue.vrp.model.*;

public interface PackageNodeAvailability {
    /**
     * @param vehicle the vehicle used for the routes
     * @param route the route of the vehicle
     * @param vehicleAvailability the time range for vehicle to serve the route
     * @return return pair of the TimeWindow. The first of pair denotes the availability of the delivery packages in the satellite.
     *         The second is the availability of the pickup packages in the satellite.
     */
    public Pair<TimeWindow, TimeWindow> getPackagesAvailability(Vehicle vehicle, Route route, TimeWindow vehicleAvailability);
}
