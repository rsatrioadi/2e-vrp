package nl.tue.vrp.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Node {

    protected final int id;
    protected final Point location;
    protected final int demand;
    protected final int serviceTime;

    protected Node(int id, int x, int y, int demand, int serviceTime) {
        this.id = id;
        this.location = new Point(x, y);
        this.demand = demand;
        this.serviceTime = serviceTime;
    }

    public int getId() {
        return id;
    }

    public Point getLocation() {
        return location;
    }

    public int getDemand() {
        return demand;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public static class Depot extends Node {

        public Depot(int id, int x, int y, int demand, int serviceTime) {
            super(id, x, y, demand, serviceTime);
        }

        @Override
        public String toString() {
            return String.format("Depot[id= %d, location= %s, demand= %d]", id, location, demand);
        }
    }

    public static class Satellite extends Node {

        private final List<Customer> customers = new ArrayList<>();

        public Satellite(int id, int x, int y, int demand, int serviceTime) {
            super(id, x, y, demand, serviceTime);
        }

        @Override
        public String toString() {
            return String.format("Satellite[id= %d, location= %s, demand= %d]", id, location, demand);
        }

        public void addCustomer(Customer customer) {
            customers.add(customer);
        }

        public List<Node> listCustomers() {
            return customers.stream().collect(Collectors.toUnmodifiableList());
        }

        public List<Node> listNodes() {
            List<Node> nodes = new ArrayList<>();
            nodes.add(this);
            nodes.addAll(listCustomers());
            return nodes.stream().collect(Collectors.toUnmodifiableList());
        }
    }

    public static class Customer extends Node {

        private final int early;
        private final int latest;
        private final Depot depot;

        public Customer(int id, int x, int y, int demand, int serviceTime, int early, int latest, Depot depot) {
            super(id, x, y, demand, serviceTime);
            this.early = early;
            this.latest = latest;
            this.depot = depot;
        }

        public int getEarly() {
            return early;
        }

        public int getLatest() {
            return latest;
        }

        public Depot getDepot() {
            return depot;
        }

        @Override
        public String toString() {
            return String.format("Customer[id= %d, location= %s, demand= %d]", id, location, demand);
        }
    }

    @Override
    public String toString() {
        return String.format("Node[id= %d, location= %s, demand= %d]", id, location, demand);
    }
}
