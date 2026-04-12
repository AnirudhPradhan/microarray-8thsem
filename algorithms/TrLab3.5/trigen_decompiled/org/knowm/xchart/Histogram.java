/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Histogram {
    private List<Double> xAxisData;
    private List<Double> yAxisData;
    private final Collection<? extends Number> originalData;
    private final int numBins;
    private final double min;
    private final double max;

    public Histogram(Collection<? extends Number> data, int numBins) {
        this.numBins = numBins;
        this.originalData = data;
        Double tempMax = -1.7976931348623157E308;
        Double tempMin = Double.MAX_VALUE;
        for (Number number : data) {
            double value = number.doubleValue();
            if (value > tempMax) {
                tempMax = value;
            }
            if (!(value < tempMin)) continue;
            tempMin = value;
        }
        this.max = tempMax;
        this.min = tempMin;
        this.init();
    }

    public Histogram(Collection<? extends Number> data, int numBins, double min, double max) {
        this.numBins = numBins;
        this.originalData = data;
        this.min = min;
        this.max = max;
        this.init();
    }

    private void init() {
        double[] tempYAxisData = new double[this.numBins];
        double binSize = (this.max - this.min) / (double)this.numBins;
        Iterator<? extends Number> itr = this.originalData.iterator();
        while (itr.hasNext()) {
            double doubleValue = itr.next().doubleValue();
            int bin = (int)((doubleValue - this.min) / binSize);
            if (bin < 0) continue;
            if (doubleValue == this.max) {
                int n = bin - 1;
                tempYAxisData[n] = tempYAxisData[n] + 1.0;
                continue;
            }
            if (bin > this.numBins || bin == this.numBins) continue;
            int n = bin;
            tempYAxisData[n] = tempYAxisData[n] + 1.0;
        }
        this.yAxisData = new ArrayList<Double>(this.numBins);
        for (double d : tempYAxisData) {
            this.yAxisData.add(d);
        }
        this.xAxisData = new ArrayList<Double>(this.numBins);
        for (int i = 0; i < this.numBins; ++i) {
            this.xAxisData.add((double)i * (this.max - this.min) / (double)this.numBins + this.min + binSize / 2.0);
        }
    }

    public List<Double> getxAxisData() {
        return this.xAxisData;
    }

    public List<Double> getyAxisData() {
        return this.yAxisData;
    }

    public Collection<? extends Number> getOriginalData() {
        return this.originalData;
    }

    public int getNumBins() {
        return this.numBins;
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }
}

