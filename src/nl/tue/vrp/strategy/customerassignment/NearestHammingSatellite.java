package nl.tue.vrp.strategy.customerassignment;

import nl.tue.vrp.model.nodes.Customer;
import nl.tue.vrp.model.nodes.Satellite;

import java.awt.*;
import java.util.List;

public class NearestHammingSatellite implements CustomerAssignment {
    private static double distance(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private static Satellite nearestSatellite(Customer cust, Satellite s1, Satellite s2) {
        double d1 = distance(s1.getLocation(), cust.getLocation());
        double d2 = distance(s2.getLocation(), cust.getLocation());
        return d1 < d2 ? s1 : s2;
    }

    @Override
    public Satellite getAssignedSatellite(Customer customer, List<Satellite> satellites) {
        // TODO: check stream parallel is feasible on low number of satellites
        return satellites.stream().parallel().reduce((s1, s2) -> nearestSatellite(customer, s1, s2)).orElse(null);
    }
}
