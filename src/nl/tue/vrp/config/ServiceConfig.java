package nl.tue.vrp.config;


public class ServiceConfig {
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