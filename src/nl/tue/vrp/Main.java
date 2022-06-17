package nl.tue.vrp;

import nl.tue.vrp.model.*;

import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;

public class Main {

    public static void main(String[] args) {

        String fileName = "data/example.csv";
        try {

//            // generate random nodes
//
//            List<Node> nodes = new ArrayList<>();
//            Random r = new Random(42);
//
//            // - generate depots
//            for (int i=0; i<3; i++) {
//                nodes.add(new Node.Depot(i, r.nextInt(100), r.nextInt(100), 0, 0));
//            }
//            // - generate satellites
//            for (int i=3; i<7; i++) {
//                nodes.add(new Node.Satellite(i, r.nextInt(100), r.nextInt(100), 0, 0));
//            }
//            // - generate customers
//            for (int i=7; i<20; i++) {
//                nodes.add(new Node.Customer(i, r.nextInt(100), r.nextInt(100), r.nextInt(20), 0, 0, 0, null));
//            }
//
//            // build the world
//
//            World world = new World(nodes);

            // load world from file

            World world = new World(fileName);

            List<Node> depots = world.depots();
            List<Node> satellites = world.satellites();
            List<Node> customers = world.customers();

            // print all nodes

            System.out.println("# All depots:");
            depots.forEach(System.out::println);
            System.out.println();
            System.out.println("# All satellites:");
            satellites.forEach(System.out::println);
            System.out.println();
            System.out.println("# All customers:");
            customers.forEach(System.out::println);
            System.out.println();

            // find each customer's nearest satellite

            for (Node cust: customers) {
                // - find nearest satellite
                Node.Satellite nearestSat = satellites.stream()
                        .map(s -> (Node.Satellite) s)
                        .reduce((n1, n2) -> world.distance(cust, n1) < world.distance(cust, n2) ? n1 : n2)
                        .get();
                nearestSat.addCustomer((Node.Customer) cust);
            }

            // print each satellite's customers

            // - for each satellite...
            for (Node sat: satellites) {
                System.out.printf("# Customers for %s:%n", sat);
                // - if there are customers assigned to it, print them all; otherwise print [none]
                var myCustomers = ((Node.Satellite)sat).listCustomers();
                if (!myCustomers.isEmpty()) {
                    myCustomers.forEach(System.out::println);
                } else {
                    System.out.println("[none]");
                }
                System.out.println();
            }

            // dummy routing with naive algorithm: first vehicle tries to grab as many customers as possible
            // in the order of customer.id (not based on shortest path), the next vehicle does the same with
            // the remaining customers, and so on

            for (Node sat: satellites) {
                System.out.printf("# Route for %s:%n", sat);

                Vehicle v1 = new Vehicle(sat.getId() * 100 + 1, 40);
                Vehicle v2 = new Vehicle(sat.getId() * 100 + 2, 20);

                BiFunction<List<Vehicle>, List<Node>, List<Route>> strategy = (vs, ns) -> {

                    // implement algorithm here

                    List<Route> candidate = new ArrayList<>();

                    List<Node> remainingNodes = new ArrayList<>(ns);
                    List<Vehicle> remainingVehicles = new ArrayList<>(vs);
                    while (!remainingNodes.isEmpty() && !remainingVehicles.isEmpty()) {
                        Vehicle v = remainingVehicles.remove(0);
                        List<Node> currentRoute = new ArrayList<>();
                        currentRoute.add(remainingNodes.get(0));
                        Visit visit = new Visit(v, remainingNodes.remove(0));
                        int idx = 0;
                        while (idx < remainingNodes.size()) {
                            if ((v.getCapacity() - visit.getLoad()) > remainingNodes.get(idx).getDemand()) {
                                currentRoute.add(remainingNodes.get(idx));
                                visit = visit.nextVisit(remainingNodes.remove(idx));
                            } else {
                                idx++;
                            }
                        }
                        candidate.add(new Route(v, currentRoute));
                    }
                    return candidate;
                };

                Routes routes = new Routes(
                        List.of(v1, v2), // available vehicles
                        ((Node.Satellite) sat).listNodes(), // all nodes to visit (satellite + its customers)
                        strategy
                );
                System.out.println(routes);
                System.out.println();
            }
        } catch (IOException e) {
            System.err.printf("Cannot open file %s%n", fileName);
            System.exit(-1);
        }

    }
}
