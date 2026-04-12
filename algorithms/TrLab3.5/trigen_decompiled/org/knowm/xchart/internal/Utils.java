/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal;

public class Utils {
    private Utils() {
    }

    public static double getTickStartOffset(double workingSpace, double tickSpace) {
        double marginSpace = workingSpace - tickSpace;
        return marginSpace / 2.0;
    }

    public static double pow(double base, int exponent) {
        if (exponent > 0) {
            return Math.pow(base, exponent);
        }
        return 1.0 / Math.pow(base, -1 * exponent);
    }
}

