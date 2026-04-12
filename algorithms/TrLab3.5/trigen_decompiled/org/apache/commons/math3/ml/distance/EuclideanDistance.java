/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.ml.distance;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.util.MathArrays;

public class EuclideanDistance
implements DistanceMeasure {
    private static final long serialVersionUID = 1717556319784040040L;

    public double compute(double[] a2, double[] b2) {
        return MathArrays.distance(a2, b2);
    }
}

