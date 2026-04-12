/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NullArgumentException;

public class Incrementor {
    private int maximalCount;
    private int count = 0;
    private final MaxCountExceededCallback maxCountCallback;

    public Incrementor() {
        this(0);
    }

    public Incrementor(int max) {
        this(max, new MaxCountExceededCallback(){

            public void trigger(int max) throws MaxCountExceededException {
                throw new MaxCountExceededException(max);
            }
        });
    }

    public Incrementor(int max, MaxCountExceededCallback cb) throws NullArgumentException {
        if (cb == null) {
            throw new NullArgumentException();
        }
        this.maximalCount = max;
        this.maxCountCallback = cb;
    }

    public void setMaximalCount(int max) {
        this.maximalCount = max;
    }

    public int getMaximalCount() {
        return this.maximalCount;
    }

    public int getCount() {
        return this.count;
    }

    public boolean canIncrement() {
        return this.count < this.maximalCount;
    }

    public void incrementCount(int value) throws MaxCountExceededException {
        for (int i = 0; i < value; ++i) {
            this.incrementCount();
        }
    }

    public void incrementCount() throws MaxCountExceededException {
        if (++this.count > this.maximalCount) {
            this.maxCountCallback.trigger(this.maximalCount);
        }
    }

    public void resetCount() {
        this.count = 0;
    }

    public static interface MaxCountExceededCallback {
        public void trigger(int var1) throws MaxCountExceededException;
    }
}

