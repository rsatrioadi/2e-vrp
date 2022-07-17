package nl.tue.vrp.strategy.customerassignment;

import nl.tue.vrp.model.nodes.Customer;
import nl.tue.vrp.model.nodes.Satellite;

import java.util.List;

public interface CustomerAssignment {
    Satellite getAssignedSatellite(Customer customer, List<Satellite> satellites);
}
