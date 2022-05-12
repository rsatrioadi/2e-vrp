package nl.tue.vrp.model;

import java.util.List;
import java.util.stream.Collectors;

public class World {

    private final List<Node> nodes;
    private final double[][] distances;

    public World(List<Node> nodes) {
        this(nodes.toArray(new Node[0]));
    }

    public World(Node ... nodes) {
        this.nodes = List.of(nodes);
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
