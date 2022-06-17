package nl.tue.vrp.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class World {

    private final List<Node> nodes;
    private final double[][] distances;

    public World(List<Node> nodes) {
        this.nodes = nodes.stream().collect(Collectors.toUnmodifiableList());
        int length = this.nodes.stream()
                .map(Node::getId)
                .max(Integer::compare)
                .get()+1;
        distances = new double[length][length];
        for (Node n1: this.nodes) {
            for (Node n2: this.nodes) {
                distances[n1.getId()][n2.getId()] = n1.getLocation().distance(n2.getLocation());
            }
        }
    }

    public World(String fileName, String delimiter, boolean hasHeader) throws IOException {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            if (hasHeader) { br.readLine(); }
            while ((line = br.readLine()) != null) {
                String[] values = line.split(delimiter);
                records.add(values);
            }
            List<Node> nodes = new ArrayList<>();
            List<String[]> depotRecs = records.stream()
                    .filter(r -> r[0].equalsIgnoreCase("Depot"))
                    .collect(Collectors.toUnmodifiableList());
            int nextId = 1;
            for (int i=0; i<depotRecs.size(); i++) {
                var r = depotRecs.get(i);
                Node n = new Node.Depot(i+nextId,Integer.parseInt(r[1]),Integer.parseInt(r[2]),0,Integer.parseInt(r[3]));
                nodes.add(n);
            }
            List<String[]> satRecs = records.stream()
                    .filter(r -> r[0].equalsIgnoreCase("Satellite"))
                    .collect(Collectors.toUnmodifiableList());
            nextId = nodes.size();
            for (int i=0; i<satRecs.size(); i++) {
                var r = satRecs.get(i);
                Node n = new Node.Satellite(i+nextId,Integer.parseInt(r[1]),Integer.parseInt(r[2]),0,Integer.parseInt(r[3]));
                nodes.add(n);
            }
            List<String[]> delivRecs = records.stream()
                    .filter(r -> r[0].equalsIgnoreCase("Delivery"))
                    .collect(Collectors.toUnmodifiableList());
            nextId = nodes.size();
            for (int i=0; i<delivRecs.size(); i++) {
                var r = delivRecs.get(i);
                Node n = new Node.Customer(i+nextId,
                        Integer.parseInt(r[1]),
                        Integer.parseInt(r[2]),
                        Integer.parseInt(r[6]),
                        Integer.parseInt(r[3]),
                        Integer.parseInt(r[4]),
                        Integer.parseInt(r[5]),
                        (Node.Depot) nodes.stream()
                                .filter(n1 -> n1.getId() == Integer.parseInt(r[7]))
                                .findFirst()
                                .get());
                nodes.add(n);
            }
            List<String[]> pickupRecs = records.stream()
                    .filter(r -> r[0].equalsIgnoreCase("Pickup"))
                    .collect(Collectors.toUnmodifiableList());
            nextId = nodes.size();
            for (int i=0; i<pickupRecs.size(); i++) {
                var r = pickupRecs.get(i);
                Node n = new Node.Customer(i+nextId,
                        Integer.parseInt(r[1]),
                        Integer.parseInt(r[2]),
                        Integer.parseInt(r[6]) * -1,
                        Integer.parseInt(r[3]),
                        Integer.parseInt(r[4]),
                        Integer.parseInt(r[5]),
                        (Node.Depot) nodes.stream()
                                .filter(n1 -> n1.getId() == Integer.parseInt(r[7]))
                                .findFirst()
                                .get());
                nodes.add(n);
            }
            this.nodes = nodes.stream().collect(Collectors.toUnmodifiableList());
            int length = this.nodes.stream()
                    .map(Node::getId)
                    .max(Integer::compare)
                    .get()+1;
            distances = new double[length][length];
            for (Node n1: this.nodes) {
                for (Node n2: this.nodes) {
                    distances[n1.getId()][n2.getId()] = n1.getLocation().distance(n2.getLocation());
                }
            }
        }
    }

    public World(String fileName) throws IOException {
        this(fileName, ",", true);
    }

    public List<Node> depots() {
        return nodes.stream()
                .parallel()
                .filter(node -> node instanceof Node.Depot)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Node> satellites() {
        return nodes.stream()
                .parallel()
                .filter(node -> node instanceof Node.Satellite)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Node> customers() {
        return nodes.stream()
                .parallel()
                .filter(node -> node instanceof Node.Customer)
                .collect(Collectors.toUnmodifiableList());
    }

    public double distance(Node n1, Node n2) {
        return distances[n1.getId()][n2.getId()];
    }
}
