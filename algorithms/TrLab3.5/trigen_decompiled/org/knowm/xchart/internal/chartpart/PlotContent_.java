/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.ChartPart;
import org.knowm.xchart.style.AxesChartStyler;

public abstract class PlotContent_<ST extends AxesChartStyler, S extends Series>
implements ChartPart {
    protected final Chart<ST, S> chart;
    protected final Stroke errorBarStroke = new BasicStroke(1.0f, 0, 2);

    public abstract void doPaint(Graphics2D var1);

    protected PlotContent_(Chart<ST, S> chart) {
        this.chart = chart;
    }

    @Override
    public void paint(Graphics2D g) {
        Rectangle2D bounds = this.getBounds();
        if (bounds.getWidth() < 30.0) {
            return;
        }
        g.setClip(bounds.createIntersection(bounds));
        this.doPaint(g);
        g.setClip(null);
    }

    @Override
    public Rectangle2D getBounds() {
        return this.chart.getPlot().getBounds();
    }

    void closePath(Graphics2D g, Path2D.Double path, double previousX, Rectangle2D bounds, double yTopMargin) {
        if (path != null) {
            double yBottomOfArea = this.getBounds().getY() + this.getBounds().getHeight() - yTopMargin;
            path.lineTo(previousX, yBottomOfArea);
            path.closePath();
            g.fill(path);
        }
    }
}

