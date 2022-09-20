package nl.tue.vrp.model;

import nl.tue.vrp.config.*;
import nl.tue.vrp.model.nodes.Customer;
import nl.tue.vrp.model.nodes.Depot;
import nl.tue.vrp.model.nodes.Node;
import nl.tue.vrp.model.nodes.Satellite;
import nl.tue.vrp.strategy.customerassignment.CustomerAssignment;
import nl.tue.vrp.strategy.packageassignment.PackageAssignment;
import nl.tue.vrp.util.IDManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class World {

    private final List<Node> nodes;
    private double[][] distances;

    public World(List<Node> nodes) {
        this.nodes = nodes.stream()
                .parallel()
                .collect(Collectors.toUnmodifiableList());
        calculateDistances();
        findNearestSatellites();
    }

    public World(String fileName, String delimiter, boolean hasHeader) throws IOException {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            if (hasHeader) {
                br.readLine();
            }
            while ((line = br.readLine()) != null) {
                String[] values = line.split(delimiter);
                records.add(values);
            }
            List<Node> nodes = new ArrayList<>();
            List<Package> packages = new ArrayList<>();
            List<String[]> depotRecs = records.stream()
                    .parallel()
                    .filter(r -> r[0].equalsIgnoreCase("Depot"))
                    .collect(Collectors.toUnmodifiableList());
            int nextId = 1;
            for (int i = 0; i < depotRecs.size(); i++) {
                var r = depotRecs.get(i);
                Node n = new Depot(i + nextId, Integer.parseInt(r[1]), Integer.parseInt(r[2]), Integer.parseInt(r[3]));
                nodes.add(n);
            }
            List<String[]> satRecs = records.stream()
                    .parallel()
                    .filter(r -> r[0].equalsIgnoreCase("Satellite"))
                    .collect(Collectors.toUnmodifiableList());
            nextId = nodes.size();
            for (int i = 0; i < satRecs.size(); i++) {
                var r = satRecs.get(i);
                Node n = new Satellite(i + nextId, Integer.parseInt(r[1]), Integer.parseInt(r[2]), Integer.parseInt(r[3]));
                nodes.add(n);
            }
            List<String[]> delivRecs = records.stream()
                    .parallel()
                    .filter(r -> r[0].equalsIgnoreCase("Delivery"))
                    .collect(Collectors.toUnmodifiableList());
            nextId = nodes.size();
            for (int i = 0; i < delivRecs.size(); i++) {
                var r = delivRecs.get(i);
                Node n = new Customer(i + nextId,
                        Integer.parseInt(r[1]),
                        Integer.parseInt(r[2]),
                        Integer.parseInt(r[6]),
                        Integer.parseInt(r[3]),
                        Integer.parseInt(r[4]),
                        Integer.parseInt(r[5]),
                        (Depot) nodes.stream()
                                .parallel()
                                .filter(n1 -> n1.getId() == Integer.parseInt(r[7]))
                                .findFirst()
                                .get());
                nodes.add(n);
            }
            List<String[]> pickupRecs = records.stream()
                    .filter(r -> r[0].equalsIgnoreCase("Pickup"))
                    .collect(Collectors.toUnmodifiableList());
            nextId = nodes.size();
            for (int i = 0; i < pickupRecs.size(); i++) {
                var r = pickupRecs.get(i);
                Node n = new Customer(i + nextId,
                        Integer.parseInt(r[1]),
                        Integer.parseInt(r[2]),
                        Integer.parseInt(r[6]) * -1,
                        Integer.parseInt(r[3]),
                        Integer.parseInt(r[4]),
                        Integer.parseInt(r[5]),
                        (Depot) nodes.stream()
                                .parallel()
                                .filter(n1 -> n1.getId() == Integer.parseInt(r[7]))
                                .findFirst()
                                .get());
                nodes.add(n);
            }
            this.nodes = nodes.stream()
                    .parallel()
                    .collect(Collectors.toUnmodifiableList());
            calculateDistances();
            findNearestSatellites();
        }
    }

    public World(String fileName) throws IOException {
        this(fileName, ",", true);
    }

    public World(WorldConfig config) throws IllegalArgumentException {
        nodes = new ArrayList<>();
        for (DepotConfig depotConfig : config.getDepots()) {
            if (depotConfig.getId() == 0) {
                throw new IllegalArgumentException("depot id is empty");
            }
            Depot depot = new Depot(depotConfig);
            if (!IDManager.getInstance().registerID(depot.getId(), depot)) {
                throw new IllegalArgumentException(String.format("duplicate id (%d) in depot", depotConfig.getId()));
            }
            nodes.add(depot);
        }
        for (SatelliteConfig satelliteConfig : config.getSatellites()) {
            if (satelliteConfig.getId() == 0) {
                throw new IllegalArgumentException("satellite id is empty");
            }
            Satellite satellite = new Satellite(satelliteConfig);
            if (!IDManager.getInstance().registerID(satellite.getId(), satellite)) {
                throw new IllegalArgumentException(String.format("duplicate id (%d) in satellite", satelliteConfig.getId()));
            }
            nodes.add(satellite);
        }
        for (CustomerConfig customerConfig : config.getCustomers()) {
            if (customerConfig.getId() == 0) {
                throw new IllegalArgumentException("customer id is empty");
            }
            Customer customer = new Customer(customerConfig, (Depot) IDManager.getInstance().getObject(customerConfig.getDepotID()));
            if (!IDManager.getInstance().registerID(customer.getId(), customer)) {
                throw new IllegalArgumentException(String.format("duplicate id (%d) in satellite", customerConfig.getId()));
            }
            nodes.add(customer);
        }


        // Add vehicles after adding all nodes to avoid duplicate id
        for (DepotConfig depotConfig : config.getDepots()) {
            Depot depot = (Depot) IDManager.getInstance().getObject(depotConfig.getId());
            if (depot == null) {
                throw new IllegalStateException("depot id not found when building world");
            }
            for (VehicleConfig vehicleConfig : depotConfig.getVehicles()) {
                IntStream.rangeClosed(1, vehicleConfig.getCount()).forEach(value -> {
                    Vehicle vehicle = new Vehicle(vehicleConfig,
                            IDManager.getInstance().getNewID());
                    depot.addVehicle(vehicle);
                });
            }
        }

        for (SatelliteConfig satelliteConfig : config.getSatellites()) {
            Satellite satellite = (Satellite) IDManager.getInstance().getObject(satelliteConfig.getId());
            if (satellite == null) {
                throw new IllegalStateException("satellite id not found when building world");
            }
            for (VehicleConfig vehicleConfig : satelliteConfig.getVehicles()) {
                IntStream.rangeClosed(1, vehicleConfig.getCount()).forEach(value -> {
                    Vehicle vehicle = new Vehicle(vehicleConfig,
                            IDManager.getInstance().getNewID());
                    IDManager.getInstance().assignExistingID(vehicle.getId(), vehicle);
                    satellite.addVehicle(vehicle);
                });
            }
        }

        calculateDistances();
    }

    private void calculateDistances() {
        int length = this.nodes.stream()
                .parallel()
                .map(Node::getId)
                .max(Integer::compare)
                .get() + 1;
        distances = new double[length][length];
        for (Node n1 : this.nodes) {
            for (Node n2 : this.nodes) {
                distances[n1.getId()][n2.getId()] = n1.getLocation().distance(n2.getLocation());
            }
        }
    }

    private void findNearestSatellites() {
        // find each customer's nearest satellite

        for (Customer customer : customers()) {
            // - find nearest satellite
            Satellite nearestSat = satellites().stream()
                    .parallel()
                    .reduce((n1, n2) -> distance(customer, n1) < distance(customer, n2) ? n1 : n2)
                    .get();
            nearestSat.addPackage(customer.getPackageAvailability());
        }
    }

