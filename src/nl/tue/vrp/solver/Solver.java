package nl.tue.vrp.solver;

import nl.tue.vrp.config.WorldConfig;
import nl.tue.vrp.model.*;
import nl.tue.vrp.model.Package;
import nl.tue.vrp.model.nodes.Customer;
import nl.tue.vrp.model.nodes.Depot;
import nl.tue.vrp.model.nodes.Satellite;
import nl.tue.vrp.output.*;
import nl.tue.vrp.strategy.packageassignment.PackageAssignment;
import nl.tue.vrp.strategy.packagenodeavailability.PackageNodeAvailability;
import nl.tue.vrp.strategy.packagesearch.PackageSearch;
import nl.tue.vrp.strategy.selectvehicle.SelectVehicle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Solver {
    private final Output output;

    private void solve(WorldConfig worldConfig,
               EnumSet<Constraints> constraints,
               PackageAssignment packageAssignmentStrategy,
               SelectVehicle selectVehicleE2Strategy1,
               PackageSearch packageSearchE2Strategy1,
               PackageNodeAvailability packageAvailabilityE2Strategy1,
               SelectVehicle selectVehicleE1Strategy,
               PackageSearch packageSearchE1Strategy
//               PackageNodeAvailability packageAvailabilityE1Strategy,
//               SelectVehicle selectVehicleE2Strategy2,
//               PackageSearch packageSearchE2Strategy2
    ) {
        World world = new World(worldConfig);
        Map<Integer, Integer> customerSatelliteMap = new TreeMap<>();
        world.applyPackageAssignment(packageAssignmentStrategy);

        ArrayList<SatelliteOutput> satelliteV1Outputs = new ArrayList<>();
        for (Satellite sat : world.satellites()) {
            SatelliteOutput satout = sat.toOutput();
            Routes routes = new Routes(sat, selectVehicleE2Strategy1, packageSearchE2Strategy1, constraints);
            satout.setRoutes(routes.toOutput());
            satout.setSatisfied(routes.isSatisfied());
            satelliteV1Outputs.add(satout);

            for (Customer cust : sat.listCustomers()) {
                customerSatelliteMap.put(cust.getId(), sat.getId());
            }

            for (Vehicle vehicle : routes.getVehicles()) {
                int startTime = 0;
                int endTime = Integer.MAX_VALUE;
                List<Route> vehicleRouteList = routes.getRoutesFromVehicle(vehicle);
                vehicleRouteList.sort((a, b) -> b.getFirstVisit().getDepartureTime() - a.getFirstVisit().getDepartureTime());
                for (int i=0;i<vehicleRouteList.size();++i) {
                    Route vehicleRoute = vehicleRouteList.get(i);
                    startTime = vehicleRoute.getFirstVisit().getDepartureTime();
                    Pair<TimeWindow, TimeWindow> a = packageAvailabilityE2Strategy1.getPackagesAvailability(vehicle, vehicleRoute, new TimeWindow(startTime, endTime));
                    TimeWindow deliveryAvailability = a.getFirst();
                    TimeWindow pickupAvailability = a.getSecond();
                    System.out.printf("%s %s", pickupAvailability, deliveryAvailability);
                    for (Package aPackage : vehicleRoute.getPackages()) {
                        nl.tue.vrp.model.PackageAvailability packageAvailability;
                        if (aPackage.isDelivery()) {
                            packageAvailability = new PackageAvailability(aPackage, sat, deliveryAvailability);
                        } else {
                            packageAvailability = new PackageAvailability(aPackage, sat, pickupAvailability);
                        }

                        aPackage.getDepot().addPackage(packageAvailability);
                        aPackage.transferTo(sat);
                    }
                    endTime = startTime - 1;
                }
            }
        }

        for (Satellite sat: world.satellites()) {
            sat.clearPackages();
        }

        ArrayList<DepotOutput> depotOutputs = new ArrayList<>();
        for (Depot depot : world.depots()) {
            DepotOutput depotOutput = depot.toOutput();
//            System.out.println(depot);

            Routes routes = new Routes(depot, selectVehicleE1Strategy, packageSearchE1Strategy, constraints);
            depotOutput.setRoutes(routes.toOutput());
            depotOutput.setSatisfied(routes.isSatisfied());
            depotOutputs.add(depotOutput);
            for (Vehicle vehicle: routes.getVehicles()) {
//                int startTime = 0;
//                int endTime = Integer.MAX_VALUE;
                List<Route> vehicleRouteList = routes.getRoutesFromVehicle(vehicle);
                vehicleRouteList.sort((a, b) -> b.getFirstVisit().getDepartureTime() - a.getFirstVisit().getDepartureTime());
//                for (int i=0;i<vehicleRouteList.size();++i) {
//                    Route vehicleRoute = vehicleRouteList.get(i);
//                    Pair<TimeWindow, TimeWindow> a = packageAvailabilityE1Strategy.getPackagesAvailability(vehicleRoute.getVehicle(), vehicleRoute, new TimeWindow(startTime, endTime));
//                    TimeWindow deliveryAvailability = a.getFirst();
//                    TimeWindow pickupAvailability = a.getSecond();
//                    for (Package aPackage: vehicleRoute.getPackages()) {
//                        aPackage.getSatellite().ifPresent(s -> {
//                            PackageAvailability packageAvailability;
//                            if (aPackage.isDelivery()) {
//                                packageAvailability = new PackageAvailability(aPackage, s, deliveryAvailability);
//                            } else {
//                                packageAvailability = new PackageAvailability(aPackage, s, pickupAvailability);
//                            }
//                            s.addPackage(packageAvailability);
//                        });
//                    }
//                }
            }
//            System.out.println(routes);
        }

//        ArrayList<SatelliteOutput> satelliteV2Outputs = new ArrayList<>();
//        for (Satellite sat : world.satellites()) {
//            SatelliteOutput satout = sat.toOutput();
//            Routes routes = new Routes(sat, selectVehicleE2Strategy2, packageSearchE2Strategy2, constraints);
//            satout.setRoutes(routes.toOutput());
//            satout.setSatisfied(routes.isSatisfied());
//            satelliteV2Outputs.add(satout);
//
////            System.out.println(routes);
//        }

        output.setCustomers(world.customers().stream().map(Customer::toOutput).collect(Collectors.toList()));
        output.setSatellites(satelliteV1Outputs);
//        output.setSatellites(satelliteV2Outputs);
        output.setDepots(depotOutputs);

        double totalDistance = 0;
        int timeFinished = 0;
        double totalFuelUsed = 0;
        boolean satisfied = true;
        Set<Integer> customerIDs = new TreeSet<>();
        for (CustomerOutput cust : output.getCustomers()) {
            customerIDs.add(cust.getId());
            cust.setAssignedSatelliteID(customerSatelliteMap.get(cust.getId()));
        }
        for (SatelliteOutput satellite : output.getSatellites()) {
            for (RouteOutput route : satellite.getRoutes()) {
                VisitOutput lastVisit = route.getVisits().get(route.getVisits().size()-1);
                totalDistance += lastVisit.getAccumulatedCost();
                timeFinished = Math.max(timeFinished, lastVisit.getDepartureTime());

                for (int i=1;i<route.getVisits().size() - 1;++i) {
                    int custID = route.getVisits().get(i).getNodeID();
                    customerIDs.remove(custID);
                }
            }
        }
        for (DepotOutput depot : output.getDepots()) {
            for (RouteOutput route : depot.getRoutes()) {
                VisitOutput lastVisit = route.getVisits().get(route.getVisits().size()-1);
                totalDistance += lastVisit.getAccumulatedCost();
                timeFinished = Math.max(timeFinished, lastVisit.getDepartureTime());
            }
        }
        satisfied = customerIDs.isEmpty();
        output.setTimeFinished(timeFinished);
        output.setTotalDistance(totalDistance);
        output.setSatisfied(satisfied);
        output.setTotalFuelUsed(totalFuelUsed);
    }

    private static boolean isYamlFileName(String str) {
        if (str.endsWith(".yaml") || str.endsWith(".yml")) {
            try {
                Paths.get(str);
            } catch (InvalidPathException | NullPointerException ex) {
                return false;
            }
            return true;
        }
        return false;
    }

    public Solver(
            String input,
            EnumSet<Constraints> constraints,
            PackageAssignment packageAssignmentStrategy,
            SelectVehicle selectVehicleE2Strategy1,
            PackageSearch packageSearchE2Strategy1,
            PackageNodeAvailability packageAvailabilityE2Strategy1,
            SelectVehicle selectVehicleE1Strategy,
            PackageSearch packageSearchE1Strategy
//            PackageNodeAvailability packageAvailabilityE1Strategy,
//            SelectVehicle selectVehicleE2Strategy2,
//            PackageSearch packageSearchE2Strategy2
    ) throws FileNotFoundException {
        Yaml yaml = new Yaml(new Constructor(WorldConfig.class));
        WorldConfig worldConfig = null;
        if (isYamlFileName(input)) {
            InputStream inputStream = new FileInputStream(input);
            if (inputStream == null) throw new IllegalArgumentException("fail to open file");
            worldConfig = yaml.load(inputStream);
        } else {
            worldConfig = yaml.load(input);
        }
        World world = new World(worldConfig);
        world.applyPackageAssignment(packageAssignmentStrategy);
        output = new Output();

        solve(worldConfig,
                constraints,
                packageAssignmentStrategy,
                selectVehicleE2Strategy1,
                packageSearchE2Strategy1,
                packageAvailabilityE2Strategy1,
                selectVehicleE1Strategy,
                packageSearchE1Strategy
//                packageAvailabilityE1Strategy,
//                selectVehicleE2Strategy2,
//                packageSearchE2Strategy2
        );
    }

    public Solver(
            WorldConfig input,
            EnumSet<Constraints> constraints,
            PackageAssignment packageAssignmentStrategy,
            SelectVehicle selectVehicleE2Strategy1,
            PackageSearch packageSearchE2Strategy1,
            PackageNodeAvailability packageAvailabilityE2Strategy1,
            SelectVehicle selectVehicleE1Strategy,
            PackageSearch packageSearchE1Strategy
//            PackageNodeAvailability packageAvailabilityE1Strategy,
//            SelectVehicle selectVehicleE2Strategy2,
//            PackageSearch packageSearchE2Strategy2
    ) {
        output = new Output();
        solve(input,
                constraints,
                packageAssignmentStrategy,
                selectVehicleE2Strategy1,
                packageSearchE2Strategy1,
                packageAvailabilityE2Strategy1,
                selectVehicleE1Strategy,
                packageSearchE1Strategy
//                packageAvailabilityE1Strategy,
//                selectVehicleE2Strategy2,
//                packageSearchE2Strategy2
        );
    }

    public Output getOutput() {
        return output;
    }

    public String getYAMLOutput() {
        Output output = new Output();
        StringWriter writer = new StringWriter();
        Yaml outputYaml = new Yaml();
        outputYaml.dump(output, writer);
        return writer.toString();
    }
}
