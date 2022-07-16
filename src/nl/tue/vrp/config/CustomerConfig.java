package nl.tue.vrp.config;

public class CustomerConfig extends NodeConfig {
    protected int satelliteID;

    public CustomerConfig() {
        super();
    }

    public int getSatelliteID() {
        return satelliteID;
    }

    public void setSatelliteID(int satelliteID) {
        this.satelliteID = satelliteID;
    }
}
