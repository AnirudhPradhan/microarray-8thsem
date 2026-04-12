/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.util;

import java.util.List;
import java.util.Random;

public abstract class MathUtils {
    private static final Random a = new Random();

    private MathUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean almostEqual(double d, double d2, double d3) {
        return Math.abs(d - d2) <= d3;
    }

    public static double round(double d, double d2) {
        if (d2 == 0.0) {
            return 0.0;
        }
        return (double)Math.round(d / d2) * d2;
    }

    public static double floor(double d, double d2) {
        if (d2 == 0.0) {
            return 0.0;
        }
        return Math.floor(d / d2) * d2;
    }

    public static double ceil(double d, double d2) {
        if (d2 == 0.0) {
            return 0.0;
        }
        return Math.ceil(d / d2) * d2;
    }

    public static int binarySearch(double[] dArray, double d) {
        int n;
        int n2 = 0;
        int n3 = dArray.length - 1;
        do {
            if (d > dArray[n = (int)(((long)n2 + (long)n3) / 2L)]) {
                n2 = n + 1;
                continue;
            }
            if (d < dArray[n]) {
                n3 = n - 1;
                continue;
            }
            return n;
        } while (n2 <= n3);
        return n;
    }

    public static int binarySearchFloor(double[] dArray, double d) {
        if (dArray.length == 0) {
            return -1;
        }
        int n = MathUtils.binarySearch(dArray, d);
        if (n >= 0 && dArray[n] > d) {
            --n;
        }
        return n;
    }

    public static int binarySearchCeil(double[] dArray, double d) {
        if (dArray.length == 0) {
            return -1;
        }
        int n = MathUtils.binarySearch(dArray, d);
        if (n >= 0 && dArray[n] < d) {
            ++n;
        }
        return n;
    }

    public static <T extends Number> T limit(T t, T t2, T t3) {
        if (t.doubleValue() > t3.doubleValue()) {
            return t3;
        }
        if (t.doubleValue() < t2.doubleValue()) {
            return t2;
        }
        return t;
    }

    public static double limit(double d, double d2, double d3) {
        if (d > d3) {
            return d3;
        }
        if (d < d2) {
            return d2;
        }
        return d;
    }

    public static float limit(float f, float f2, float f3) {
        if (f > f3) {
            return f3;
        }
        if (f < f2) {
            return f2;
        }
        return f;
    }

    public static int limit(int n, int n2, int n3) {
        if (n > n3) {
            return n3;
        }
        if (n < n2) {
            return n2;
        }
        return n;
    }

    public static <T extends Comparable<T>> int randomizedSelect(List<T> list, int n, int n2, int n3) {
        while (!list.isEmpty()) {
            if (n == n2) {
                return n;
            }
            int n4 = n2;
            int n5 = n;
            List<T> list2 = list;
            int n6 = n5 + a.nextInt(n4 - n5 + 1);
            MathUtils.a(list2, n4, n6);
            Comparable comparable = (Comparable)list2.get(n4);
            int n7 = n5 - 1;
            while (n5 < n4) {
                if (((Comparable)list2.get(n5)).compareTo(comparable) <= 0) {
                    MathUtils.a(list2, ++n7, n5);
                }
                ++n5;
            }
            MathUtils.a(list2, n7 + 1, n4);
            int n8 = n7 + 1;
            n5 = n8 - n + 1;
            if (n3 == n5) {
                return n8;
            }
            if (n3 < n5) {
                n2 = n8 - 1;
                continue;
            }
            n3 -= n5;
            n = n8 + 1;
        }
        return -1;
    }

    private static <T> void a(List<T> list, int n, int n2) {
        T t = list.get(n2);
        list.set(n2, list.get(n));
        list.set(n, t);
    }

    public static double magnitude(double d, double d2) {
        double d3 = Math.log(Math.abs(d2)) / Math.log(d);
        return Math.signum(d2) * Math.pow(d, Math.floor(d3));
    }

    public static double quantile(List<Double> list, double d) {
        int n = list.size();
        double d2 = 1.0 + ((double)n + -1.0) * d - 1.0;
        double d3 = (int)d2;
        double d4 = d2 - d3;
        if (d3 < 0.0) {
            return list.get(0);
        }
        if (d3 >= (double)n) {
            return list.get(n - 1);
        }
        int n2 = (int)d3;
        if (d4 == 0.0) {
            return list.get(n2);
        }
        return list.get(n2) + (list.get(n2 + 1) - list.get(n2)) * (0.0 + d4 * 1.0);
    }

    public static boolean isCalculatable(Number number) {
        return number != null && MathUtils.isCalculatable(number.doubleValue());
    }

    public static boolean isCalculatable(double d) {
        return !Double.isNaN(d) && !Double.isInfinite(d);
    }

    public static double normalizeDegrees(double d) {
        while (d < 0.0) {
            d += 360.0;
        }
        return d % 360.0;
    }
}

