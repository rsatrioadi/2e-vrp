package nl.tue.vrp.strategy.nodesearch;

import nl.tue.vrp.model.Visit;
import nl.tue.vrp.model.nodes.Node;

import java.util.List;

public interface NodeSearch {
    public Node getNode(Visit lastVisit, List<Node> nodes);
}
