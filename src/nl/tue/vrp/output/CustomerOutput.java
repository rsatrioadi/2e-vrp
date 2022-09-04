package nl.tue.vrp.output;

import java.awt.Point;

public class CustomerOutput extends NodeOutput {
    protected int demand;
    protected int depotID;
    protected String type;
    protected int assignedSatelliteID;

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public int getDepotID() {
        return depotID;
    }

    public void setDepotID(int depotID) {
        this.depotID = depotID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAssignedSatelliteID() {
        return assignedSatelliteID;
    }

    public void setAssignedSatelliteID(int assignedSatelliteID) {
        this.assignedSatelliteID = assignedSatelliteID;
    }
}
