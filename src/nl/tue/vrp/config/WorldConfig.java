package nl.tue.vrp.config;

import java.util.List;

public class WorldConfig {
    protected List<DepotConfig> depots;
    protected List<SatelliteConfig> satellites;
    protected List<CustomerConfig> customers;
    protected ServicesConfig services;

    public WorldConfig() {
    }

    public List<DepotConfig> getDepots() {
        return depots;
    }

    public void setDepots(List<DepotConfig> depotConfigs) {
        this.depots = depotConfigs;
    }

    public List<SatelliteConfig> getSatellites() {
        return satellites;
    }

    public void setSatellites(List<SatelliteConfig> satelliteConfigs) {
        this.satellites = satelliteConfigs;
    }

    public List<CustomerConfig> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerConfig> customerConfigs) {
        this.customers = customerConfigs;
    }

    public ServicesConfig getServices() {
        return services;
    }

    public void setServices(ServicesConfig servicesConfig) {
        this.services = servicesConfig;
    }
}
