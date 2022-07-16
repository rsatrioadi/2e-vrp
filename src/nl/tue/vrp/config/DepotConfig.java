package nl.tue.vrp.config;

import java.util.List;

public class DepotConfig extends NodeConfig {
    protected List<VehicleConfig> vehicles;

    public DepotConfig() {
        super();
    }

    public List<VehicleConfig> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleConfig> vehicleConfigs) {
        this.vehicles = vehicleConfigs;
    }
}