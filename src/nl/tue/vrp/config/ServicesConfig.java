package nl.tue.vrp.config;

import java.util.List;

public class ServicesConfig {
    static public class ServiceConfig {
        protected int customerID;
        protected int depotID;
        protected int demand;
        protected int serviceTime;

        public ServiceConfig() {
        }

        public int getCustomerID() {
            return customerID;
        }

        public void setCustomerID(int customerID) {
            this.customerID = customerID;
        }

        public int getDepotID() {
            return depotID;
        }

        public void setDepotID(int depotID) {
            this.depotID = depotID;
        }

        public int getDemand() {
            return demand;
        }

        public void setDemand(int demand) {
            this.demand = demand;
        }

        public int getServiceTime() {
            return serviceTime;
        }

        public void setServiceTime(int serviceTime) {
            this.serviceTime = serviceTime;
        }
    }

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
