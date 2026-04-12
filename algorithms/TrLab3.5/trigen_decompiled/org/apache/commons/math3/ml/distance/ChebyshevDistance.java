/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.ml.distance;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.util.MathArrays;

public class ChebyshevDistance
implements DistanceMeasure {
    private static final long serialVersionUID = -4694868171115238296L;

    public double compute(double[] a2, double[] b2) {
        return MathArrays.distanceInf(a2, b2);
    }
}

