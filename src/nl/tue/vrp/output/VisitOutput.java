package nl.tue.vrp.output;

import nl.tue.vrp.model.PackageAvailability;
import nl.tue.vrp.model.Vehicle;
import nl.tue.vrp.model.nodes.Node;

public class VisitOutput {
    private int vehicleID;
    private int nodeID;
    private int customerID;
    private int demand;
    private int load;
    private double legCost, accumulatedCost;
    private double remainingFuel;
    private int arrivalTime, departureTime;

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public int getNodeID() {
        return nodeID;
    }

    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public double getLegCost() {
        return legCost;
    }

    public void setLegCost(double legCost) {
        this.legCost = legCost;
    }

    public double getAccumulatedCost() {
        return accumulatedCost;
    }

    public void setAccumulatedCost(double accumulatedCost) {
        this.accumulatedCost = accumulatedCost;
    }

    public double getRemainingFuel() {
        return remainingFuel;
    }

    public void setRemainingFuel(double remainingFuel) {
        this.remainingFuel = remainingFuel;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(int departureTime) {
        this.departureTime = departureTime;
    }
}
