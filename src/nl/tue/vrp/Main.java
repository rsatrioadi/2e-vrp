package nl.tue.vrp;

import nl.tue.vrp.model.*;
import nl.tue.vrp.solver.Solver;
import nl.tue.vrp.strategy.packagesearch.NearestPackage;
import nl.tue.vrp.strategy.packageassignment.NearestSatellite;
import nl.tue.vrp.strategy.packagenodeavailability.SimpleStrategy;
import nl.tue.vrp.strategy.selectvehicle.EarliestBiggestVehicle;

import java.util.EnumSet;

public class Main {
  public static void main(String[] args) {
    try {
      EnumSet<Constraints> constraints = (new ConstraintsBuilder()).
              addCapacityCheck().
              addTimeCheck().
              get();
      Solver solver = new Solver(
              "testdata.yaml",
              constraints,
              new NearestSatellite(),
              new EarliestBiggestVehicle(),
              new NearestPackage(),
              new SimpleStrategy(),
              new EarliestBiggestVehicle(),
              new NearestPackage()
      );

      System.out.println(solver.getYAMLOutput());
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      System.exit(-1);
    }
  }
}
