package nl.tue.vrp.config;

public class VehicleConfig {
    protected int capacity;
    protected int fuelCapacity;
    protected int averageSpeed;
    protected int fuelPerDistance;
    protected int count;

    public VehicleConfig() {
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(int fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    public int getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(int averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public int getFuelPerDistance() {
        return fuelPerDistance;
    }

    public void setFuelPerDistance(int fuelPerDistance) {
        this.fuelPerDistance = fuelPerDistance;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
