package nl.tue.vrp.model;

import nl.tue.vrp.model.nodes.Customer;
import nl.tue.vrp.model.nodes.Node;
import nl.tue.vrp.model.nodes.Depot;
import nl.tue.vrp.model.nodes.Satellite;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Package {
    public enum ServiceType {
        PICKUP,
        DELIVERY
    }

    private final ServiceType type;
    private final int demand;
    private final List<Node> nodes;
    private final Depot depot;


    public Package(ServiceType type, int demand, Node node, Depot depot) {
        this.type = type;
        this.demand = demand;
        this.depot = depot;
        this.nodes = new ArrayList<>();
        nodes.add(node);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public Node getCurrentNode() {
        return nodes.get(nodes.size() - 1);
    }

    public Customer getCustomer() {
        return (Customer) nodes.get(0);
    }

    public Optional<Satellite> getSatellite() {
        if (nodes.size() > 1) {
            return Optional.of((Satellite) nodes.get(1));
        }
        return Optional.ofNullable(null);
    }

    public ServiceType getType() {
        return type;
    }

    public int getDemand() {
        return demand;
    }

    public Depot getDepot() {
        return depot;
    }

    public Package transferTo(Node node) {
        this.nodes.add(node);
        return this;
    }

    public boolean isDelivery() {
        return type == ServiceType.DELIVERY;
    }

    public boolean isPickup() {
        return type == ServiceType.PICKUP;
    }

    public static ServiceType typeFromString(String type) {
        if (type.equals("pickup")) {
            return ServiceType.PICKUP;
        } else if (type.equals("delivery")) {
            return ServiceType.DELIVERY;
        }
        throw new IllegalArgumentException("invalid customer type: " + type);
    }

    public static String typeToString(ServiceType type) {
        switch (type) {
            case PICKUP:
                return "pickup";
            case DELIVERY:
                return "delivery";
        };
        return "";
    }

    @Override
    public String toString() {
        return String.format("type: %s demand: %d depot: %d customer: %d", typeToString(type), demand, depot.getId(), nodes.get(0).getId());
    }
}
