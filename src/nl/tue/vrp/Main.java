package nl.tue.vrp;

import nl.tue.vrp.config.WorldConfig;
import nl.tue.vrp.model.Routes;
import nl.tue.vrp.model.Visit;
import nl.tue.vrp.model.World;
import nl.tue.vrp.model.nodes.Customer;
import nl.tue.vrp.model.nodes.Node;
import nl.tue.vrp.model.nodes.Satellite;
import nl.tue.vrp.strategy.customerassignment.NearestSatellite;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;

public class Main {

    public static void main(String[] args) {
        String fileName = "data/example.yaml";
        try {
            Yaml yaml = new Yaml(new Constructor(WorldConfig.class));
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(fileName);
            WorldConfig worldConfig = yaml.load(inputStream);
            World world = new World(worldConfig);
            world.applyCustomerAssignment(new NearestSatellite());

//            // generate random nodes
//
//            List<Node> wNodes = new ArrayList<>();
//            Random r = new Random(42);
//
//            // - generate depots
//            for (int i = 0; i < 3; i++) {
//                wNodes.add(new Depot(i, r.nextInt(100) + 1, r.nextInt(100) + 1, 0));
//            }
//            // - generate satellites
//            for (int i = 3; i < 7; i++) {
//                wNodes.add(new Satellite(i, r.nextInt(100) + 1, r.nextInt(100) + 1, 0));
//            }
//            // - generate customers
//            for (int i = 7; i < 57; i++) {
//                wNodes.add(new Customer(i, r.nextInt(100) + 1, r.nextInt(100) + 1, (r.nextInt(20) + 1) * (r.nextBoolean() ? 1 : -1), 0, 0, 0, null));
//            }
//
//            // build the world
//
//            World world = new World(wNodes);
//
//            // load world from file
//
////            String fileName = "data/example.csv";
////            World world = new World(fileName);
//
//            List<Depot> depots = world.depots();
//            List<Satellite> satellites = world.satellites();
//            List<Customer> customers = world.customers();
//
//            for (Satellite sat : satellites) {
//                // add vehicles (dummy - should be configurable from file etc)
//                for (int i = 1; i <= 5; i++) {
//                    sat.addVehicle(new Vehicle(sat.getId() * 100 + i, 50, 1d));
//                }
//            }

            // print all nodes

            System.out.println("\"All depots:\"");
            world.depots().forEach(System.out::println);
            System.out.println();
            System.out.println("\"All satellites:\"");
            world.satellites().forEach(System.out::println);
            System.out.println();
            System.out.println("\"All customers:\"");
            world.customers().forEach(System.out::println);
            System.out.println();

            // print each satellite's customers

            // - for each satellite...
            for (Satellite sat : world.satellites()) {
                System.out.printf("\"Customers for %s:\"%n", sat);
                // - if there are customers assigned to it, print them all; otherwise print [none]
                List<Customer> myCustomers = sat.listCustomers();
                if (!myCustomers.isEmpty()) {
                    myCustomers.forEach(System.out::println);
                } else {
                    System.out.println("(none)");
                }
                System.out.println();
            }

            // greedy strategy: find next closest node
            BiFunction<Visit, List<Node>, Node> searchNodeStrategy = (visit, nodes) -> nodes.parallelStream()
                    .min(Comparator.comparingDouble(n -> world.distance(n, visit.getNode())))
                    .get();

            for (Satellite sat : world.satellites()) {
                System.out.printf("\"Route for %s:\"%n", sat);

                Routes routes = new Routes(sat, searchNodeStrategy);

                System.out.println(routes);
                System.out.println();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }

    }
}
