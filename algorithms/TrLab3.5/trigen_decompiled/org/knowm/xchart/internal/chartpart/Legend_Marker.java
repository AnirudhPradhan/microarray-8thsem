/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Series_Markers;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.Legend_;
import org.knowm.xchart.internal.chartpart.RenderableSeries;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.lines.SeriesLines;

public class Legend_Marker<ST extends AxesChartStyler, S extends Series>
extends Legend_ {
    AxesChartStyler stylerAxesChart;

    public Legend_Marker(Chart<AxesChartStyler, XYSeries> chart) {
        super(chart);
        this.stylerAxesChart = chart.getStyler();
    }

    @Override
    public void doPaint(Graphics2D g) {
        double startx = this.xOffset + (double)((Styler)this.chart.getStyler()).getLegendPadding();
        double starty = this.yOffset + (double)((Styler)this.chart.getStyler()).getLegendPadding();
        Map map = this.chart.getSeriesMap();
        for (Series_Markers series : map.values()) {
            double x;
            if (!series.isShowInLegend()) continue;
            Map<String, Rectangle2D> seriesTextBounds = this.getSeriesTextBounds(series);
            float legendEntryHeight = this.getLegendEntryHeight(seriesTextBounds, series.getLegendRenderType() == RenderableSeries.LegendRenderType.Box ? 20 : this.stylerAxesChart.getMarkerSize());
            if (series.getLegendRenderType() != RenderableSeries.LegendRenderType.Box) {
                if (series.getLegendRenderType() == RenderableSeries.LegendRenderType.Line && series.getLineStyle() != SeriesLines.NONE) {
                    g.setColor(series.getLineColor());
                    g.setStroke(series.getLineStyle());
                    Line2D.Double line = new Line2D.Double(startx, starty + (double)legendEntryHeight / 2.0, startx + (double)((Styler)this.chart.getStyler()).getLegendSeriesLineLength(), starty + (double)legendEntryHeight / 2.0);
                    g.draw(line);
                }
                if (series.getMarker() != null) {
                    g.setColor(series.getMarkerColor());
                    series.getMarker().paint(g, startx + (double)((Styler)this.chart.getStyler()).getLegendSeriesLineLength() / 2.0, starty + (double)legendEntryHeight / 2.0, this.stylerAxesChart.getMarkerSize());
                }
            } else {
                Rectangle2D.Double rectSmall = new Rectangle2D.Double(startx, starty, 20.0, 20.0);
                g.setColor(series.getFillColor());
                g.fill(rectSmall);
            }
            if (series.getLegendRenderType() != RenderableSeries.LegendRenderType.Box) {
                x = startx + (double)((Styler)this.chart.getStyler()).getLegendSeriesLineLength() + (double)((Styler)this.chart.getStyler()).getLegendPadding();
                this.paintSeriesText(g, seriesTextBounds, this.stylerAxesChart.getMarkerSize(), x, starty);
            } else {
                x = startx + 20.0 + (double)((Styler)this.chart.getStyler()).getLegendPadding();
                this.paintSeriesText(g, seriesTextBounds, 20, x, starty);
            }
            starty += (double)(legendEntryHeight + (float)((Styler)this.chart.getStyler()).getLegendPadding());
        }
    }

    @Override
    public Rectangle2D getBounds() {
        if (this.bounds == null) {
            this.bounds = this.getBoundsHint();
        }
        return this.bounds;
    }

    @Override
    public double getSeriesLegendRenderGraphicHeight(Series series) {
        return series.getLegendRenderType() == RenderableSeries.LegendRenderType.Box ? 20.0 : (double)this.stylerAxesChart.getMarkerSize();
    }
}

