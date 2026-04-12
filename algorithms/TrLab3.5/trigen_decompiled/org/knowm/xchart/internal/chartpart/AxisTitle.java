/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Series_AxesChart;
import org.knowm.xchart.internal.chartpart.Axis;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.ChartPart;
import org.knowm.xchart.style.AxesChartStyler;

public class AxisTitle<ST extends AxesChartStyler, S extends Series>
implements ChartPart {
    private final Chart<AxesChartStyler, Series_AxesChart> chart;
    private Rectangle2D bounds;
    private final Axis.Direction direction;

    protected AxisTitle(Chart<AxesChartStyler, Series_AxesChart> chart, Axis.Direction direction) {
        this.chart = chart;
        this.direction = direction;
    }

    @Override
    public void paint(Graphics2D g) {
        this.bounds = new Rectangle2D.Double();
        g.setColor(this.chart.getStyler().getChartFontColor());
        g.setFont(this.chart.getStyler().getAxisTitleFont());
        if (this.direction == Axis.Direction.Y) {
            if (this.chart.getyYAxisTitle() != null && !this.chart.getyYAxisTitle().trim().equalsIgnoreCase("") && this.chart.getStyler().isYAxisTitleVisible()) {
                FontRenderContext frc = g.getFontRenderContext();
                TextLayout nonRotatedTextLayout = new TextLayout(this.chart.getyYAxisTitle(), this.chart.getStyler().getAxisTitleFont(), frc);
                Rectangle2D nonRotatedRectangle = nonRotatedTextLayout.getBounds();
                int xOffset = (int)(this.chart.getYAxis().getBounds().getX() + nonRotatedRectangle.getHeight());
                int yOffset = (int)((this.chart.getYAxis().getBounds().getHeight() + nonRotatedRectangle.getWidth()) / 2.0 + this.chart.getYAxis().getBounds().getY());
                AffineTransform rot = AffineTransform.getRotateInstance(-1.5707963267948966, 0.0, 0.0);
                Shape shape = nonRotatedTextLayout.getOutline(rot);
                AffineTransform orig = g.getTransform();
                AffineTransform at = new AffineTransform();
                at.translate(xOffset, yOffset);
                g.transform(at);
                g.fill(shape);
                g.setTransform(orig);
                this.bounds = new Rectangle2D.Double((double)xOffset - nonRotatedRectangle.getHeight(), (double)yOffset - nonRotatedRectangle.getWidth(), nonRotatedRectangle.getHeight() + (double)this.chart.getStyler().getAxisTitlePadding(), nonRotatedRectangle.getWidth());
            } else {
                this.bounds = new Rectangle2D.Double(this.chart.getYAxis().getBounds().getX(), this.chart.getYAxis().getBounds().getY(), 0.0, this.chart.getYAxis().getBounds().getHeight());
            }
        } else if (this.chart.getXAxisTitle() != null && !this.chart.getXAxisTitle().trim().equalsIgnoreCase("") && this.chart.getStyler().isXAxisTitleVisible()) {
            FontRenderContext frc = g.getFontRenderContext();
            TextLayout textLayout = new TextLayout(this.chart.getXAxisTitle(), this.chart.getStyler().getAxisTitleFont(), frc);
            Rectangle2D rectangle = textLayout.getBounds();
            double xOffset = this.chart.getXAxis().getBounds().getX() + (this.chart.getXAxis().getBounds().getWidth() - rectangle.getWidth()) / 2.0;
            double yOffset = this.chart.getXAxis().getBounds().getY() + this.chart.getXAxis().getBounds().getHeight() - rectangle.getHeight();
            Shape shape = textLayout.getOutline(null);
            AffineTransform orig = g.getTransform();
            AffineTransform at = new AffineTransform();
            at.translate((float)xOffset, (float)(yOffset - rectangle.getY()));
            g.transform(at);
            g.fill(shape);
            g.setTransform(orig);
            this.bounds = new Rectangle2D.Double(xOffset, yOffset - (double)this.chart.getStyler().getAxisTitlePadding(), rectangle.getWidth(), rectangle.getHeight() + (double)this.chart.getStyler().getAxisTitlePadding());
        } else {
            this.bounds = new Rectangle2D.Double(this.chart.getXAxis().getBounds().getX(), this.chart.getXAxis().getBounds().getY() + this.chart.getXAxis().getBounds().getHeight(), this.chart.getXAxis().getBounds().getWidth(), 0.0);
        }
    }

    @Override
    public Rectangle2D getBounds() {
        return this.bounds;
    }
}

