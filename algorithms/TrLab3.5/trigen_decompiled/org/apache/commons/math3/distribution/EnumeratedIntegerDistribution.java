/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.distribution;

import java.util.ArrayList;
import org.apache.commons.math3.distribution.AbstractIntegerDistribution;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotANumberException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;

public class EnumeratedIntegerDistribution
extends AbstractIntegerDistribution {
    private static final long serialVersionUID = 20130308L;
    protected final EnumeratedDistribution<Integer> innerDistribution;

    public EnumeratedIntegerDistribution(int[] singletons, double[] probabilities) throws DimensionMismatchException, NotPositiveException, MathArithmeticException, NotFiniteNumberException, NotANumberException {
        this(new Well19937c(), singletons, probabilities);
    }

    public EnumeratedIntegerDistribution(RandomGenerator rng, int[] singletons, double[] probabilities) throws DimensionMismatchException, NotPositiveException, MathArithmeticException, NotFiniteNumberException, NotANumberException {
        super(rng);
        if (singletons.length != probabilities.length) {
            throw new DimensionMismatchException(probabilities.length, singletons.length);
        }
        ArrayList samples = new ArrayList(singletons.length);
        for (int i = 0; i < singletons.length; ++i) {
            samples.add(new Pair<Integer, Double>(singletons[i], probabilities[i]));
        }
        this.innerDistribution = new EnumeratedDistribution(rng, samples);
    }

    public double probability(int x) {
        return this.innerDistribution.probability(x);
    }

    public double cumulativeProbability(int x) {
        double probability = 0.0;
        for (Pair<Integer, Double> sample : this.innerDistribution.getPmf()) {
            if (sample.getKey() > x) continue;
            probability += sample.getValue().doubleValue();
        }
        return probability;
    }

    public double getNumericalMean() {
        double mean = 0.0;
        for (Pair<Integer, Double> sample : this.innerDistribution.getPmf()) {
            mean += sample.getValue() * (double)sample.getKey().intValue();
        }
        return mean;
    }

    public double getNumericalVariance() {
        double mean = 0.0;
        double meanOfSquares = 0.0;
        for (Pair<Integer, Double> sample : this.innerDistribution.getPmf()) {
            mean += sample.getValue() * (double)sample.getKey().intValue();
            meanOfSquares += sample.getValue() * (double)sample.getKey().intValue() * (double)sample.getKey().intValue();
        }
        return meanOfSquares - mean * mean;
    }

    public int getSupportLowerBound() {
        int min = Integer.MAX_VALUE;
        for (Pair<Integer, Double> sample : this.innerDistribution.getPmf()) {
            if (sample.getKey() >= min || !(sample.getValue() > 0.0)) continue;
            min = sample.getKey();
        }
        return min;
    }

    public int getSupportUpperBound() {
        int max = Integer.MIN_VALUE;
        for (Pair<Integer, Double> sample : this.innerDistribution.getPmf()) {
            if (sample.getKey() <= max || !(sample.getValue() > 0.0)) continue;
            max = sample.getKey();
        }
        return max;
    }

    public boolean isSupportConnected() {
        return true;
    }

    public int sample() {
        return this.innerDistribution.sample();
    }
}

