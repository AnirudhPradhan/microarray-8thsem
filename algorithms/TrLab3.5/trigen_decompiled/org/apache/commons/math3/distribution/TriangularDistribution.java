/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

public class TriangularDistribution
extends AbstractRealDistribution {
    private static final long serialVersionUID = 20120112L;
    private final double a;
    private final double b;
    private final double c;
    private final double solverAbsoluteAccuracy;

    public TriangularDistribution(double a2, double c2, double b2) throws NumberIsTooLargeException, NumberIsTooSmallException {
        this(new Well19937c(), a2, c2, b2);
    }

    public TriangularDistribution(RandomGenerator rng, double a2, double c2, double b2) throws NumberIsTooLargeException, NumberIsTooSmallException {
        super(rng);
        if (a2 >= b2) {
            throw new NumberIsTooLargeException((Localizable)LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, (Number)a2, b2, false);
        }
        if (c2 < a2) {
            throw new NumberIsTooSmallException((Localizable)LocalizedFormats.NUMBER_TOO_SMALL, (Number)c2, a2, true);
        }
        if (c2 > b2) {
            throw new NumberIsTooLargeException((Localizable)LocalizedFormats.NUMBER_TOO_LARGE, (Number)c2, b2, true);
        }
        this.a = a2;
        this.c = c2;
        this.b = b2;
        this.solverAbsoluteAccuracy = FastMath.max(FastMath.ulp(a2), FastMath.ulp(b2));
    }

    public double getMode() {
        return this.c;
    }

    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double density(double x) {
        if (x < this.a) {
            return 0.0;
        }
        if (this.a <= x && x < this.c) {
            double divident = 2.0 * (x - this.a);
            double divisor = (this.b - this.a) * (this.c - this.a);
            return divident / divisor;
        }
        if (x == this.c) {
            return 2.0 / (this.b - this.a);
        }
        if (this.c < x && x <= this.b) {
            double divident = 2.0 * (this.b - x);
            double divisor = (this.b - this.a) * (this.b - this.c);
            return divident / divisor;
        }
        return 0.0;
    }

    public double cumulativeProbability(double x) {
        if (x < this.a) {
            return 0.0;
        }
        if (this.a <= x && x < this.c) {
            double divident = (x - this.a) * (x - this.a);
            double divisor = (this.b - this.a) * (this.c - this.a);
            return divident / divisor;
        }
        if (x == this.c) {
            return (this.c - this.a) / (this.b - this.a);
        }
        if (this.c < x && x <= this.b) {
            double divident = (this.b - x) * (this.b - x);
            double divisor = (this.b - this.a) * (this.b - this.c);
            return 1.0 - divident / divisor;
        }
        return 1.0;
    }

    public double getNumericalMean() {
        return (this.a + this.b + this.c) / 3.0;
    }

    public double getNumericalVariance() {
        return (this.a * this.a + this.b * this.b + this.c * this.c - this.a * this.b - this.a * this.c - this.b * this.c) / 18.0;
    }

    public double getSupportLowerBound() {
        return this.a;
    }

    public double getSupportUpperBound() {
        return this.b;
    }

    public boolean isSupportLowerBoundInclusive() {
        return true;
    }

    public boolean isSupportUpperBoundInclusive() {
        return true;
    }

    public boolean isSupportConnected() {
        return true;
    }

    public double inverseCumulativeProbability(double p) throws OutOfRangeException {
        if (p < 0.0 || p > 1.0) {
            throw new OutOfRangeException(p, (Number)0, 1);
        }
        if (p == 0.0) {
            return this.a;
        }
        if (p == 1.0) {
            return this.b;
        }
        if (p < (this.c - this.a) / (this.b - this.a)) {
            return this.a + FastMath.sqrt(p * (this.b - this.a) * (this.c - this.a));
        }
        return this.b - FastMath.sqrt((1.0 - p) * (this.b - this.a) * (this.b - this.c));
    }
}

