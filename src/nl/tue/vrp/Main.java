package nl.tue.vrp;

import nl.tue.vrp.model.*;
import nl.tue.vrp.model.nodes.Customer;
import nl.tue.vrp.model.nodes.Depot;
import nl.tue.vrp.model.nodes.Node;
import nl.tue.vrp.model.nodes.Satellite;

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

            List<Depot> depots = world.depots();
            List<Satellite> satellites = world.satellites();
            List<Customer> customers = world.customers();

            for (Satellite sat: satellites) {
                // add vehicles (dummy - should be configurable from file etc)
                for (int i=1; i<=5; i++) {
                    sat.addVehicle(new Vehicle(sat.getId() * 100 + i, 50));
                }
            }

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

            // print each satellite's customers

            // - for each satellite...
            for (Satellite sat: satellites) {
                System.out.printf("# Customers for %s:%n", sat);
                // - if there are customers assigned to it, print them all; otherwise print [none]
                List<Customer> myCustomers = sat.listCustomers();
                if (!myCustomers.isEmpty()) {
                    myCustomers.forEach(System.out::println);
                } else {
                    System.out.println("[none]");
                }
                System.out.println();
            }

            for (Satellite sat: satellites) {
                System.out.printf("# Route for %s:%n", sat);

                // greedy strategy: find next closest node
                BiFunction<Visit, List<Node>, Node> searchNodeStrategy = (visit, nodes) -> nodes.parallelStream()
                        .min(Comparator.comparingDouble(n -> world.distance(n, visit.getNode())))
                        .get();

                Routes routes = new Routes(sat, searchNodeStrategy);

                System.out.println(routes);
                System.out.println();
            }
        } catch (IOException e) {
            System.err.printf("Cannot open file %s%n", fileName);
            System.exit(-1);
        }

    }
}
