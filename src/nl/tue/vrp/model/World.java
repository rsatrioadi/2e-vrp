package nl.tue.vrp.model;

import nl.tue.vrp.model.nodes.Customer;
import nl.tue.vrp.model.nodes.Depot;
import nl.tue.vrp.model.nodes.Node;
import nl.tue.vrp.model.nodes.Satellite;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class World {

    private final List<Node> nodes;
    private double[][] distances;

    public World(List<Node> nodes) {
        this.nodes = nodes.stream()
                .parallel()
                .collect(Collectors.toUnmodifiableList());
        calculateDistances();
        findNearestSatellites();
    }

    public World(String fileName, String delimiter, boolean hasHeader) throws IOException {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            if (hasHeader) {
                br.readLine();
            }
            while ((line = br.readLine()) != null) {
                String[] values = line.split(delimiter);
                records.add(values);
            }
            List<Node> nodes = new ArrayList<>();
            List<String[]> depotRecs = records.stream()
                    .parallel()
                    .filter(r -> r[0].equalsIgnoreCase("Depot"))
                    .collect(Collectors.toUnmodifiableList());
            int nextId = 1;
            for (int i = 0; i < depotRecs.size(); i++) {
                var r = depotRecs.get(i);
                Node n = new Depot(i + nextId, Integer.parseInt(r[1]), Integer.parseInt(r[2]), Integer.parseInt(r[3]));
                nodes.add(n);
            }
            List<String[]> satRecs = records.stream()
                    .parallel()
                    .filter(r -> r[0].equalsIgnoreCase("Satellite"))
                    .collect(Collectors.toUnmodifiableList());
            nextId = nodes.size();
            for (int i = 0; i < satRecs.size(); i++) {
                var r = satRecs.get(i);
                Node n = new Satellite(i + nextId, Integer.parseInt(r[1]), Integer.parseInt(r[2]), Integer.parseInt(r[3]));
                nodes.add(n);
            }
            List<String[]> delivRecs = records.stream()
                    .parallel()
                    .filter(r -> r[0].equalsIgnoreCase("Delivery"))
                    .collect(Collectors.toUnmodifiableList());
            nextId = nodes.size();
            for (int i = 0; i < delivRecs.size(); i++) {
                var r = delivRecs.get(i);
                Node n = new Customer(i + nextId,
                        Integer.parseInt(r[1]),
                        Integer.parseInt(r[2]),
                        Integer.parseInt(r[6]),
                        Integer.parseInt(r[3]),
                        Integer.parseInt(r[4]),
                        Integer.parseInt(r[5]),
                        (Depot) nodes.stream()
                                .parallel()
                                .filter(n1 -> n1.getId() == Integer.parseInt(r[7]))
                                .findFirst()
                                .get());
                nodes.add(n);
            }
            List<String[]> pickupRecs = records.stream()
                    .filter(r -> r[0].equalsIgnoreCase("Pickup"))
                    .collect(Collectors.toUnmodifiableList());
            nextId = nodes.size();
            for (int i = 0; i < pickupRecs.size(); i++) {
                var r = pickupRecs.get(i);
                Node n = new Customer(i + nextId,
                        Integer.parseInt(r[1]),
                        Integer.parseInt(r[2]),
                        Integer.parseInt(r[6]) * -1,
                        Integer.parseInt(r[3]),
                        Integer.parseInt(r[4]),
                        Integer.parseInt(r[5]),
                        (Depot) nodes.stream()
                                .parallel()
                                .filter(n1 -> n1.getId() == Integer.parseInt(r[7]))
                                .findFirst()
                                .get());
                nodes.add(n);
            }
            this.nodes = nodes.stream()
                    .parallel()
                    .collect(Collectors.toUnmodifiableList());
            calculateDistances();
            findNearestSatellites();
        }
    }

    public World(String fileName) throws IOException {
        this(fileName, ",", true);
    }

    private void calculateDistances() {
        int length = this.nodes.stream()
                .parallel()
                .map(Node::getId)
                .max(Integer::compare)
                .get() + 1;
        distances = new double[length][length];
        for (Node n1 : this.nodes) {
            for (Node n2 : this.nodes) {
                distances[n1.getId()][n2.getId()] = n1.getLocation().distance(n2.getLocation());
            }
        }
    }

    private void findNearestSatellites() {
        // find each customer's nearest satellite

        for (Customer cust : customers()) {
            // - find nearest satellite
            Satellite nearestSat = satellites().stream()
                    .parallel()
                    .reduce((n1, n2) -> distance(cust, n1) < distance(cust, n2) ? n1 : n2)
                    .get();
            nearestSat.addCustomer(cust);
        }
    }

    public List<Depot> depots() {
        return nodes.stream()
                .parallel()
                .filter(node -> node instanceof Depot)
                .map(node -> (Depot) node)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Satellite> satellites() {
        return nodes.stream()
                .parallel()
                .filter(node -> node instanceof Satellite)
                .map(node -> (Satellite) node)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Customer> customers() {
        return nodes.stream()
                .parallel()
                .filter(node -> node instanceof Customer)
                .map(node -> (Customer) node)
                .collect(Collectors.toUnmodifiableList());
    }

    public double distance(Node n1, Node n2) {
        return distances[n1.getId()][n2.getId()];
    }
}
