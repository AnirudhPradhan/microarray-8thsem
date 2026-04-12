/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.ChartPart;
import org.knowm.xchart.style.Styler;

public class ChartTitle
implements ChartPart {
    private final Chart<?, ?> chart;
    private Rectangle2D bounds;

    public ChartTitle(Chart<?, ?> chart) {
        this.chart = chart;
    }

    @Override
    public void paint(Graphics2D g) {
        g.setFont(((Styler)this.chart.getStyler()).getChartTitleFont());
        if (!((Styler)this.chart.getStyler()).isChartTitleVisible() || this.chart.getTitle().length() == 0) {
            return;
        }
        FontRenderContext frc = g.getFontRenderContext();
        TextLayout textLayout = new TextLayout(this.chart.getTitle(), ((Styler)this.chart.getStyler()).getChartTitleFont(), frc);
        Rectangle2D textBounds = textLayout.getBounds();
        double xOffset = this.chart.getPlot().getBounds().getX();
        double yOffset = ((Styler)this.chart.getStyler()).getChartPadding();
        if (((Styler)this.chart.getStyler()).isChartTitleBoxVisible()) {
            double chartTitleBoxWidth = this.chart.getPlot().getBounds().getWidth();
            double chartTitleBoxHeight = textBounds.getHeight() + (double)(2 * ((Styler)this.chart.getStyler()).getChartTitlePadding());
            g.setStroke(new BasicStroke(1.0f, 0, 2));
            Rectangle2D.Double rect = new Rectangle2D.Double(xOffset, yOffset, chartTitleBoxWidth, chartTitleBoxHeight);
            g.setColor(((Styler)this.chart.getStyler()).getChartTitleBoxBackgroundColor());
            g.fill(rect);
            g.setColor(((Styler)this.chart.getStyler()).getChartTitleBoxBorderColor());
            g.draw(rect);
        }
        xOffset = this.chart.getPlot().getBounds().getX() + (this.chart.getPlot().getBounds().getWidth() - textBounds.getWidth()) / 2.0;
        yOffset = (double)((Styler)this.chart.getStyler()).getChartPadding() + textBounds.getHeight() + (double)((Styler)this.chart.getStyler()).getChartTitlePadding();
        g.setColor(((Styler)this.chart.getStyler()).getChartFontColor());
        Shape shape = textLayout.getOutline(null);
        AffineTransform orig = g.getTransform();
        AffineTransform at = new AffineTransform();
        at.translate(xOffset, yOffset);
        g.transform(at);
        g.fill(shape);
        g.setTransform(orig);
        double width = (double)(2 * ((Styler)this.chart.getStyler()).getChartTitlePadding()) + textBounds.getWidth();
        double height = (double)(2 * ((Styler)this.chart.getStyler()).getChartTitlePadding()) + textBounds.getHeight();
        this.bounds = new Rectangle2D.Double(xOffset - (double)((Styler)this.chart.getStyler()).getChartTitlePadding(), yOffset - textBounds.getHeight() - (double)((Styler)this.chart.getStyler()).getChartTitlePadding(), width, height);
    }

    private Rectangle2D getBoundsHint() {
        if (((Styler)this.chart.getStyler()).isChartTitleVisible() && this.chart.getTitle().length() > 0) {
            TextLayout textLayout = new TextLayout(this.chart.getTitle(), ((Styler)this.chart.getStyler()).getChartTitleFont(), new FontRenderContext(null, true, false));
            Rectangle2D rectangle = textLayout.getBounds();
            double width = (double)(2 * ((Styler)this.chart.getStyler()).getChartTitlePadding()) + rectangle.getWidth();
            double height = (double)(2 * ((Styler)this.chart.getStyler()).getChartTitlePadding()) + rectangle.getHeight();
            return new Rectangle2D.Double(Double.NaN, Double.NaN, width, height);
        }
        return new Rectangle2D.Double();
    }

    @Override
    public Rectangle2D getBounds() {
        if (this.bounds == null) {
            this.bounds = this.getBoundsHint();
        }
        return this.bounds;
    }
}

