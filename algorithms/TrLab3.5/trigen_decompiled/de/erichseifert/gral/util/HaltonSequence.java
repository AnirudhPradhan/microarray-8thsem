/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.util;

import java.io.Serializable;
import java.util.Iterator;

public class HaltonSequence
implements Serializable,
Iterator<Double> {
    private static final long serialVersionUID = 7466395251522942013L;
    private final int a;
    private long b;

    public HaltonSequence() {
        this(2);
    }

    public HaltonSequence(int n) {
        this.a = n;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Double next() {
        if (++this.b == Long.MAX_VALUE) {
            this.b = 0L;
        }
        long l = this.b;
        double d = 0.0;
        double d2 = 1.0 / (double)this.a;
        while (l > 0L) {
            long l2 = l % (long)this.a;
            d += (double)l2 * d2;
            l = (l - l2) / (long)this.a;
            d2 /= (double)this.a;
        }
        return d;
    }

    @Override
    public void remove() {
    }
}

