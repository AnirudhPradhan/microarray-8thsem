/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.util;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;

public class PointND<T extends Number>
implements Serializable {
    private static final long serialVersionUID = 3552680202450906771L;
    public static final int X = 0;
    public static final int Y = 1;
    public static final int Z = 2;
    private final T[] a;

    public PointND(T ... TArray) {
        this.a = (Number[])Arrays.copyOf(TArray, TArray.length);
    }

    public int getDimensions() {
        return this.a.length;
    }

    public T get(int n) {
        return this.a[n];
    }

    public void set(int n, T t) {
        this.a[n] = t;
    }

    public void setLocation(T ... TArray) {
        if (this.getDimensions() != TArray.length) {
            throw new IllegalArgumentException(MessageFormat.format("Wrong number of dimensions: Expected {0,number,integer} values, got {1,number,integer}.", this.getDimensions(), TArray.length));
        }
        System.arraycopy(TArray, 0, this.a, 0, this.getDimensions());
    }

    public Point2D getPoint2D(int n, int n2) {
        if (this.getDimensions() < 2) {
            throw new ArrayIndexOutOfBoundsException("Can't create two-dimensional point from " + this.getDimensions() + "D data.");
        }
        return new Point2D.Double(((Number)this.get(n)).doubleValue(), ((Number)this.get(n2)).doubleValue());
    }

    public Point2D getPoint2D() {
        return this.getPoint2D(0, 1);
    }

    public String toString() {
        return this.getClass().getName() + Arrays.deepToString(this.a);
    }

    public boolean equals(Object object) {
        if (!(object instanceof PointND)) {
            return false;
        }
        object = (PointND)object;
        if (this.getDimensions() != ((PointND)object).getDimensions()) {
            return false;
        }
        for (int i = 0; i < this.a.length; ++i) {
            T t = this.get(i);
            T t2 = ((PointND)object).get(i);
            if (!(t != null && t2 != null ? !t.equals(t2) : t != t2)) continue;
            return false;
        }
        return true;
    }

    public int hashCode() {
        int n = 0;
        T[] TArray = this.a;
        int n2 = this.a.length;
        for (int i = 0; i < n2; ++i) {
            T t = TArray[i];
            n ^= t.hashCode();
        }
        return n;
    }
}

