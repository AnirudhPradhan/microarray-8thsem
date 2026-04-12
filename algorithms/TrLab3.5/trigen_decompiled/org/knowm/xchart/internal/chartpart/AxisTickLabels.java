/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.HashMap;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Series_AxesChart;
import org.knowm.xchart.internal.chartpart.Axis;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.ChartPart;
import org.knowm.xchart.style.AxesChartStyler;

public class AxisTickLabels<ST extends AxesChartStyler, S extends Series>
implements ChartPart {
    private final Chart<AxesChartStyler, Series_AxesChart> chart;
    private Rectangle2D bounds;
    private final Axis.Direction direction;

    protected AxisTickLabels(Chart<AxesChartStyler, Series_AxesChart> chart, Axis.Direction direction) {
        this.chart = chart;
        this.direction = direction;
    }

    @Override
    public void paint(Graphics2D g) {
        g.setFont(this.chart.getStyler().getAxisTickLabelsFont());
        g.setColor(this.chart.getStyler().getAxisTickLabelsColor());
        if (this.direction == Axis.Direction.Y && this.chart.getStyler().isYAxisTicksVisible()) {
            double boundWidth;
            double xWidth = this.chart.getYAxis().getAxisTitle().getBounds().getWidth();
            double xOffset = this.chart.getYAxis().getAxisTitle().getBounds().getX() + xWidth;
            double yOffset = this.chart.getYAxis().getBounds().getY();
            double height = this.chart.getYAxis().getBounds().getHeight();
            double maxTickLabelWidth = 0.0;
            HashMap<Double, TextLayout> axisLabelTextLayouts = new HashMap<Double, TextLayout>();
            for (int i = 0; i < this.chart.getYAxis().getAxisTickCalculator().getTickLabels().size(); ++i) {
                String tickLabel = this.chart.getYAxis().getAxisTickCalculator().getTickLabels().get(i);
                double tickLocation = this.chart.getYAxis().getAxisTickCalculator().getTickLocations().get(i);
                double flippedTickLocation = yOffset + height - tickLocation;
                if (tickLabel == null || !(flippedTickLocation > yOffset) || !(flippedTickLocation < yOffset + height)) continue;
                FontRenderContext frc = g.getFontRenderContext();
                TextLayout axisLabelTextLayout = new TextLayout(tickLabel, this.chart.getStyler().getAxisTickLabelsFont(), frc);
                Rectangle2D tickLabelBounds = axisLabelTextLayout.getBounds();
                boundWidth = tickLabelBounds.getWidth();
                if (boundWidth > maxTickLabelWidth) {
                    maxTickLabelWidth = boundWidth;
                }
                axisLabelTextLayouts.put(tickLocation, axisLabelTextLayout);
            }
            for (Double tickLocation : axisLabelTextLayouts.keySet()) {
                double xPos;
                TextLayout axisLabelTextLayout = (TextLayout)axisLabelTextLayouts.get(tickLocation);
                Shape shape = axisLabelTextLayout.getOutline(null);
                Rectangle tickLabelBounds = shape.getBounds();
                double flippedTickLocation = yOffset + height - tickLocation;
                AffineTransform orig = g.getTransform();
                AffineTransform at = new AffineTransform();
                boundWidth = ((RectangularShape)tickLabelBounds).getWidth();
                switch (this.chart.getStyler().getYAxisLabelAlignment()) {
                    case Right: {
                        xPos = xOffset + maxTickLabelWidth - boundWidth;
                        break;
                    }
                    case Centre: {
                        xPos = xOffset + (maxTickLabelWidth - boundWidth) / 2.0;
                        break;
                    }
                    default: {
                        xPos = xOffset;
                    }
                }
                at.translate(xPos, flippedTickLocation + ((RectangularShape)tickLabelBounds).getHeight() / 2.0);
                g.transform(at);
                g.fill(shape);
                g.setTransform(orig);
            }
            this.bounds = new Rectangle2D.Double(xOffset, yOffset, maxTickLabelWidth, height);
        } else if (this.direction == Axis.Direction.X && this.chart.getStyler().isXAxisTicksVisible()) {
            double xOffset = this.chart.getXAxis().getBounds().getX();
            double yOffset = this.chart.getXAxis().getAxisTitle().getBounds().getY();
            double width = this.chart.getXAxis().getBounds().getWidth();
            double maxTickLabelHeight = 0.0;
            for (int i = 0; i < this.chart.getXAxis().getAxisTickCalculator().getTickLabels().size(); ++i) {
                double xPos;
                String tickLabel = this.chart.getXAxis().getAxisTickCalculator().getTickLabels().get(i);
                double tickLocation = this.chart.getXAxis().getAxisTickCalculator().getTickLocations().get(i);
                double shiftedTickLocation = xOffset + tickLocation;
                if (tickLabel == null || !(shiftedTickLocation > xOffset) || !(shiftedTickLocation < xOffset + width)) continue;
                FontRenderContext frc = g.getFontRenderContext();
                TextLayout textLayout = new TextLayout(tickLabel, this.chart.getStyler().getAxisTickLabelsFont(), frc);
                AffineTransform rot = AffineTransform.getRotateInstance(-1.0 * Math.toRadians(this.chart.getStyler().getXAxisLabelRotation()), 0.0, 0.0);
                Shape shape = textLayout.getOutline(rot);
                Rectangle2D tickLabelBounds = shape.getBounds2D();
                AffineTransform orig = g.getTransform();
                AffineTransform at = new AffineTransform();
                switch (this.chart.getStyler().getXAxisLabelAlignment()) {
                    case Left: {
                        xPos = shiftedTickLocation;
                        break;
                    }
                    case Right: {
                        xPos = shiftedTickLocation - tickLabelBounds.getWidth();
                        break;
                    }
                    default: {
                        xPos = shiftedTickLocation - tickLabelBounds.getWidth() / 2.0;
                    }
                }
                double shiftX = -1.0 * tickLabelBounds.getX() * Math.sin(Math.toRadians(this.chart.getStyler().getXAxisLabelRotation()));
                double shiftY = -1.0 * (tickLabelBounds.getY() + tickLabelBounds.getHeight());
                at.translate(xPos + shiftX, yOffset + shiftY);
                g.transform(at);
                g.fill(shape);
                g.setTransform(orig);
                if (!(tickLabelBounds.getHeight() > maxTickLabelHeight)) continue;
                maxTickLabelHeight = tickLabelBounds.getHeight();
            }
            this.bounds = new Rectangle2D.Double(xOffset, yOffset - maxTickLabelHeight, width, maxTickLabelHeight);
        } else {
            this.bounds = new Rectangle2D.Double();
        }
    }

    @Override
    public Rectangle2D getBounds() {
        return this.bounds;
    }
}

