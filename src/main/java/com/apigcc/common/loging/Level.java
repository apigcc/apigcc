package com.apigcc.common.loging;

import java.util.Comparator;

public enum Level {

    ERROR(3),WARNING(2),INFO(1),DEBUG(0);

    private int level;

    Level(int level) {
        this.level = level;
    }

    public static Comparator<Level> COMPARATOR = Comparator.comparingInt(o -> o.level);

}
