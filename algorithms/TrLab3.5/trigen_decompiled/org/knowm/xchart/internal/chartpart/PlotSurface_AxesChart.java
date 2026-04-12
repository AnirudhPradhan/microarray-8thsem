/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.PlotSurface_;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler;

public class PlotSurface_AxesChart<ST extends Styler, S extends Series>
extends PlotSurface_ {
    private final AxesChartStyler stylerAxesChart;

    protected PlotSurface_AxesChart(Chart<AxesChartStyler, XYSeries> chart) {
        super(chart);
        this.stylerAxesChart = chart.getStyler();
    }

    @Override
    public void paint(Graphics2D g) {
        int i;
        Rectangle2D bounds = this.getBounds();
        Rectangle2D.Double rect = new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
        g.setColor(this.stylerAxesChart.getPlotBackgroundColor());
        g.fill(rect);
        if (this.stylerAxesChart.isPlotBorderVisible()) {
            g.setColor(this.stylerAxesChart.getPlotBorderColor());
            g.draw(rect);
        }
        if (this.stylerAxesChart.isPlotGridHorizontalLinesVisible() || this.stylerAxesChart.isPlotTicksMarksVisible()) {
            List<Double> yAxisTickLocations = this.chart.getYAxis().getAxisTickCalculator().getTickLocations();
            for (i = 0; i < yAxisTickLocations.size(); ++i) {
                Line2D.Double line;
                double yOffset = bounds.getY() + bounds.getHeight() - yAxisTickLocations.get(i);
                if (!(yOffset > bounds.getY()) || !(yOffset < bounds.getY() + bounds.getHeight())) continue;
                if (this.stylerAxesChart.isPlotGridHorizontalLinesVisible()) {
                    g.setColor(this.stylerAxesChart.getPlotGridLinesColor());
                    g.setStroke(this.stylerAxesChart.getPlotGridLinesStroke());
                    line = new Line2D.Double(bounds.getX(), yOffset, bounds.getX() + bounds.getWidth(), yOffset);
                    g.draw(line);
                }
                if (!this.stylerAxesChart.isPlotTicksMarksVisible()) continue;
                g.setColor(this.stylerAxesChart.getAxisTickMarksColor());
                g.setStroke(this.stylerAxesChart.getAxisTickMarksStroke());
                line = new Line2D.Double(bounds.getX(), yOffset, bounds.getX() + (double)this.stylerAxesChart.getAxisTickMarkLength(), yOffset);
                g.draw(line);
                line = new Line2D.Double(bounds.getX() + bounds.getWidth(), yOffset, bounds.getX() + bounds.getWidth() - (double)this.stylerAxesChart.getAxisTickMarkLength(), yOffset);
                g.draw(line);
            }
        }
        if (this.stylerAxesChart.isPlotGridVerticalLinesVisible() || this.stylerAxesChart.isPlotTicksMarksVisible()) {
            List<Double> xAxisTickLocations = this.chart.getXAxis().getAxisTickCalculator().getTickLocations();
            for (i = 0; i < xAxisTickLocations.size(); ++i) {
                Line2D.Double line;
                double tickLocation = xAxisTickLocations.get(i);
                double xOffset = bounds.getX() + tickLocation;
                if (!(xOffset > bounds.getX()) || !(xOffset < bounds.getX() + bounds.getWidth())) continue;
                if (this.stylerAxesChart.isPlotGridVerticalLinesVisible()) {
                    g.setColor(this.stylerAxesChart.getPlotGridLinesColor());
                    g.setStroke(this.stylerAxesChart.getPlotGridLinesStroke());
                    line = new Line2D.Double(xOffset, bounds.getY(), xOffset, bounds.getY() + bounds.getHeight());
                    g.draw(line);
                }
                if (!this.stylerAxesChart.isPlotTicksMarksVisible()) continue;
                g.setColor(this.stylerAxesChart.getAxisTickMarksColor());
                g.setStroke(this.stylerAxesChart.getAxisTickMarksStroke());
                line = new Line2D.Double(xOffset, bounds.getY(), xOffset, bounds.getY() + (double)this.stylerAxesChart.getAxisTickMarkLength());
                g.draw(line);
                line = new Line2D.Double(xOffset, bounds.getY() + bounds.getHeight(), xOffset, bounds.getY() + bounds.getHeight() - (double)this.stylerAxesChart.getAxisTickMarkLength());
                g.draw(line);
            }
        }
    }
}

