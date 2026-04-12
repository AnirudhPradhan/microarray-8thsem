/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.ml.distance;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.util.MathArrays;

public class ManhattanDistance
implements DistanceMeasure {
    private static final long serialVersionUID = -9108154600539125566L;

    public double compute(double[] a2, double[] b2) {
        return MathArrays.distance1(a2, b2);
    }
}

