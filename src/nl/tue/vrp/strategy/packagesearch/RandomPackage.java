package nl.tue.vrp.strategy.packagesearch;

import nl.tue.vrp.model.Package;
import nl.tue.vrp.model.PackageAvailability;
import nl.tue.vrp.model.Visit;

import java.util.List;
import java.util.Random;

public class RandomPackage implements PackageSearch {
    protected final Random rand;

    RandomPackage() {
        rand = new Random();
    }

    RandomPackage(int seed) {
        rand = new Random(seed);
    }

    @Override
    public PackageAvailability getNextPackage(Visit lastVisit, List<PackageAvailability> packages) {
        if (packages.size() == 0) {
            return null;
        }
        return packages.get(rand.nextInt(packages.size()));
    }
}
