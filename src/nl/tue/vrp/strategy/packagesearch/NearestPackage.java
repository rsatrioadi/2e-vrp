package nl.tue.vrp.strategy.packagesearch;

import nl.tue.vrp.model.PackageAvailability;
import nl.tue.vrp.model.Visit;

import java.util.Comparator;
import java.util.List;

public class NearestPackage implements PackageSearch {
    @Override
    public PackageAvailability getNextPackage(Visit lastVisit, List<PackageAvailability> packages) {
        return packages.parallelStream()
                .min(Comparator.comparingDouble(n -> lastVisit.getNode().getLocation().distance(n.getNode().getLocation())))
                .orElse(null);
    }
}
