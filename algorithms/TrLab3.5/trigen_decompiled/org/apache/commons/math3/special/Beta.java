/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.special;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.util.FastMath;

public class Beta {
    private static final double DEFAULT_EPSILON = 1.0E-14;
    private static final double HALF_LOG_TWO_PI = 0.9189385332046727;
    private static final double[] DELTA = new double[]{0.08333333333333333, -2.777777777777778E-5, 7.936507936507937E-8, -5.952380952380953E-10, 8.417508417508329E-12, -1.917526917518546E-13, 6.410256405103255E-15, -2.955065141253382E-16, 1.7964371635940225E-17, -1.3922896466162779E-18, 1.338028550140209E-19, -1.542460098679661E-20, 1.9770199298095743E-21, -2.3406566479399704E-22, 1.713480149663986E-23};

    private Beta() {
    }

    public static double regularizedBeta(double x, double a2, double b2) {
        return Beta.regularizedBeta(x, a2, b2, 1.0E-14, Integer.MAX_VALUE);
    }

    public static double regularizedBeta(double x, double a2, double b2, double epsilon) {
        return Beta.regularizedBeta(x, a2, b2, epsilon, Integer.MAX_VALUE);
    }

    public static double regularizedBeta(double x, double a2, double b2, int maxIterations) {
        return Beta.regularizedBeta(x, a2, b2, 1.0E-14, maxIterations);
    }

    public static double regularizedBeta(double x, final double a2, final double b2, double epsilon, int maxIterations) {
        double ret;
        if (Double.isNaN(x) || Double.isNaN(a2) || Double.isNaN(b2) || x < 0.0 || x > 1.0 || a2 <= 0.0 || b2 <= 0.0) {
            ret = Double.NaN;
        } else if (x > (a2 + 1.0) / (2.0 + b2 + a2) && 1.0 - x <= (b2 + 1.0) / (2.0 + b2 + a2)) {
            ret = 1.0 - Beta.regularizedBeta(1.0 - x, b2, a2, epsilon, maxIterations);
        } else {
            ContinuedFraction fraction = new ContinuedFraction(){

                protected double getB(int n, double x) {
                    double ret;
                    if (n % 2 == 0) {
                        double m = (double)n / 2.0;
                        ret = m * (b2 - m) * x / ((a2 + 2.0 * m - 1.0) * (a2 + 2.0 * m));
                    } else {
                        double m = ((double)n - 1.0) / 2.0;
                        ret = -((a2 + m) * (a2 + b2 + m) * x) / ((a2 + 2.0 * m) * (a2 + 2.0 * m + 1.0));
                    }
                    return ret;
                }

                protected double getA(int n, double x) {
                    return 1.0;
                }
            };
            ret = FastMath.exp(a2 * FastMath.log(x) + b2 * FastMath.log1p(-x) - FastMath.log(a2) - Beta.logBeta(a2, b2)) * 1.0 / fraction.evaluate(x, epsilon, maxIterations);
        }
        return ret;
    }

    @Deprecated
    public static double logBeta(double a2, double b2, double epsilon, int maxIterations) {
        return Beta.logBeta(a2, b2);
    }

    private static double logGammaSum(double a2, double b2) throws OutOfRangeException {
        if (a2 < 1.0 || a2 > 2.0) {
            throw new OutOfRangeException(a2, (Number)1.0, 2.0);
        }
        if (b2 < 1.0 || b2 > 2.0) {
            throw new OutOfRangeException(b2, (Number)1.0, 2.0);
        }
        double x = a2 - 1.0 + (b2 - 1.0);
        if (x <= 0.5) {
            return Gamma.logGamma1p(1.0 + x);
        }
        if (x <= 1.5) {
            return Gamma.logGamma1p(x) + FastMath.log1p(x);
        }
        return Gamma.logGamma1p(x - 1.0) + FastMath.log(x * (1.0 + x));
    }

    private static double logGammaMinusLogGammaSum(double a2, double b2) throws NumberIsTooSmallException {
        double w;
        double d;
        if (a2 < 0.0) {
            throw new NumberIsTooSmallException(a2, (Number)0.0, true);
        }
        if (b2 < 10.0) {
            throw new NumberIsTooSmallException(b2, (Number)10.0, true);
        }
        if (a2 <= b2) {
            d = b2 + (a2 - 0.5);
            w = Beta.deltaMinusDeltaSum(a2, b2);
        } else {
            d = a2 + (b2 - 0.5);
            w = Beta.deltaMinusDeltaSum(b2, a2);
        }
        double u = d * FastMath.log1p(a2 / b2);
        double v = a2 * (FastMath.log(b2) - 1.0);
        return u <= v ? w - u - v : w - v - u;
    }

