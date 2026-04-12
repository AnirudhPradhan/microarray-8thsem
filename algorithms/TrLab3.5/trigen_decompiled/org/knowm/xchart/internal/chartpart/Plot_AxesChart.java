/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.PlotSurface_AxesChart;
import org.knowm.xchart.internal.chartpart.Plot_;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler;

public class Plot_AxesChart<ST extends Styler, S extends Series>
extends Plot_ {
    AxesChartStyler stylerAxesChart;

    public Plot_AxesChart(Chart<AxesChartStyler, XYSeries> chart) {
        super(chart);
        this.stylerAxesChart = chart.getStyler();
        this.plotSurface = new PlotSurface_AxesChart(chart);
    }

    @Override
    public void paint(Graphics2D g) {
        double xOffset = this.chart.getYAxis().getBounds().getX() + this.chart.getYAxis().getBounds().getWidth() + (double)(this.stylerAxesChart.isYAxisTicksVisible() ? this.stylerAxesChart.getPlotMargin() : 0);
        double yOffset = this.chart.getYAxis().getBounds().getY();
        double width = this.chart.getXAxis().getBounds().getWidth();
        double height = this.chart.getYAxis().getBounds().getHeight();
        this.bounds = new Rectangle2D.Double(xOffset, yOffset, width, height);
        super.paint(g);
    }
}

