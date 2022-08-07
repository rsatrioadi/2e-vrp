package nl.tue.vrp.strategy.nodesearch;

import nl.tue.vrp.model.Visit;
import nl.tue.vrp.model.nodes.Node;

import java.util.Comparator;
import java.util.List;

public class NearestNode implements NodeSearch {
    @Override
    public Node getNode(Visit lastVisit, List<Node> nodes) {
        return nodes.parallelStream()
                .min(Comparator.comparingDouble(n -> lastVisit.getNode().getLocation().distance(n.getLocation())))
                .orElse(null);
    }
}
