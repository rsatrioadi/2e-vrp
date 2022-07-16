package nl.tue.vrp.config;

import java.util.List;

public class ServicesConfig {

    List<ServiceConfig> deliveries;
    List<ServiceConfig> pickups;

    public ServicesConfig() {
    }

    public List<ServiceConfig> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<ServiceConfig> deliveries) {
        this.deliveries = deliveries;
    }

    public List<ServiceConfig> getPickups() {
        return pickups;
    }

    public void setPickups(List<ServiceConfig> pickups) {
        this.pickups = pickups;
    }
}
