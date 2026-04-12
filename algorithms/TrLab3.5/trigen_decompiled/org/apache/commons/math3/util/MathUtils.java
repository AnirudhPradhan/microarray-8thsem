/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.util;

import java.util.Arrays;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;

public final class MathUtils {
    public static final double TWO_PI = Math.PI * 2;

    private MathUtils() {
    }

    public static int hash(double value) {
        return new Double(value).hashCode();
    }

    public static boolean equals(double x, double y) {
        return new Double(x).equals(new Double(y));
    }

    public static int hash(double[] value) {
        return Arrays.hashCode(value);
    }

    public static double normalizeAngle(double a2, double center) {
        return a2 - Math.PI * 2 * FastMath.floor((a2 + Math.PI - center) / (Math.PI * 2));
    }

    public static double reduce(double a2, double period, double offset) {
        double p = FastMath.abs(period);
        return a2 - p * FastMath.floor((a2 - offset) / p) - offset;
    }

    public static byte copySign(byte magnitude, byte sign) throws MathArithmeticException {
        if (magnitude >= 0 && sign >= 0 || magnitude < 0 && sign < 0) {
            return magnitude;
        }
        if (sign >= 0 && magnitude == -128) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        }
        return -magnitude;
    }

    public static short copySign(short magnitude, short sign) throws MathArithmeticException {
        if (magnitude >= 0 && sign >= 0 || magnitude < 0 && sign < 0) {
            return magnitude;
        }
        if (sign >= 0 && magnitude == Short.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        }
        return -magnitude;
    }

    public static int copySign(int magnitude, int sign) throws MathArithmeticException {
        if (magnitude >= 0 && sign >= 0 || magnitude < 0 && sign < 0) {
            return magnitude;
        }
        if (sign >= 0 && magnitude == Integer.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        }
        return -magnitude;
    }

    public static long copySign(long magnitude, long sign) throws MathArithmeticException {
        if (magnitude >= 0L && sign >= 0L || magnitude < 0L && sign < 0L) {
            return magnitude;
        }
        if (sign >= 0L && magnitude == Long.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        }
        return -magnitude;
    }

    public static void checkFinite(double x) throws NotFiniteNumberException {
        if (Double.isInfinite(x) || Double.isNaN(x)) {
            throw new NotFiniteNumberException(x, new Object[0]);
        }
    }

    public static void checkFinite(double[] val) throws NotFiniteNumberException {
        for (int i = 0; i < val.length; ++i) {
            double x = val[i];
            if (!Double.isInfinite(x) && !Double.isNaN(x)) continue;
            throw new NotFiniteNumberException((Localizable)LocalizedFormats.ARRAY_ELEMENT, x, i);
        }
    }

    public static void checkNotNull(Object o, Localizable pattern, Object ... args) throws NullArgumentException {
        if (o == null) {
            throw new NullArgumentException(pattern, args);
        }
    }

    public static void checkNotNull(Object o) throws NullArgumentException {
        if (o == null) {
            throw new NullArgumentException();
        }
    }
}

