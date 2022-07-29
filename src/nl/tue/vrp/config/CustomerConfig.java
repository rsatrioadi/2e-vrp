package nl.tue.vrp.config;

public class CustomerConfig extends NodeConfig {
    protected int satelliteID;

    protected int demand;

    protected int depotID;

    protected String type;

    public CustomerConfig() {
        super();
    }

    public int getSatelliteID() {
        return satelliteID;
    }

    public void setSatelliteID(int satelliteID) {
        this.satelliteID = satelliteID;
    }

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
}
