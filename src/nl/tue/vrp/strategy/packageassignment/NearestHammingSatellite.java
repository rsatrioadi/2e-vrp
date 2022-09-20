package nl.tue.vrp.strategy.packageassignment;

import nl.tue.vrp.model.Package;
import nl.tue.vrp.model.PackageAvailability;
import nl.tue.vrp.model.nodes.Node;
import nl.tue.vrp.model.nodes.Satellite;

import java.awt.*;
import java.util.List;

public class NearestHammingSatellite implements PackageAssignment {
    private static double distance(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private static Satellite nearestSatellite(Node node, Satellite s1, Satellite s2) {
        double d1 = distance(s1.getLocation(), node.getLocation());
        double d2 = distance(s2.getLocation(), node.getLocation());
        return d1 < d2 ? s1 : s2;
    }

    @Override
    public Satellite getAssignedSatellite(PackageAvailability aPackage, List<Satellite> satellites) {
        // TODO: check stream parallel is feasible on low number of satellites
        return satellites.stream().parallel().reduce((s1, s2) -> nearestSatellite(aPackage.getNode(), s1, s2)).orElse(null);
    }
}
