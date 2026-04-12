/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.graphics;

public enum Location {
    CENTER(0.5, 0.5),
    NORTH(0.5, 0.0),
    NORTH_EAST(1.0, 0.0),
    EAST(1.0, 0.5),
    SOUTH_EAST(1.0, 1.0),
    SOUTH(0.5, 1.0),
    SOUTH_WEST(0.0, 1.0),
    WEST(0.0, 0.5),
    NORTH_WEST(0.0, 0.0);

    private final double a;
    private final double b;

    private Location(double d, double d2) {
        this.a = d;
        this.b = d2;
    }

    public final double getAlignmentH() {
        return this.a;
    }

    public final double getAlignmentV() {
        return this.b;
    }
}

