package model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum ShipPosition {
    VERTICAL, HORIZONTAL;
    private static final List<ShipPosition> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static ShipPosition randomPosition()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
};