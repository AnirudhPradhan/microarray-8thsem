/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.PieSeries;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.PlotSurface_;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.Styler;

public class PlotSurface_Pie<ST extends Styler, S extends Series>
extends PlotSurface_ {
    private final PieStyler stylerPie;

    protected PlotSurface_Pie(Chart<PieStyler, PieSeries> chart) {
        super(chart);
        this.stylerPie = chart.getStyler();
    }

    @Override
    public void paint(Graphics2D g) {
        Rectangle2D bounds = this.getBounds();
        Rectangle2D.Double rect = new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
        g.setColor(this.stylerPie.getPlotBackgroundColor());
        g.fill(rect);
        if (this.stylerPie.isPlotBorderVisible()) {
            g.setColor(this.stylerPie.getPlotBorderColor());
            g.draw(rect);
        }
    }
}

