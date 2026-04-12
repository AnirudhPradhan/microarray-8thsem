/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.ml.distance;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.util.FastMath;

public class CanberraDistance
implements DistanceMeasure {
    private static final long serialVersionUID = -6972277381587032228L;

    public double compute(double[] a2, double[] b2) {
        double sum = 0.0;
        for (int i = 0; i < a2.length; ++i) {
            double num = FastMath.abs(a2[i] - b2[i]);
            double denom = FastMath.abs(a2[i]) + FastMath.abs(b2[i]);
            sum += num == 0.0 && denom == 0.0 ? 0.0 : num / denom;
        }
        return sum;
    }
}

