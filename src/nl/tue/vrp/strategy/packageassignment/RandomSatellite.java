package nl.tue.vrp.strategy.packageassignment;

import nl.tue.vrp.model.PackageAvailability;
import nl.tue.vrp.model.nodes.Satellite;

import java.util.List;
import java.util.Random;

public class RandomSatellite implements PackageAssignment {
    protected final Random rand;

    RandomSatellite() {
        rand = new Random();
    }

    RandomSatellite(int seed) {
        rand = new Random(seed);
    }

    @Override
    public Satellite getAssignedSatellite(PackageAvailability aPackage, List<Satellite> satellites) {
        if (satellites.size() == 0) {
            return null;
        }
        return satellites.get(rand.nextInt(satellites.size()));
    }
}
