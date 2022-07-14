package nl.tue.vrp.model;

public class Vehicle {

    protected final int id;
    protected final int capacity;
    protected final double speed;

    public Vehicle(int id, int capacity, double speed) {
        this.id = id;
        this.capacity = capacity;
        this.speed = speed;
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

    @Override
    public String toString() {
        return String.format("Vehicle id: %d capacity: %2d speed: %2.2f", id, capacity, speed);
    }
}
