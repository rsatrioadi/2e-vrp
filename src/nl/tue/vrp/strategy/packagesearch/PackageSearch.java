package nl.tue.vrp.strategy.packagesearch;

import nl.tue.vrp.model.Package;
import nl.tue.vrp.model.PackageAvailability;
import nl.tue.vrp.model.Visit;

import java.util.List;

public interface PackageSearch {
    public PackageAvailability getNextPackage(Visit lastVisit, List<PackageAvailability> packages);
}
