package nl.tue.vrp.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Node {

    protected final int id;
    protected final Point location;
    protected final int demand;

    protected Node(int id, int x, int y, int demand) {
        this.id = id;
        this.location = new Point(x, y);
        this.demand = demand;
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

    public static class Depot extends Node {

        public Depot(int id, int x, int y, int demand) {
            super(id, x, y, demand);
        }

        @Override
        public String toString() {
            return String.format("Depot[id= %d, location= %s, demand= %d]", id, location, demand);
        }
    }

    public static class Satellite extends Node {

        private final List<Customer> customers = new ArrayList<>();

        public Satellite(int id, int x, int y, int demand) {
            super(id, x, y, demand);
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

        public Customer(int id, int x, int y, int demand) {
            super(id, x, y, demand);
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