    private static double deltaMinusDeltaSum(double a2, double b2) throws OutOfRangeException, NumberIsTooSmallException {
        if (a2 < 0.0 || a2 > b2) {
            throw new OutOfRangeException(a2, (Number)0, b2);
        }
        if (b2 < 10.0) {
            throw new NumberIsTooSmallException(b2, (Number)10, true);
        }
        double h = a2 / b2;
        double p = h / (1.0 + h);
        double q = 1.0 / (1.0 + h);
        double q2 = q * q;
        double[] s = new double[DELTA.length];
        s[0] = 1.0;
        for (int i = 1; i < s.length; ++i) {
            s[i] = 1.0 + (q + q2 * s[i - 1]);
        }
        double sqrtT = 10.0 / b2;
        double t = sqrtT * sqrtT;
        double w = DELTA[DELTA.length - 1] * s[s.length - 1];
        for (int i = DELTA.length - 2; i >= 0; --i) {
            w = t * w + DELTA[i] * s[i];
        }
        return w * p / b2;
    }

    private static double sumDeltaMinusDeltaSum(double p, double q) {
        if (p < 10.0) {
            throw new NumberIsTooSmallException(p, (Number)10.0, true);
        }
        if (q < 10.0) {
            throw new NumberIsTooSmallException(q, (Number)10.0, true);
        }
        double a2 = FastMath.min(p, q);
        double b2 = FastMath.max(p, q);
        double sqrtT = 10.0 / a2;
        double t = sqrtT * sqrtT;
        double z = DELTA[DELTA.length - 1];
        for (int i = DELTA.length - 2; i >= 0; --i) {
            z = t * z + DELTA[i];
        }
        return z / a2 + Beta.deltaMinusDeltaSum(a2, b2);
    }

    public static double logBeta(double p, double q) {
        if (Double.isNaN(p) || Double.isNaN(q) || p <= 0.0 || q <= 0.0) {
            return Double.NaN;
        }
        double a2 = FastMath.min(p, q);
        double b2 = FastMath.max(p, q);
        if (a2 >= 10.0) {
            double v;
            double w = Beta.sumDeltaMinusDeltaSum(a2, b2);
            double h = a2 / b2;
            double c2 = h / (1.0 + h);
            double u = -(a2 - 0.5) * FastMath.log(c2);
            if (u <= (v = b2 * FastMath.log1p(h))) {
                return -0.5 * FastMath.log(b2) + 0.9189385332046727 + w - u - v;
            }
            return -0.5 * FastMath.log(b2) + 0.9189385332046727 + w - v - u;
        }
        if (a2 > 2.0) {
            if (b2 > 1000.0) {
                int n = (int)FastMath.floor(a2 - 1.0);
                double prod = 1.0;
                double ared = a2;
                for (int i = 0; i < n; ++i) {
                    prod *= (ared -= 1.0) / (1.0 + ared / b2);
                }
                return FastMath.log(prod) - (double)n * FastMath.log(b2) + (Gamma.logGamma(ared) + Beta.logGammaMinusLogGammaSum(ared, b2));
            }
            double prod1 = 1.0;
            double ared = a2;
            while (ared > 2.0) {
                double h = (ared -= 1.0) / b2;
                prod1 *= h / (1.0 + h);
            }
            if (b2 < 10.0) {
                double prod2 = 1.0;
                double bred = b2;
                while (bred > 2.0) {
                    prod2 *= (bred -= 1.0) / (ared + bred);
                }
                return FastMath.log(prod1) + FastMath.log(prod2) + (Gamma.logGamma(ared) + (Gamma.logGamma(bred) - Beta.logGammaSum(ared, bred)));
            }
            return FastMath.log(prod1) + Gamma.logGamma(ared) + Beta.logGammaMinusLogGammaSum(ared, b2);
        }
        if (a2 >= 1.0) {
            if (b2 > 2.0) {
                if (b2 < 10.0) {
                    double prod = 1.0;
                    double bred = b2;
                    while (bred > 2.0) {
                        prod *= (bred -= 1.0) / (a2 + bred);
                    }
                    return FastMath.log(prod) + (Gamma.logGamma(a2) + (Gamma.logGamma(bred) - Beta.logGammaSum(a2, bred)));
                }
                return Gamma.logGamma(a2) + Beta.logGammaMinusLogGammaSum(a2, b2);
            }
            return Gamma.logGamma(a2) + Gamma.logGamma(b2) - Beta.logGammaSum(a2, b2);
        }
        if (b2 >= 10.0) {
            return Gamma.logGamma(a2) + Beta.logGammaMinusLogGammaSum(a2, b2);
        }
        return FastMath.log(Gamma.gamma(a2) * Gamma.gamma(b2) / Gamma.gamma(a2 + b2));
    }
}

