package nl.tue.vrp.model;

public class Vehicle {

    protected final int id;
    protected final int capacity;

    public Vehicle(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }
}
