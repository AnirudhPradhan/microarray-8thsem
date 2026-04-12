/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data.filters;

import java.io.Serializable;
import java.util.Arrays;

public class Kernel
implements Serializable {
    private static final long serialVersionUID = 7721293471122850684L;
    private final double[] a;
    private final int b;

    public Kernel(int n, double[] dArray) {
        this.a = Arrays.copyOf(dArray, dArray.length);
        this.b = n;
    }

    public Kernel(double ... dArray) {
        this(dArray.length / 2, dArray);
    }

    public static Kernel getBinomial(double d) {
        int n = (int)(d * 4.0) + 1;
        return Kernel.getBinomial(n);
    }

    public static Kernel getBinomial(int n) {
        int n2;
        double[] dArray = new double[n];
        double[] dArray2 = dArray;
        dArray[0] = 1.0;
        for (n2 = 0; n2 < n - 1; ++n2) {
            dArray2[0] = dArray2[0] / 2.0;
        }
        for (n2 = 0; n2 < n; ++n2) {
            for (int i = n2; i > 0; --i) {
                int n3 = i;
                dArray2[n3] = dArray2[n3] + dArray2[i - 1];
            }
        }
        return new Kernel(dArray2);
    }

    public static Kernel getUniform(int n, int n2, double d) {
        double[] dArray = new double[n];
        Arrays.fill(dArray, d);
        return new Kernel(n2, dArray);
    }

    public double get(int n) {
        if (n < this.getMinIndex() || n > this.getMaxIndex()) {
            return 0.0;
        }
        return this.a[n - this.getMinIndex()];
    }

    protected void set(int n, double d) {
        if (n < this.getMinIndex() || n > this.getMaxIndex()) {
            return;
        }
        this.a[n - this.getMinIndex()] = d;
    }

    public int getOffset() {
        return this.b;
    }

    public int size() {
        return this.a.length;
    }

    public int getMinIndex() {
        return -this.getOffset();
    }

    public int getMaxIndex() {
        return this.size() - this.getOffset() - 1;
    }

    public Kernel add(double d) {
        int n = 0;
        while (n < this.a.length) {
            int n2 = n++;
            this.a[n2] = this.a[n2] + d;
        }
        return this;
    }

    public Kernel add(Kernel kernel) {
        int n = this.getMinIndex();
        int n2 = this.getMaxIndex();
        if (this.size() > kernel.size()) {
            n = kernel.getMinIndex();
            n2 = kernel.getMaxIndex();
        }
        while (n <= n2) {
            this.set(n, this.get(n) + kernel.get(n));
            ++n;
        }
        return this;
    }

    public Kernel mul(double d) {
        int n = 0;
        while (n < this.a.length) {
            int n2 = n++;
            this.a[n2] = this.a[n2] * d;
        }
        return this;
    }

    public Kernel mul(Kernel kernel) {
        int n = this.getMinIndex();
        int n2 = this.getMaxIndex();
        if (this.size() > kernel.size()) {
            n = kernel.getMinIndex();
            n2 = kernel.getMaxIndex();
        }
        while (n <= n2) {
            this.set(n, this.get(n) * kernel.get(n));
            ++n;
        }
        return this;
    }

    public Kernel normalize() {
        double d = 0.0;
        double[] dArray = this.a;
        int n = this.a.length;
        for (int i = 0; i < n; ++i) {
            double d2 = dArray[i];
            d += d2;
        }
        return this.mul(1.0 / d);
    }

    public Kernel negate() {
        this.mul(-1.0);
        return this;
    }
}

