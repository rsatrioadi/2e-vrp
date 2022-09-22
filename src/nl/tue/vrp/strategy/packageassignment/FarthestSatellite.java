package nl.tue.vrp.strategy.packageassignment;

import nl.tue.vrp.model.PackageAvailability;
import nl.tue.vrp.model.nodes.Node;
import nl.tue.vrp.model.nodes.Satellite;

import java.util.List;

public class FarthestSatellite implements PackageAssignment {
    private static Satellite farthestSatellite(Node node, Satellite s1, Satellite s2) {
        double d1 = s1.getLocation().distance(node.getLocation());
        double d2 = s2.getLocation().distance(node.getLocation());
        return d1 > d2 ? s1 : s2;
    }

    @Override
    public Satellite getAssignedSatellite(PackageAvailability aPackage, List<Satellite> satellites) {
        return satellites.stream().parallel().reduce((s1, s2) -> farthestSatellite(aPackage.getNode(), s1, s2)).orElse(null);
    }
}
