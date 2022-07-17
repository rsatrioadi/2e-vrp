package nl.tue.vrp.strategy.customerassignment;

import nl.tue.vrp.model.nodes.Customer;
import nl.tue.vrp.model.nodes.Satellite;

import java.util.List;
import java.util.Random;

public class RandomSatellite implements CustomerAssignment {
    protected final Random rand;

    RandomSatellite() {
        rand = new Random();
    }

    RandomSatellite(int seed) {
        rand = new Random(seed);
    }

    @Override
    public Satellite getAssignedSatellite(Customer customer, List<Satellite> satellites) {
        if (satellites.size() == 0) {
            return null;
        }
        return satellites.get(rand.nextInt(satellites.size()));
    }
}
