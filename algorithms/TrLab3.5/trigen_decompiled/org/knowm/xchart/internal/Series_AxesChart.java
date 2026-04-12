/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.Axis;

public abstract class Series_AxesChart
extends Series {
    List<?> xData;
    Axis.AxisDataType xAxisType;
    List<? extends Number> yData;
    Axis.AxisDataType yAxisType;
    List<? extends Number> extraValues;
    double xMin;
    double xMax;
    double yMin;
    double yMax;
    BasicStroke stroke;
    Color lineColor;
    float lineWidth = -1.0f;

    public abstract Axis.AxisDataType getAxesType(List<?> var1);

    public abstract void calculateMinMax();

    public Series_AxesChart(String name, List<?> xData, List<? extends Number> yData, List<? extends Number> errorBars) {
        super(name);
        this.xData = xData;
        this.xAxisType = this.getAxesType(xData);
        this.yData = yData;
        this.yAxisType = Axis.AxisDataType.Number;
        this.calculateMinMax();
    }

    public void replaceData(List<?> newXData, List<? extends Number> newYData, List<? extends Number> newExtraValues) {
        if (newExtraValues != null && newExtraValues.size() != newYData.size()) {
            throw new IllegalArgumentException("error bars and Y-Axis sizes are not the same!!!");
        }
        if (newXData.size() != newYData.size()) {
            throw new IllegalArgumentException("X and Y-Axis sizes are not the same!!!");
        }
        this.xData = newXData;
        this.yData = newYData;
        this.extraValues = newExtraValues;
        this.calculateMinMax();
    }

    double[] findMinMax(Collection<?> data, Axis.AxisDataType axisType) {
        double min = Double.MAX_VALUE;
        double max = -1.7976931348623157E308;
        for (Object dataPoint : data) {
            if (dataPoint == null) continue;
            double value = 0.0;
            if (axisType == Axis.AxisDataType.Number) {
                value = ((Number)dataPoint).doubleValue();
            } else if (axisType == Axis.AxisDataType.Date) {
                Date date = (Date)dataPoint;
                value = date.getTime();
            } else if (axisType == Axis.AxisDataType.String) {
                return new double[]{Double.NaN, Double.NaN};
            }
            if (value < min) {
                min = value;
            }
            if (!(value > max)) continue;
            max = value;
        }
        return new double[]{min, max};
    }

    public Series_AxesChart setLineStyle(BasicStroke basicStroke) {
        this.stroke = basicStroke;
        if (this.lineWidth > 0.0f) {
            this.stroke = new BasicStroke(this.lineWidth, this.stroke.getEndCap(), this.stroke.getLineJoin(), this.stroke.getMiterLimit(), this.stroke.getDashArray(), this.stroke.getDashPhase());
        }
        return this;
    }

    public Series_AxesChart setLineColor(Color color) {
        this.lineColor = color;
        return this;
    }

    public Series_AxesChart setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public Collection<?> getXData() {
        return this.xData;
    }

    public Axis.AxisDataType getxAxisDataType() {
        return this.xAxisType;
    }

    public Collection<? extends Number> getYData() {
        return this.yData;
    }

    public Axis.AxisDataType getyAxisDataType() {
        return this.yAxisType;
    }

    public Collection<? extends Number> getExtraValues() {
        return this.extraValues;
    }

    public double getXMin() {
        return this.xMin;
    }

    public double getXMax() {
        return this.xMax;
    }

    public double getYMin() {
        return this.yMin;
    }

    public double getYMax() {
        return this.yMax;
    }

    public BasicStroke getLineStyle() {
        return this.stroke;
    }

    public Color getLineColor() {
        return this.lineColor;
    }

    public float getLineWidth() {
        return this.lineWidth;
    }
}

