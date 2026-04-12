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
import java.util.LinkedHashMap;
import java.util.Map;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.ChartPart;
import org.knowm.xchart.internal.chartpart.RenderableSeries;
import org.knowm.xchart.style.Styler;

public abstract class Legend_<ST extends Styler, S extends Series>
implements ChartPart {
    protected static final int LEGEND_MARGIN = 6;
    protected static final int BOX_SIZE = 20;
    protected static final int MULTI_LINE_SPACE = 3;
    protected Chart<ST, S> chart;
    protected Rectangle2D bounds = null;
    protected double xOffset = 0.0;
    protected double yOffset = 0.0;

    public abstract double getSeriesLegendRenderGraphicHeight(Series var1);

    public abstract void doPaint(Graphics2D var1);

    public Legend_(Chart<ST, S> chart) {
        this.chart = chart;
    }

    @Override
    public void paint(Graphics2D g) {
        if (!((Styler)this.chart.getStyler()).isLegendVisible()) {
            return;
        }
        if (this.chart.getSeriesMap().isEmpty()) {
            return;
        }
        if (this.chart.getPlot().getBounds().getWidth() < 30.0) {
            return;
        }
        if (this.bounds == null) {
            this.bounds = this.getBoundsHint();
        }
        switch (((Styler)this.chart.getStyler()).getLegendPosition()) {
            case OutsideE: {
                this.xOffset = (double)this.chart.getWidth() - this.bounds.getWidth() - (double)((Styler)this.chart.getStyler()).getChartPadding();
                this.yOffset = this.chart.getPlot().getBounds().getY() + (this.chart.getPlot().getBounds().getHeight() - this.bounds.getHeight()) / 2.0;
                break;
            }
            case InsideNW: {
                this.xOffset = this.chart.getPlot().getBounds().getX() + 6.0;
                this.yOffset = this.chart.getPlot().getBounds().getY() + 6.0;
                break;
            }
            case InsideNE: {
                this.xOffset = this.chart.getPlot().getBounds().getX() + this.chart.getPlot().getBounds().getWidth() - this.bounds.getWidth() - 6.0;
                this.yOffset = this.chart.getPlot().getBounds().getY() + 6.0;
                break;
            }
            case InsideSE: {
                this.xOffset = this.chart.getPlot().getBounds().getX() + this.chart.getPlot().getBounds().getWidth() - this.bounds.getWidth() - 6.0;
                this.yOffset = this.chart.getPlot().getBounds().getY() + this.chart.getPlot().getBounds().getHeight() - this.bounds.getHeight() - 6.0;
                break;
            }
            case InsideSW: {
                this.xOffset = this.chart.getPlot().getBounds().getX() + 6.0;
                this.yOffset = this.chart.getPlot().getBounds().getY() + this.chart.getPlot().getBounds().getHeight() - this.bounds.getHeight() - 6.0;
                break;
            }
            case InsideN: {
                this.xOffset = this.chart.getPlot().getBounds().getX() + (this.chart.getPlot().getBounds().getWidth() - this.bounds.getWidth()) / 2.0 + 6.0;
                this.yOffset = this.chart.getPlot().getBounds().getY() + 6.0;
                break;
            }
        }
        Rectangle2D.Double rect = new Rectangle2D.Double(this.xOffset, this.yOffset, this.bounds.getWidth(), this.bounds.getHeight());
        g.setColor(((Styler)this.chart.getStyler()).getLegendBackgroundColor());
        g.fill(rect);
        g.setStroke(new BasicStroke(1.0f, 0, 2, 10.0f, new float[]{3.0f, 0.0f}, 0.0f));
        g.setColor(((Styler)this.chart.getStyler()).getLegendBorderColor());
        g.draw(rect);
        this.doPaint(g);
        this.bounds = new Rectangle2D.Double(this.xOffset, this.yOffset, this.bounds.getWidth(), this.bounds.getHeight());
    }

    public Rectangle2D getBoundsHint() {
        if (!((Styler)this.chart.getStyler()).isLegendVisible()) {
            return new Rectangle2D.Double();
        }
        boolean containsBox = false;
        double legendTextContentMaxWidth = 0.0;
        double legendContentHeight = 0.0;
        Map<String, S> map = this.chart.getSeriesMap();
        for (Series series : map.values()) {
            if (!series.isShowInLegend()) continue;
            Map<String, Rectangle2D> seriesTextBounds = this.getSeriesTextBounds(series);
            double legendEntryHeight = 0.0;
            for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {
                legendEntryHeight += entry.getValue().getHeight() + 3.0;
                legendTextContentMaxWidth = Math.max(legendTextContentMaxWidth, entry.getValue().getWidth());
            }
            legendEntryHeight -= 3.0;
            legendEntryHeight = Math.max(legendEntryHeight, this.getSeriesLegendRenderGraphicHeight(series));
            legendContentHeight += legendEntryHeight + (double)((Styler)this.chart.getStyler()).getLegendPadding();
            if (series.getLegendRenderType() != RenderableSeries.LegendRenderType.Box) continue;
            containsBox = true;
        }
        double legendContentWidth = 0.0;
        legendContentWidth = !containsBox ? (double)(((Styler)this.chart.getStyler()).getLegendSeriesLineLength() + ((Styler)this.chart.getStyler()).getLegendPadding()) + legendTextContentMaxWidth : (double)(20 + ((Styler)this.chart.getStyler()).getLegendPadding()) + legendTextContentMaxWidth;
        double width = legendContentWidth + (double)(2 * ((Styler)this.chart.getStyler()).getLegendPadding());
        double height = legendContentHeight + (double)(1 * ((Styler)this.chart.getStyler()).getLegendPadding());
        return new Rectangle2D.Double(Double.NaN, Double.NaN, width, height);
    }

    protected Map<String, Rectangle2D> getSeriesTextBounds(Series series) {
        String[] lines = series.getName().split("\\n");
        LinkedHashMap<String, Rectangle2D> seriesTextBounds = new LinkedHashMap<String, Rectangle2D>(lines.length);
        for (String line : lines) {
            TextLayout textLayout = new TextLayout(line, ((Styler)this.chart.getStyler()).getLegendFont(), new FontRenderContext(null, true, false));
            Shape shape = textLayout.getOutline(null);
            Rectangle2D bounds = shape.getBounds2D();
            seriesTextBounds.put(line, bounds);
        }
        return seriesTextBounds;
    }

    float getLegendEntryHeight(Map<String, Rectangle2D> seriesTextBounds, int markerSize) {
        float legendEntryHeight = 0.0f;
        for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {
            legendEntryHeight = (float)((double)legendEntryHeight + (entry.getValue().getHeight() + 3.0));
        }
        legendEntryHeight -= 3.0f;
        legendEntryHeight = Math.max(legendEntryHeight, (float)markerSize);
        return legendEntryHeight;
    }

    void paintSeriesText(Graphics2D g, Map<String, Rectangle2D> seriesTextBounds, int markerSize, double x, double starty) {
        g.setColor(((Styler)this.chart.getStyler()).getChartFontColor());
        g.setFont(((Styler)this.chart.getStyler()).getLegendFont());
        double multiLineOffset = 0.0;
        for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {
            double height = entry.getValue().getHeight();
            double centerOffsetY = (Math.max((double)markerSize, height) - height) / 2.0;
            FontRenderContext frc = g.getFontRenderContext();
            TextLayout tl = new TextLayout(entry.getKey(), ((Styler)this.chart.getStyler()).getLegendFont(), frc);
            Shape shape = tl.getOutline(null);
            AffineTransform orig = g.getTransform();
            AffineTransform at = new AffineTransform();
            at.translate(x, starty + height + centerOffsetY + multiLineOffset);
            g.transform(at);
            g.fill(shape);
            g.setTransform(orig);
            multiLineOffset += height + 3.0;
        }
    }
}

