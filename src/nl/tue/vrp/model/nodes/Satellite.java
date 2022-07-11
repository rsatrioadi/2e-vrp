package nl.tue.vrp.model.nodes;

import nl.tue.vrp.model.Vehicle;
import nl.tue.vrp.model.VehicleOwner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Satellite extends Node implements VehicleOwner {

    private final List<Customer> customers = new ArrayList<>();
    private final List<Vehicle> vehicles = new ArrayList<>();

    public Satellite(int id, int x, int y, int serviceTime) {
        super(id, x, y, 0, serviceTime);
    }

    public void addVehicle(Vehicle v) {
        vehicles.add(v);
    }

    public void addVehicles(Collection<Vehicle> vs) {
        vehicles.addAll(vs);
    }

    public List<Vehicle> getVehicles() {
        return vehicles.stream().collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String toString() {
        return String.format("Satellite[id= %d, location= %s, vehicleCapacities= %s, vehicleSpeeds= %s]",
                id,
                location,
                vehicles.stream().map(Vehicle::getCapacity).collect(Collectors.toUnmodifiableList()),
                vehicles.stream().map(Vehicle::getSpeed).collect(Collectors.toUnmodifiableList()));
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public List<Customer> listCustomers() {
        return customers.stream().collect(Collectors.toUnmodifiableList());
    }

    public List<Node> listNodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(this);
        nodes.addAll(listCustomers());
        return nodes.stream().collect(Collectors.toUnmodifiableList());
    }
}
