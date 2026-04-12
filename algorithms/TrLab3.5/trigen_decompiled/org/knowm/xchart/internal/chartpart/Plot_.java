/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.ChartPart;
import org.knowm.xchart.internal.chartpart.PlotContent_;
import org.knowm.xchart.internal.chartpart.PlotSurface_;
import org.knowm.xchart.style.Styler;

public class Plot_<ST extends Styler, S extends Series>
implements ChartPart {
    protected final Chart<ST, S> chart;
    protected Rectangle2D bounds;
    protected PlotSurface_ plotSurface;
    protected PlotContent_ plotContent;

    public Plot_(Chart<ST, S> chart) {
        this.chart = chart;
    }

    @Override
    public void paint(Graphics2D g) {
        this.plotSurface.paint(g);
        if (this.chart.getSeriesMap().isEmpty()) {
            return;
        }
        this.plotContent.paint(g);
    }

    @Override
    public Rectangle2D getBounds() {
        return this.bounds;
    }
}

