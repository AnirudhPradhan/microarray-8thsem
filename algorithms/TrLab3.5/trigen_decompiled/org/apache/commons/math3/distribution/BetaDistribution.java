/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.FastMath;

public class BetaDistribution
extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9;
    private static final long serialVersionUID = -1221965979403477668L;
    private final double alpha;
    private final double beta;
    private double z;
    private final double solverAbsoluteAccuracy;

    public BetaDistribution(double alpha, double beta) {
        this(alpha, beta, 1.0E-9);
    }

    public BetaDistribution(double alpha, double beta, double inverseCumAccuracy) {
        this(new Well19937c(), alpha, beta, inverseCumAccuracy);
    }

    public BetaDistribution(RandomGenerator rng, double alpha, double beta) {
        this(rng, alpha, beta, 1.0E-9);
    }

    public BetaDistribution(RandomGenerator rng, double alpha, double beta, double inverseCumAccuracy) {
        super(rng);
        this.alpha = alpha;
        this.beta = beta;
        this.z = Double.NaN;
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    public double getAlpha() {
        return this.alpha;
    }

    public double getBeta() {
        return this.beta;
    }

    private void recomputeZ() {
        if (Double.isNaN(this.z)) {
            this.z = Gamma.logGamma(this.alpha) + Gamma.logGamma(this.beta) - Gamma.logGamma(this.alpha + this.beta);
        }
    }

    public double density(double x) {
        double logDensity = this.logDensity(x);
        return logDensity == Double.NEGATIVE_INFINITY ? 0.0 : FastMath.exp(logDensity);
    }

    public double logDensity(double x) {
        this.recomputeZ();
        if (x < 0.0 || x > 1.0) {
            return Double.NEGATIVE_INFINITY;
        }
        if (x == 0.0) {
            if (this.alpha < 1.0) {
                throw new NumberIsTooSmallException((Localizable)LocalizedFormats.CANNOT_COMPUTE_BETA_DENSITY_AT_0_FOR_SOME_ALPHA, (Number)this.alpha, 1, false);
            }
            return Double.NEGATIVE_INFINITY;
        }
        if (x == 1.0) {
            if (this.beta < 1.0) {
                throw new NumberIsTooSmallException((Localizable)LocalizedFormats.CANNOT_COMPUTE_BETA_DENSITY_AT_1_FOR_SOME_BETA, (Number)this.beta, 1, false);
            }
            return Double.NEGATIVE_INFINITY;
        }
        double logX = FastMath.log(x);
        double log1mX = FastMath.log1p(-x);
        return (this.alpha - 1.0) * logX + (this.beta - 1.0) * log1mX - this.z;
    }

    public double cumulativeProbability(double x) {
        if (x <= 0.0) {
            return 0.0;
        }
        if (x >= 1.0) {
            return 1.0;
        }
        return Beta.regularizedBeta(x, this.alpha, this.beta);
    }

    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double getNumericalMean() {
        double a2 = this.getAlpha();
        return a2 / (a2 + this.getBeta());
    }

    public double getNumericalVariance() {
        double a2 = this.getAlpha();
        double b2 = this.getBeta();
        double alphabetasum = a2 + b2;
        return a2 * b2 / (alphabetasum * alphabetasum * (alphabetasum + 1.0));
    }

    public double getSupportLowerBound() {
        return 0.0;
    }

    public double getSupportUpperBound() {
        return 1.0;
    }

    public boolean isSupportLowerBoundInclusive() {
        return false;
    }

    public boolean isSupportUpperBoundInclusive() {
        return false;
    }

    public boolean isSupportConnected() {
        return true;
    }
}

