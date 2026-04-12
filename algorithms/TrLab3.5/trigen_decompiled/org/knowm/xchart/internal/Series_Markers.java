/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal;

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.knowm.xchart.internal.Series_AxesChart;
import org.knowm.xchart.internal.chartpart.Axis;
import org.knowm.xchart.style.markers.Marker;

public abstract class Series_Markers
extends Series_AxesChart {
    private Marker marker;
    private Color markerColor;

    @Override
    public abstract Axis.AxisDataType getAxesType(List<?> var1);

    public Series_Markers(String name, List<?> xData, List<? extends Number> yData, List<? extends Number> errorBars) {
        super(name, xData, yData, errorBars);
        this.extraValues = errorBars;
        this.calculateMinMax();
    }

    @Override
    public void calculateMinMax() {
        double[] xMinMax = this.findMinMax(this.xData, this.xAxisType);
        this.xMin = xMinMax[0];
        this.xMax = xMinMax[1];
        double[] yMinMax = null;
        yMinMax = this.extraValues == null ? this.findMinMax(this.yData, this.yAxisType) : this.findMinMaxWithErrorBars(this.yData, this.extraValues);
        this.yMin = yMinMax[0];
        this.yMax = yMinMax[1];
    }

    private double[] findMinMaxWithErrorBars(Collection<? extends Number> data, Collection<? extends Number> errorBars) {
        double min = Double.MAX_VALUE;
        double max = -1.7976931348623157E308;
        Iterator<? extends Number> itr = data.iterator();
        Iterator<? extends Number> ebItr = errorBars.iterator();
        while (itr.hasNext()) {
            double eb;
            double bigDecimal = itr.next().doubleValue();
            if (bigDecimal - (eb = ebItr.next().doubleValue()) < min) {
                min = bigDecimal - eb;
            }
            if (!(bigDecimal + eb > max)) continue;
            max = bigDecimal + eb;
        }
        return new double[]{min, max};
    }

    public Series_Markers setMarker(Marker marker) {
        this.marker = marker;
        return this;
    }

    public Series_Markers setMarkerColor(Color color) {
        this.markerColor = color;
        return this;
    }

    public Marker getMarker() {
        return this.marker;
    }

    public Color getMarkerColor() {
        return this.markerColor;
    }
}

