/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal;

import java.util.List;
import org.knowm.xchart.internal.Series_AxesChart;
import org.knowm.xchart.internal.chartpart.Axis;

public abstract class Series_Bubble
extends Series_AxesChart {
    @Override
    public abstract Axis.AxisDataType getAxesType(List<?> var1);

    public Series_Bubble(String name, List<?> xData, List<? extends Number> yData, List<? extends Number> extraValues) {
        super(name, xData, yData, extraValues);
        this.extraValues = extraValues;
        this.calculateMinMax();
    }

    @Override
    public void calculateMinMax() {
        double[] xMinMax = this.findMinMax(this.xData, this.xAxisType);
        this.xMin = xMinMax[0];
        this.xMax = xMinMax[1];
        double[] yMinMax = null;
        yMinMax = this.findMinMax(this.yData, this.yAxisType);
        this.yMin = yMinMax[0];
        this.yMax = yMinMax[1];
    }
}

