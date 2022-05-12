package nl.tue.vrp;

import nl.tue.vrp.model.*;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        // generate random nodes

        List<Node> nodes = new ArrayList<>();
        Random r = new Random(42);

        // - generate depots
        for (int i=0; i<3; i++) {
            nodes.add(new Node.Depot(i, r.nextInt(100), r.nextInt(100), 0));
        }
        // - generate satellites
        for (int i=3; i<7; i++) {
            nodes.add(new Node.Satellite(i, r.nextInt(100), r.nextInt(100), 0));
        }
        // - generate customers
        for (int i=7; i<20; i++) {
            nodes.add(new Node.Customer(i, r.nextInt(100), r.nextInt(100), r.nextInt(20)));
        }

        // build the world

        World world = new World(nodes);

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

        // dummy routing

        for (Node sat: satellites) {
            System.out.printf("# Route for %s:%n", sat);

            Vehicle v1 = new Vehicle(sat.getId() * 100 + 1, 40);
            Vehicle v2 = new Vehicle(sat.getId() * 100 + 2, 20);

            Routes routes = new Routes(
                    List.of(v1, v2), // available vehicles
                    ((Node.Satellite) sat).listNodes(), // all nodes to visit (satellite + its customers)
                    (vs, ns) -> {

                        // implement algorithm here

                        List<Route> candidate = new ArrayList<>();

                        List<Node> remainingNodes = new ArrayList<>(ns);
                        List<Vehicle> remainingVehicles = new ArrayList<>(vs);
                        while (!remainingNodes.isEmpty() && !remainingVehicles.isEmpty()) {
                            Vehicle v = remainingVehicles.remove(0);
                            List<Node> currentRoute = new ArrayList<>();
                            currentRoute.add(remainingNodes.get(0));
                            Visit visit = new Visit(v, remainingNodes.remove(0));
                            while (!remainingNodes.isEmpty() && (v.getCapacity() - visit.getLoad()) > remainingNodes.get(0).getDemand()) {
                                currentRoute.add(remainingNodes.get(0));
                                visit = visit.nextVisit(remainingNodes.remove(0));
                            }
                            candidate.add(new Route(v, currentRoute));
                        }
                        return candidate;
                    }
            );
            System.out.println(routes);
            System.out.println();
        }
    }
}
