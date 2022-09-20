package nl.tue.vrp.output;

import java.util.List;

public class Output {
    private double totalDistance;
    private double totalFuelUsed;
    private int timeFinished;
    private boolean satisfied;

    private List<DepotOutput> depots;
    private List<SatelliteOutput> satellites;
    private List<CustomerOutput> customers;
//    private List<VehicleOutput> vehicle;

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public double getTotalFuelUsed() {
        return totalFuelUsed;
    }

    public void setTotalFuelUsed(double totalFuelUsed) {
        this.totalFuelUsed = totalFuelUsed;
    }

    public int getTimeFinished() {
        return timeFinished;
    }

    public void setTimeFinished(int timeFinished) {
        this.timeFinished = timeFinished;
    }

    public boolean getSatisfied() {
        return satisfied;
    }

    public void setSatisfied(boolean satisfied) {
        this.satisfied = satisfied;
    }

    public List<DepotOutput> getDepots() {
        return depots;
    }

    public void setDepots(List<DepotOutput> depots) {
        this.depots = depots;
    }

    public List<SatelliteOutput> getSatellites() {
        return satellites;
    }

    public void setSatellites(List<SatelliteOutput> satellites) {
        this.satellites = satellites;
    }

    public List<CustomerOutput> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerOutput> customers) {
        this.customers = customers;
    }
}
