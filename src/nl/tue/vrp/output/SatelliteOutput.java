package nl.tue.vrp.output;

import nl.tue.vrp.model.Vehicle;

import java.util.List;

public class SatelliteOutput extends NodeOutput {
    protected boolean satisfied;
    protected List<RouteOutput> routes;
    protected List<VehicleOutput> vehicles;

    public boolean getSatisfied() {
        return satisfied;
    }

    public void setSatisfied(boolean satisfied) {
        this.satisfied = satisfied;
    }

    public List<RouteOutput> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteOutput> routes) {
        this.routes = routes;
    }

    public List<VehicleOutput> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleOutput> vehicles) {
        this.vehicles = vehicles;
    }
}
