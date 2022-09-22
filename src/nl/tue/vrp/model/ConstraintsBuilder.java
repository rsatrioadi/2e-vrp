package nl.tue.vrp.model;

import java.util.EnumSet;

public class ConstraintsBuilder {
    EnumSet<Constraints> constraints;

    public static EnumSet<Constraints> allCheck() {
        return Constraints.CHECK_ALL;
    }

    public ConstraintsBuilder(){
        constraints = EnumSet.noneOf(Constraints.class);
    }

    public ConstraintsBuilder addCapacityCheck() {
        this.constraints.add(Constraints.CHECK_CAPACITY);
        return this;
    }

    public ConstraintsBuilder addTimeCheck() {
        this.constraints.add(Constraints.CHECK_TIME);
        return this;
    }

    public ConstraintsBuilder addFuelCheck() {
        this.constraints.add(Constraints.CHECK_FUEL);
        return  this;
    }

    public final EnumSet<Constraints> get() {
        return constraints;
    }
}
