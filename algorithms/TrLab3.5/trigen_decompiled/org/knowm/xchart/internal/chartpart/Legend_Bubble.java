/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Series_Bubble;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.Legend_;
import org.knowm.xchart.internal.chartpart.RenderableSeries;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler;

public class Legend_Bubble<ST extends AxesChartStyler, S extends Series>
extends Legend_ {
    AxesChartStyler stylerAxesChart;

    public Legend_Bubble(Chart<AxesChartStyler, XYSeries> chart) {
        super(chart);
        this.stylerAxesChart = chart.getStyler();
    }

    @Override
    public void doPaint(Graphics2D g) {
        double startx = this.xOffset + (double)((Styler)this.chart.getStyler()).getLegendPadding();
        double starty = this.yOffset + (double)((Styler)this.chart.getStyler()).getLegendPadding();
        Map map = this.chart.getSeriesMap();
        for (Series_Bubble series : map.values()) {
            if (!series.isShowInLegend()) continue;
            Map<String, Rectangle2D> seriesTextBounds = this.getSeriesTextBounds(series);
            float legendEntryHeight = this.getLegendEntryHeight(seriesTextBounds, 20);
            Ellipse2D.Double rectSmall = new Ellipse2D.Double(startx, starty, 20.0, 20.0);
            g.setColor(series.getFillColor());
            g.fill(rectSmall);
            g.setStroke(series.getLineStyle());
            g.setColor(series.getLineColor());
            g.draw(rectSmall);
            double x = startx + 20.0 + (double)((Styler)this.chart.getStyler()).getLegendPadding();
            this.paintSeriesText(g, seriesTextBounds, 20, x, starty);
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

