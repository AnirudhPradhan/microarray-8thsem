/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.ml.neuralnet;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.Precision;

public class Neuron
implements Serializable {
    private static final long serialVersionUID = 20130207L;
    private final long identifier;
    private final int size;
    private final AtomicReference<double[]> features;

    Neuron(long identifier, double[] features) {
        this.identifier = identifier;
        this.size = features.length;
        this.features = new AtomicReference<Object>(features.clone());
    }

    public long getIdentifier() {
        return this.identifier;
    }

    public int getSize() {
        return this.size;
    }

    public double[] getFeatures() {
        return (double[])this.features.get().clone();
    }

    public boolean compareAndSetFeatures(double[] expect, double[] update) {
        if (update.length != this.size) {
            throw new DimensionMismatchException(update.length, this.size);
        }
        double[] current = this.features.get();
        if (!this.containSameValues(current, expect)) {
            return false;
        }
        return this.features.compareAndSet(current, (double[])update.clone());
    }

    private boolean containSameValues(double[] current, double[] expect) {
        if (expect.length != this.size) {
            throw new DimensionMismatchException(expect.length, this.size);
        }
        for (int i = 0; i < this.size; ++i) {
            if (Precision.equals(current[i], expect[i])) continue;
            return false;
        }
        return true;
    }

    private void readObject(ObjectInputStream in) {
        throw new IllegalStateException();
    }

    private Object writeReplace() {
        return new SerializationProxy(this.identifier, this.features.get());
    }

    private static class SerializationProxy
    implements Serializable {
        private static final long serialVersionUID = 20130207L;
        private final double[] features;
        private final long identifier;

        SerializationProxy(long identifier, double[] features) {
            this.identifier = identifier;
            this.features = features;
        }

        private Object readResolve() {
            return new Neuron(this.identifier, this.features);
        }
    }
}

