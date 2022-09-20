package nl.tue.vrp.strategy.nodesearch;

import nl.tue.vrp.model.Visit;
import nl.tue.vrp.model.nodes.Node;

import java.util.List;
import java.util.Random;

public class RandomNode implements NodeSearch{
    protected final Random rand;

    RandomNode() {
        rand = new Random();
    }

    RandomNode(int seed) {
        rand = new Random(seed);
    }
    @Override
    public Node getNode(Visit lastVisit, List<Node> nodes) {
        if (nodes.size() == 0) {
            return null;
        }
        return nodes.get(rand.nextInt(nodes.size()));
    }
}
