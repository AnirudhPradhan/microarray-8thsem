/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.ml.distance;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.util.FastMath;

public class EarthMoversDistance
implements DistanceMeasure {
    private static final long serialVersionUID = -5406732779747414922L;

    public double compute(double[] a2, double[] b2) {
        double lastDistance = 0.0;
        double totalDistance = 0.0;
        for (int i = 0; i < a2.length; ++i) {
            double currentDistance = a2[i] + lastDistance - b2[i];
            totalDistance += FastMath.abs(currentDistance);
            lastDistance = currentDistance;
        }
        return totalDistance;
    }
}

