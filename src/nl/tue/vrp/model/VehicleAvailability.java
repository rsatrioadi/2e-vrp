package nl.tue.vrp.model;

public class VehicleAvailability {
    private final Vehicle vehicle;
    private int availableFrom;
    private double remainingFuel;

    public VehicleAvailability(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.availableFrom = 0;
        this.remainingFuel = vehicle.getFuelCapacity();
    }

    public VehicleAvailability(Vehicle vehicle, int availableFrom) {
        this.vehicle = vehicle;
        this.availableFrom = availableFrom;
        this.remainingFuel = vehicle.getFuelCapacity();
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(int availableFrom) {
        this.availableFrom = availableFrom;
    }

    public double getRemainingFuel() {
        return remainingFuel;
    }

    public void setRemainingFuel(double remainingFuel) {
        this.remainingFuel = remainingFuel;
    }
}
