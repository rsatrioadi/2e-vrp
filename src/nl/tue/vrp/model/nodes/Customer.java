package nl.tue.vrp.model.nodes;

import nl.tue.vrp.config.CustomerConfig;
import nl.tue.vrp.model.Package;
import nl.tue.vrp.model.PackageAvailability;
import nl.tue.vrp.model.TimeWindow;
import nl.tue.vrp.output.CustomerOutput;
import nl.tue.vrp.output.TimeWindowOutput;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Customer extends Node {
    public Package aPackage;

    public Customer(int id, int x, int y, int demand, int serviceTime, int earliest, int latest, Depot depot) {
        super(id, x, y, demand, serviceTime);
        this.availability = new TimeWindow(earliest, latest);
        this.aPackage = new Package(Package.ServiceType.DELIVERY, demand, this, depot);
    }

    public Customer(CustomerConfig config, Depot depot) {
        super(config);
        this.aPackage = new Package(Package.typeFromString(config.getType()), config.getDemand(), this, depot);
    }

    @Override
    public String toString() {
        return String.format("Customer id: %2d location: %s package: (%s), availability: %s",
                id,
                String.format("(Point x: %3d y: %3d)", location.x, location.y),
                aPackage,
                availability);
    }

    public CustomerOutput toOutput() {
        CustomerOutput out = new CustomerOutput();
        out.setId(id);
        out.setAvailability(availability.toOutput());
        out.setType(aPackage.getType().toString());
        out.setServiceTime(serviceTime);
        out.setLocation(location);
        out.setDepotID(aPackage.getDepot().getId());
        out.setDemand(aPackage.getDemand());
        return out;
    }

    public Package getPackage() {
        return aPackage;
    }

    public PackageAvailability getPackageAvailability() {
        TimeWindow timeWindow = null;
        if (availability != null) timeWindow = availability;
        else timeWindow = new TimeWindow(0, Integer.MAX_VALUE);
        return new PackageAvailability(aPackage, this, timeWindow);
    }
}
