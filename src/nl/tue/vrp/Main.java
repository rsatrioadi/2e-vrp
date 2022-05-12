package nl.tue.vrp;

import nl.tue.vrp.model.Node;
import nl.tue.vrp.model.Route;
import nl.tue.vrp.model.Vehicle;
import nl.tue.vrp.model.World;

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

        Map<Node, List<Node>> satelliteToCustomer = new HashMap<>();
        // - for each customer...
        for (Node cust: customers) {
            // - find nearest satellite
            Node nearestSat = satellites.stream()
                    .reduce((n1, n2) -> world.distance(cust, n1) < world.distance(cust, n2) ? n1 : n2)
                    .get();
            // - if there is no customer list for the satellite, make a new (empty) list
            if (!satelliteToCustomer.containsKey(nearestSat)) {
                satelliteToCustomer.put(nearestSat, new ArrayList<>());
            }
            // - add this customer to the list
            satelliteToCustomer.get(nearestSat).add(cust);
        }

        // print each satellite's customers

        // - for each satellite...
        for (Node sat: satellites) {
            System.out.printf("# Customers for %s:%n", sat);
            // - if there are customers assigned to it, print them all; otherwise print [none]
            if (satelliteToCustomer.containsKey(sat)) {
                satelliteToCustomer.get(sat).forEach(System.out::println);
            } else {
                System.out.println("[none]");
            }
            System.out.println();
        }

        // dummy routing: 1 vehicle, visit all customers ordered by id (not shortest path),
        // skip customer if vehicle capacity can't meet its demand

        for (Node sat: satelliteToCustomer.keySet()) {
            System.out.printf("# Route for %s:%n", sat);
            Vehicle veh = new Vehicle(sat.getId()*100+1, 100);
            Route route = new Route(veh, satelliteToCustomer.get(sat));
            System.out.println(route);
            System.out.println();
        }
    }
}
