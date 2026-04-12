/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.knowm.xchart.PieSeries;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.Legend_;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.Styler;

public class Legend_Pie<ST extends AxesChartStyler, S extends Series>
extends Legend_ {
    PieStyler stylerPie;

    public Legend_Pie(Chart<PieStyler, PieSeries> chart) {
        super(chart);
        this.stylerPie = chart.getStyler();
    }

    @Override
    public void doPaint(Graphics2D g) {
        double startx = this.xOffset + (double)((Styler)this.chart.getStyler()).getLegendPadding();
        double starty = this.yOffset + (double)((Styler)this.chart.getStyler()).getLegendPadding();
        Map map = this.chart.getSeriesMap();
        for (Series series : map.values()) {
            if (!series.isShowInLegend()) continue;
            Map<String, Rectangle2D> seriesTextBounds = this.getSeriesTextBounds(series);
            float legendEntryHeight = this.getLegendEntryHeight(seriesTextBounds, 20);
            Rectangle2D.Double rectSmall = new Rectangle2D.Double(startx, starty, 20.0, 20.0);
            g.setColor(series.getFillColor());
            g.fill(rectSmall);
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
        return 20.0;
    }
}

