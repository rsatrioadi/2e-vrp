package nl.tue.vrp.output;

import nl.tue.vrp.model.Constraints;
import nl.tue.vrp.model.VehicleAvailability;
import nl.tue.vrp.model.Visit;

import java.util.EnumSet;
import java.util.List;

public class RouteOutput {
    private int vehicleID;
    private int vehicleAvailableFrom;
    private List<VisitOutput> visits;

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public int getVehicleAvailableFrom() {
        return vehicleAvailableFrom;
    }

    public void setVehicleAvailableFrom(int vehicleAvailableFrom) {
        this.vehicleAvailableFrom = vehicleAvailableFrom;
    }

    public List<VisitOutput> getVisits() {
        return visits;
    }

    public void setVisits(List<VisitOutput> visits) {
        this.visits = visits;
    }
}
