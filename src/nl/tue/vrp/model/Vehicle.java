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

    @Override
    public String toString() {
        return String.format("Vehicle[id= %d, capacity= %d]", id, capacity);
    }
}
