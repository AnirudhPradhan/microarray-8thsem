/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.distribution;

import java.util.ArrayList;
import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotANumberException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;

public class EnumeratedRealDistribution
extends AbstractRealDistribution {
    private static final long serialVersionUID = 20130308L;
    protected final EnumeratedDistribution<Double> innerDistribution;

    public EnumeratedRealDistribution(double[] singletons, double[] probabilities) throws DimensionMismatchException, NotPositiveException, MathArithmeticException, NotFiniteNumberException, NotANumberException {
        this(new Well19937c(), singletons, probabilities);
    }

    public EnumeratedRealDistribution(RandomGenerator rng, double[] singletons, double[] probabilities) throws DimensionMismatchException, NotPositiveException, MathArithmeticException, NotFiniteNumberException, NotANumberException {
        super(rng);
        if (singletons.length != probabilities.length) {
            throw new DimensionMismatchException(probabilities.length, singletons.length);
        }
        ArrayList samples = new ArrayList(singletons.length);
        for (int i = 0; i < singletons.length; ++i) {
            samples.add(new Pair<Double, Double>(singletons[i], probabilities[i]));
        }
        this.innerDistribution = new EnumeratedDistribution(rng, samples);
    }

    public double probability(double x) {
        return this.innerDistribution.probability(x);
    }

    public double density(double x) {
        return this.probability(x);
    }

    public double cumulativeProbability(double x) {
        double probability = 0.0;
        for (Pair<Double, Double> sample : this.innerDistribution.getPmf()) {
            if (!(sample.getKey() <= x)) continue;
            probability += sample.getValue().doubleValue();
        }
        return probability;
    }

    public double inverseCumulativeProbability(double p) throws OutOfRangeException {
        if (p < 0.0 || p > 1.0) {
            throw new OutOfRangeException(p, (Number)0, 1);
        }
        double probability = 0.0;
        double x = this.getSupportLowerBound();
        for (Pair<Double, Double> sample : this.innerDistribution.getPmf()) {
            if (sample.getValue() == 0.0) continue;
            probability += sample.getValue().doubleValue();
            x = sample.getKey();
            if (!(probability >= p)) continue;
            break;
        }
        return x;
    }

    public double getNumericalMean() {
        double mean = 0.0;
        for (Pair<Double, Double> sample : this.innerDistribution.getPmf()) {
            mean += sample.getValue() * sample.getKey();
        }
        return mean;
    }

    public double getNumericalVariance() {
        double mean = 0.0;
        double meanOfSquares = 0.0;
        for (Pair<Double, Double> sample : this.innerDistribution.getPmf()) {
            mean += sample.getValue() * sample.getKey();
            meanOfSquares += sample.getValue() * sample.getKey() * sample.getKey();
        }
        return meanOfSquares - mean * mean;
    }

    public double getSupportLowerBound() {
        double min = Double.POSITIVE_INFINITY;
        for (Pair<Double, Double> sample : this.innerDistribution.getPmf()) {
            if (!(sample.getKey() < min) || !(sample.getValue() > 0.0)) continue;
            min = sample.getKey();
        }
        return min;
    }

    public double getSupportUpperBound() {
        double max = Double.NEGATIVE_INFINITY;
        for (Pair<Double, Double> sample : this.innerDistribution.getPmf()) {
            if (!(sample.getKey() > max) || !(sample.getValue() > 0.0)) continue;
            max = sample.getKey();
        }
        return max;
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

    public double sample() {
        return this.innerDistribution.sample();
    }
}

