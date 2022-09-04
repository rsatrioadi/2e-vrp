package nl.tue.vrp.output;

public class VehicleOutput {
    private int id;
    private double averageSpeed;
    protected int fuelPerDistance;
    protected int fuelCapacity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public int getFuelPerDistance() {
        return fuelPerDistance;
    }

    public void setFuelPerDistance(int fuelPerDistance) {
        this.fuelPerDistance = fuelPerDistance;
    }

    public int getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(int fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }
}
