package nl.tue.vrp.model;

import java.util.EnumSet;

public enum Constraints {
    CHECK_CAPACITY, CHECK_TIME, CHECK_FUEL;
    public static final EnumSet<Constraints> CHECK_ALL = EnumSet.allOf(Constraints.class);
}
