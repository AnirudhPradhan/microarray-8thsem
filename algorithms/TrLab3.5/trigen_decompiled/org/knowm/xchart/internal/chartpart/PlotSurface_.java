/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.ChartPart;
import org.knowm.xchart.style.Styler;

public abstract class PlotSurface_<ST extends Styler, S extends Series>
implements ChartPart {
    protected final Chart<ST, S> chart;

    protected PlotSurface_(Chart<ST, S> chart) {
        this.chart = chart;
    }

    @Override
    public Rectangle2D getBounds() {
        return this.chart.getPlot().getBounds();
    }
}

