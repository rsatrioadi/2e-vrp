package nl.tue.vrp.config;

import java.awt.*;
import java.util.List;

public class NodeConfig {
    protected int id;
    protected Point location;
    protected int serviceTime;
    protected TimeWindowConfig[] availabilities;

    protected NodeConfig() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public TimeWindowConfig[] getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(TimeWindowConfig[] availabilities) {
        this.availabilities = availabilities;
    }

    public static class DepotConfig extends NodeConfig {
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

    public static class SatelliteConfig extends DepotConfig {
        public SatelliteConfig() {
            super();
        }
    }

    public static class CustomerConfig extends NodeConfig {
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
}
