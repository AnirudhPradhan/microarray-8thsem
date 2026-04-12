/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.AbstractIntegerDistribution;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

public class GeometricDistribution
extends AbstractIntegerDistribution {
    private static final long serialVersionUID = 20130507L;
    private final double probabilityOfSuccess;

    public GeometricDistribution(double p) {
        this(new Well19937c(), p);
    }

    public GeometricDistribution(RandomGenerator rng, double p) {
        super(rng);
        if (p <= 0.0 || p > 1.0) {
            throw new OutOfRangeException((Localizable)LocalizedFormats.OUT_OF_RANGE_LEFT, (Number)p, 0, 1);
        }
        this.probabilityOfSuccess = p;
    }

    public double getProbabilityOfSuccess() {
        return this.probabilityOfSuccess;
    }

    public double probability(int x) {
        double ret;
        if (x < 0) {
            ret = 0.0;
        } else {
            double p = this.probabilityOfSuccess;
            ret = FastMath.pow(1.0 - p, x) * p;
        }
        return ret;
    }

    public double logProbability(int x) {
        double ret;
        if (x < 0) {
            ret = Double.NEGATIVE_INFINITY;
        } else {
            double p = this.probabilityOfSuccess;
            ret = (double)x * FastMath.log1p(-p) + FastMath.log(p);
        }
        return ret;
    }

    public double cumulativeProbability(int x) {
        double ret;
        if (x < 0) {
            ret = 0.0;
        } else {
            double p = this.probabilityOfSuccess;
            ret = 1.0 - FastMath.pow(1.0 - p, x + 1);
        }
        return ret;
    }

    public double getNumericalMean() {
        double p = this.probabilityOfSuccess;
        return (1.0 - p) / p;
    }

    public double getNumericalVariance() {
        double p = this.probabilityOfSuccess;
        return (1.0 - p) / (p * p);
    }

    public int getSupportLowerBound() {
        return 0;
    }

    public int getSupportUpperBound() {
        return Integer.MAX_VALUE;
    }

    public boolean isSupportConnected() {
        return true;
    }
}

