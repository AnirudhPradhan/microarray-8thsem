/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Series_AxesChart;
import org.knowm.xchart.internal.chartpart.Axis;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.ChartPart;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.CategoryStyler;

public class AxisPair<ST extends AxesChartStyler, S extends Series>
implements ChartPart {
    private final Chart<AxesChartStyler, Series_AxesChart> chart;
    private final Axis<AxesChartStyler, Series_AxesChart> xAxis;
    private final Axis<AxesChartStyler, Series_AxesChart> yAxis;

    public AxisPair(Chart<AxesChartStyler, Series_AxesChart> chart) {
        this.chart = chart;
        this.xAxis = new Axis(chart, Axis.Direction.X);
        this.yAxis = new Axis(chart, Axis.Direction.Y);
    }

    @Override
    public void paint(Graphics2D g) {
        this.prepareForPaint();
        this.yAxis.paint(g);
        this.xAxis.paint(g);
    }

    private void prepareForPaint() {
        this.xAxis.setAxisDataType(null);
        this.yAxis.setAxisDataType(null);
        for (Series_AxesChart series : this.chart.getSeriesMap().values()) {
            this.xAxis.setAxisDataType(series.getxAxisDataType());
            this.yAxis.setAxisDataType(series.getyAxisDataType());
        }
        this.xAxis.resetMinMax();
        this.yAxis.resetMinMax();
        if (this.chart.getSeriesMap() == null || this.chart.getSeriesMap().size() < 1) {
            this.xAxis.addMinMax(-1.0, 1.0);
            this.yAxis.addMinMax(-1.0, 1.0);
        } else {
            for (Series_AxesChart series : this.chart.getSeriesMap().values()) {
                this.xAxis.addMinMax(series.getXMin(), series.getXMax());
                this.yAxis.addMinMax(series.getYMin(), series.getYMax());
            }
        }
        this.overrideMinMax();
        if (this.chart.getStyler().isXAxisLogarithmic() && this.xAxis.getMin() <= 0.0) {
            throw new IllegalArgumentException("Series data (accounting for error bars too) cannot be less or equal to zero for a logarithmic X-Axis!!!");
        }
        if (this.chart.getStyler().isYAxisLogarithmic() && this.yAxis.getMin() <= 0.0) {
            throw new IllegalArgumentException("Series data (accounting for error bars too) cannot be less or equal to zero for a logarithmic Y-Axis!!!");
        }
    }

    public void overrideMinMax() {
        CategoryStyler stylerCategory;
        double overrideXAxisMinValue = this.xAxis.getMin();
        double overrideXAxisMaxValue = this.xAxis.getMax();
        double overrideYAxisMinValue = this.yAxis.getMin();
        double overrideYAxisMaxValue = this.yAxis.getMax();
        if (this.chart.getStyler() instanceof CategoryStyler && (stylerCategory = (CategoryStyler)this.chart.getStyler()).getDefaultSeriesRenderStyle() == CategorySeries.CategorySeriesRenderStyle.Bar) {
            if (this.yAxis.getMin() > 0.0) {
                overrideYAxisMinValue = 0.0;
            }
            if (this.yAxis.getMax() < 0.0) {
                overrideYAxisMaxValue = 0.0;
            }
        }
        if (this.chart.getStyler().getXAxisMin() != null) {
            overrideXAxisMinValue = this.chart.getStyler().getXAxisMin();
        }
        if (this.chart.getStyler().getXAxisMax() != null) {
            overrideXAxisMaxValue = this.chart.getStyler().getXAxisMax();
        }
        if (this.chart.getStyler().getYAxisMin() != null) {
            overrideYAxisMinValue = this.chart.getStyler().getYAxisMin();
        }
        if (this.chart.getStyler().getYAxisMax() != null) {
            overrideYAxisMaxValue = this.chart.getStyler().getYAxisMax();
        }
        this.xAxis.setMin(overrideXAxisMinValue);
        this.xAxis.setMax(overrideXAxisMaxValue);
        this.yAxis.setMin(overrideYAxisMinValue);
        this.yAxis.setMax(overrideYAxisMaxValue);
    }

    protected Axis<AxesChartStyler, Series_AxesChart> getXAxis() {
        return this.xAxis;
    }

    protected Axis<AxesChartStyler, Series_AxesChart> getYAxis() {
        return this.yAxis;
    }

    @Override
    public Rectangle2D getBounds() {
        return null;
    }
}

