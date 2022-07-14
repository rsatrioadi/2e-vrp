package nl.tue.vrp.config;

import java.util.List;

public class WorldConfig {
    protected List<NodeConfig.DepotConfig> depots;
    protected List<NodeConfig.SatelliteConfig> satellites;
    protected List<NodeConfig.CustomerConfig> customers;
    protected ServicesConfig services;

    public WorldConfig() {
    }

    public List<NodeConfig.DepotConfig> getDepots() {
        return depots;
    }

    public void setDepots(List<NodeConfig.DepotConfig> depotConfigs) {
        this.depots = depotConfigs;
    }

    public List<NodeConfig.SatelliteConfig> getSatellites() {
        return satellites;
    }

    public void setSatellites(List<NodeConfig.SatelliteConfig> satelliteConfigs) {
        this.satellites = satelliteConfigs;
    }

    public List<NodeConfig.CustomerConfig> getCustomers() {
        return customers;
    }

    public void setCustomers(List<NodeConfig.CustomerConfig> customerConfigs) {
        this.customers = customerConfigs;
    }

    public ServicesConfig getServices() {
        return services;
    }

    public void setServices(ServicesConfig servicesConfig) {
        this.services = servicesConfig;
    }
}
