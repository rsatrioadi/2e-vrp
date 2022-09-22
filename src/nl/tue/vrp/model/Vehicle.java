package nl.tue.vrp.model;

import nl.tue.vrp.config.VehicleConfig;
import nl.tue.vrp.output.VehicleOutput;

import javax.management.BadBinaryOpValueExpException;

public class Vehicle {

    protected final int id;
    protected final int capacity;
    protected final double speed;
    protected final int fuelCapacity;
    protected final int fuelPerDistance;

    public Vehicle(int id, int capacity, double speed) {
        this.id = id;
        this.capacity = capacity;
        this.speed = speed;
        this.fuelCapacity = 0;
        this.fuelPerDistance = 0;
    }

    public Vehicle(VehicleConfig config) {
        this.id = 0;
        this.capacity = config.getCapacity();
        this.fuelCapacity = config.getFuelCapacity();
        this.speed = config.getAverageSpeed();
        this.fuelPerDistance = config.getFuelPerDistance();
    }

    public Vehicle(VehicleConfig config, int id) {
        this.id = id;
        this.capacity = config.getCapacity();
        this.fuelCapacity = config.getFuelCapacity();
        this.speed = config.getAverageSpeed();
        this.fuelPerDistance = config.getFuelPerDistance();
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getSpeed() {
        return speed;
    }

    public int getFuelCapacity() {
        return fuelCapacity;
    }

    public int getFuelPerDistance() {
        return fuelPerDistance;
    }

    @Override
    public String toString() {
        return String.format("Vehicle id: %d capacity: %2d speed: %2.2f", id, capacity, speed);
    }

    public VehicleOutput toOutput() {
        VehicleOutput out = new VehicleOutput();
        out.setAverageSpeed(speed);
        out.setFuelCapacity(fuelCapacity);
        out.setId(id);
        out.setFuelPerDistance(fuelPerDistance);
        return out;
    }
}