//    private void findNearestSatellites(CustomerAssignment strategy) {
//        // find each customer's nearest satellite
//        List<Satellite> satellites = satellites();
//        for (Customer cust : customers()) {
//            Satellite nearestSat = strategy.getAssignedSatellite(cust, satellites);
//            nearestSat.addCustomer(cust);
//        }
//    }

    public void applyPackageAssignment(PackageAssignment strategy) {
        List<Satellite> satellites = satellites();
        for (Satellite satellite : satellites) {
            satellite.clearPackages();
        }
        for (Customer customer : customers()) {
            PackageAvailability aPackage = customer.getPackageAvailability();
            Satellite nearestSat = strategy.getAssignedSatellite(aPackage, satellites);
            nearestSat.addPackage(aPackage);
        }
    }

    public Node getNode(int id) {
        return (Node) IDManager.getInstance().getObject(id);
    }

    public List<Depot> depots() {
        return nodes.stream()
                .parallel()
                .filter(node -> node instanceof Depot)
                .map(node -> (Depot) node)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Satellite> satellites() {
        return nodes.stream()
                .parallel()
                .filter(node -> node instanceof Satellite)
                .map(node -> (Satellite) node)
                .collect(Collectors.toList());
    }

    public List<Customer> customers() {
        return nodes.stream()
                .parallel()
                .filter(node -> node instanceof Customer)
                .map(node -> (Customer) node)
                .collect(Collectors.toList());
    }

    public List<Package> packages() {
        return customers().stream()
                .parallel().map(Customer::getPackage)
                .collect(Collectors.toList());
    }

    public double distance(Node n1, Node n2) {
        return distances[n1.getId()][n2.getId()];
    }
}
