/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.stat.inference;

import java.util.Arrays;
import java.util.Iterator;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.InsufficientDataException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.fraction.BigFractionField;
import org.apache.commons.math3.fraction.FractionConversionException;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class KolmogorovSmirnovTest {
    protected static final int MAXIMUM_PARTIAL_SUM_COUNT = 100000;
    protected static final double KS_SUM_CAUCHY_CRITERION = 1.0E-20;
    protected static final int SMALL_SAMPLE_PRODUCT = 200;
    protected static final int LARGE_SAMPLE_PRODUCT = 10000;
    protected static final int MONTE_CARLO_ITERATIONS = 1000000;
    private final RandomGenerator rng;

    public KolmogorovSmirnovTest() {
        this.rng = new Well19937c();
    }

    public KolmogorovSmirnovTest(RandomGenerator rng) {
        this.rng = rng;
    }

    public double kolmogorovSmirnovTest(RealDistribution distribution, double[] data, boolean exact) {
        return 1.0 - this.cdf(this.kolmogorovSmirnovStatistic(distribution, data), data.length, exact);
    }

    public double kolmogorovSmirnovStatistic(RealDistribution distribution, double[] data) {
        this.checkArray(data);
        int n = data.length;
        double nd = n;
        double[] dataCopy = new double[n];
        System.arraycopy(data, 0, dataCopy, 0, n);
        Arrays.sort(dataCopy);
        double d = 0.0;
        for (int i = 1; i <= n; ++i) {
            double yi = distribution.cumulativeProbability(dataCopy[i - 1]);
            double currD = FastMath.max(yi - (double)(i - 1) / nd, (double)i / nd - yi);
            if (!(currD > d)) continue;
            d = currD;
        }
        return d;
    }

    public double kolmogorovSmirnovTest(double[] x, double[] y, boolean strict) {
        if (x.length * y.length < 200) {
            return this.exactP(this.kolmogorovSmirnovStatistic(x, y), x.length, y.length, strict);
        }
        if (x.length * y.length < 10000) {
            return this.monteCarloP(this.kolmogorovSmirnovStatistic(x, y), x.length, y.length, strict, 1000000);
        }
        return this.approximateP(this.kolmogorovSmirnovStatistic(x, y), x.length, y.length);
    }

    public double kolmogorovSmirnovTest(double[] x, double[] y) {
        return this.kolmogorovSmirnovTest(x, y, true);
    }

    public double kolmogorovSmirnovStatistic(double[] x, double[] y) {
        double curD;
        int i;
        this.checkArray(x);
        this.checkArray(y);
        double[] sx = MathArrays.copyOf(x);
        double[] sy = MathArrays.copyOf(y);
        Arrays.sort(sx);
        Arrays.sort(sy);
        int n = sx.length;
        int m = sy.length;
        double supD = 0.0;
        for (i = 0; i < n; ++i) {
            double cdf_x = ((double)i + 1.0) / (double)n;
            int yIndex = Arrays.binarySearch(sy, sx[i]);
            double cdf_y = yIndex >= 0 ? ((double)yIndex + 1.0) / (double)m : ((double)(-yIndex) - 1.0) / (double)m;
            curD = FastMath.abs(cdf_x - cdf_y);
            if (!(curD > supD)) continue;
            supD = curD;
        }
        for (i = 0; i < m; ++i) {
            double cdf_y = ((double)i + 1.0) / (double)m;
            int xIndex = Arrays.binarySearch(sx, sy[i]);
            double cdf_x = xIndex >= 0 ? ((double)xIndex + 1.0) / (double)n : ((double)(-xIndex) - 1.0) / (double)n;
            curD = FastMath.abs(cdf_x - cdf_y);
            if (!(curD > supD)) continue;
            supD = curD;
        }
        return supD;
    }

    public double kolmogorovSmirnovTest(RealDistribution distribution, double[] data) {
        return this.kolmogorovSmirnovTest(distribution, data, false);
    }

    public boolean kolmogorovSmirnovTest(RealDistribution distribution, double[] data, double alpha) {
        if (alpha <= 0.0 || alpha > 0.5) {
            throw new OutOfRangeException((Localizable)LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, (Number)alpha, 0, 0.5);
        }
        return this.kolmogorovSmirnovTest(distribution, data) < alpha;
    }

    public double cdf(double d, int n) throws MathArithmeticException {
        return this.cdf(d, n, false);
    }

    public double cdfExact(double d, int n) throws MathArithmeticException {
        return this.cdf(d, n, true);
    }

    public double cdf(double d, int n, boolean exact) throws MathArithmeticException {
        double ninv = 1.0 / (double)n;
        double ninvhalf = 0.5 * ninv;
        if (d <= ninvhalf) {
            return 0.0;
        }
        if (ninvhalf < d && d <= ninv) {
            double res = 1.0;
            double f = 2.0 * d - ninv;
            for (int i = 1; i <= n; ++i) {
                res *= (double)i * f;
            }
            return res;
        }
        if (1.0 - ninv <= d && d < 1.0) {
            return 1.0 - 2.0 * Math.pow(1.0 - d, n);
        }
        if (1.0 <= d) {
            return 1.0;
        }
        return exact ? this.exactK(d, n) : this.roundedK(d, n);
    }

    private double exactK(double d, int n) throws MathArithmeticException {
        int k = (int)Math.ceil((double)n * d);
        FieldMatrix<BigFraction> H = this.createH(d, n);
        FieldMatrix<BigFraction> Hpower = H.power(n);
        BigFraction pFrac = Hpower.getEntry(k - 1, k - 1);
        for (int i = 1; i <= n; ++i) {
            pFrac = pFrac.multiply(i).divide(n);
        }
        return pFrac.bigDecimalValue(20, 4).doubleValue();
    }

    private double roundedK(double d, int n) throws MathArithmeticException {
        int k = (int)Math.ceil((double)n * d);
        FieldMatrix<BigFraction> HBigFraction = this.createH(d, n);
        int m = HBigFraction.getRowDimension();
        Array2DRowRealMatrix H = new Array2DRowRealMatrix(m, m);
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < m; ++j) {
                H.setEntry(i, j, HBigFraction.getEntry(i, j).doubleValue());
            }
        }
        RealMatrix Hpower = H.power(n);
        double pFrac = Hpower.getEntry(k - 1, k - 1);
        for (int i = 1; i <= n; ++i) {
            pFrac *= (double)i / (double)n;
        }
        return pFrac;
    }

    private FieldMatrix<BigFraction> createH(double d, int n) throws NumberIsTooLargeException, FractionConversionException {
        int i;
        int k = (int)Math.ceil((double)n * d);
        int m = 2 * k - 1;
        double hDouble = (double)k - (double)n * d;
        if (hDouble >= 1.0) {
            throw new NumberIsTooLargeException(hDouble, (Number)1.0, false);
        }
        BigFraction h = null;
        try {
            h = new BigFraction(hDouble, 1.0E-20, 10000);
        }
        catch (FractionConversionException e1) {
            try {
                h = new BigFraction(hDouble, 1.0E-10, 10000);
            }
            catch (FractionConversionException e2) {
                h = new BigFraction(hDouble, 1.0E-5, 10000);
            }
        }
        FieldElement[][] Hdata = new BigFraction[m][m];
        for (int i2 = 0; i2 < m; ++i2) {
            for (int j = 0; j < m; ++j) {
                Hdata[i2][j] = i2 - j + 1 < 0 ? BigFraction.ZERO : BigFraction.ONE;
            }
        }
        BigFraction[] hPowers = new BigFraction[m];
        hPowers[0] = h;
        for (i = 1; i < m; ++i) {
            hPowers[i] = h.multiply(hPowers[i - 1]);
        }
        for (i = 0; i < m; ++i) {
            Hdata[i][0] = Hdata[i][0].subtract(hPowers[i]);
            Hdata[m - 1][i] = ((BigFraction)Hdata[m - 1][i]).subtract(hPowers[m - i - 1]);
        }
        if (h.compareTo(BigFraction.ONE_HALF) == 1) {
            Hdata[m - 1][0] = ((BigFraction)Hdata[m - 1][0]).add(h.multiply(2).subtract(1).pow(m));
        }
        for (i = 0; i < m; ++i) {
            for (int j = 0; j < i + 1; ++j) {
                if (i - j + 1 <= 0) continue;
                for (int g = 2; g <= i - j + 1; ++g) {
                    Hdata[i][j] = ((BigFraction)Hdata[i][j]).divide(g);
                }
            }
        }
        return new Array2DRowFieldMatrix((Field)BigFractionField.getInstance(), Hdata);
    }

    private void checkArray(double[] array) {
        if (array == null) {
            throw new NullArgumentException(LocalizedFormats.NULL_NOT_ALLOWED, new Object[0]);
        }
        if (array.length < 2) {
            throw new InsufficientDataException(LocalizedFormats.INSUFFICIENT_OBSERVED_POINTS_IN_SAMPLE, array.length, 2);
        }
    }

    public double ksSum(double t, double tolerance, int maxIterations) {
        long i;
        double x = -2.0 * t * t;
        int sign = -1;
        double partialSum = 0.5;
        double delta = 1.0;
        for (i = 1L; delta > tolerance && i < (long)maxIterations; ++i) {
            delta = FastMath.exp(x * (double)i * (double)i);
            partialSum += (double)sign * delta;
            sign *= -1;
        }
        if (i == (long)maxIterations) {
            throw new TooManyIterationsException(maxIterations);
        }
        return partialSum * 2.0;
    }

    public double exactP(double d, int n, int m, boolean strict) {
        Iterator<int[]> combinationsIterator = CombinatoricsUtils.combinationsIterator(n + m, n);
        long tail = 0L;
        double[] nSet = new double[n];
        double[] mSet = new double[m];
        while (combinationsIterator.hasNext()) {
            int[] nSetI = combinationsIterator.next();
            int j = 0;
            int k = 0;
            for (int i = 0; i < n + m; ++i) {
                if (j < n && nSetI[j] == i) {
                    nSet[j++] = i;
                    continue;
                }
                mSet[k++] = i;
            }
            double curD = this.kolmogorovSmirnovStatistic(nSet, mSet);
            if (curD > d) {
                ++tail;
                continue;
            }
            if (curD != d || strict) continue;
            ++tail;
        }
        return (double)tail / (double)CombinatoricsUtils.binomialCoefficient(n + m, n);
    }

    public double approximateP(double d, int n, int m) {
        double dm = m;
        double dn = n;
        return 1.0 - this.ksSum(d * FastMath.sqrt(dm * dn / (dm + dn)), 1.0E-20, 100000);
    }

    public double monteCarloP(double d, int n, int m, boolean strict, int iterations) {
        int[] nPlusMSet = MathArrays.natural(m + n);
        double[] nSet = new double[n];
        double[] mSet = new double[m];
        int tail = 0;
        for (int i = 0; i < iterations; ++i) {
            this.copyPartition(nSet, mSet, nPlusMSet, n, m);
            double curD = this.kolmogorovSmirnovStatistic(nSet, mSet);
            if (curD > d) {
                ++tail;
            } else if (curD == d && !strict) {
                ++tail;
            }
            MathArrays.shuffle(nPlusMSet, this.rng);
            Arrays.sort(nPlusMSet, 0, n);
        }
        return (double)tail / (double)iterations;
    }

    private void copyPartition(double[] nSet, double[] mSet, int[] nSetI, int n, int m) {
        int j = 0;
        int k = 0;
        for (int i = 0; i < n + m; ++i) {
            if (j < n && nSetI[j] == i) {
                nSet[j++] = i;
                continue;
            }
            mSet[k++] = i;
        }
    }
}

