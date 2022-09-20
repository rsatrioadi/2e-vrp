package nl.tue.vrp.strategy.packageassignment;

import nl.tue.vrp.model.PackageAvailability;
import nl.tue.vrp.model.nodes.Depot;
import nl.tue.vrp.model.nodes.Satellite;

import java.util.List;

public interface PackageAssignment {
    Satellite getAssignedSatellite(PackageAvailability aPackage, List<Satellite> satellites);
}
