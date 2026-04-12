/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Series_AxesChart;
import org.knowm.xchart.internal.chartpart.Axis;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.ChartPart;
import org.knowm.xchart.style.AxesChartStyler;

public class AxisTickMarks<ST extends AxesChartStyler, S extends Series>
implements ChartPart {
    private final Chart<AxesChartStyler, Series_AxesChart> chart;
    private Rectangle2D bounds;
    private final Axis.Direction direction;

    protected AxisTickMarks(Chart<AxesChartStyler, Series_AxesChart> chart, Axis.Direction direction) {
        this.chart = chart;
        this.direction = direction;
    }

    @Override
    public void paint(Graphics2D g) {
        g.setColor(this.chart.getStyler().getAxisTickMarksColor());
        g.setStroke(this.chart.getStyler().getAxisTickMarksStroke());
        if (this.direction == Axis.Direction.Y && this.chart.getStyler().isYAxisTicksVisible()) {
            double xOffset = this.chart.getYAxis().getAxisTick().getAxisTickLabels().getBounds().getX() + this.chart.getYAxis().getAxisTick().getAxisTickLabels().getBounds().getWidth() + (double)this.chart.getStyler().getAxisTickPadding();
            double yOffset = this.chart.getYAxis().getBounds().getY();
            this.bounds = new Rectangle2D.Double(xOffset, yOffset, this.chart.getStyler().getAxisTickMarkLength(), this.chart.getYAxis().getBounds().getHeight());
            if (this.chart.getStyler().isAxisTicksMarksVisible()) {
                for (int i = 0; i < this.chart.getYAxis().getAxisTickCalculator().getTickLabels().size(); ++i) {
                    double tickLocation = this.chart.getYAxis().getAxisTickCalculator().getTickLocations().get(i);
                    double flippedTickLocation = yOffset + this.chart.getYAxis().getBounds().getHeight() - tickLocation;
                    if (!(flippedTickLocation > this.bounds.getY()) || !(flippedTickLocation < this.bounds.getY() + this.bounds.getHeight())) continue;
                    Line2D.Double line = new Line2D.Double(xOffset, flippedTickLocation, xOffset + (double)this.chart.getStyler().getAxisTickMarkLength(), flippedTickLocation);
                    g.draw(line);
                }
            }
            if (this.chart.getStyler().isAxisTicksLineVisible()) {
                Line2D.Double line = new Line2D.Double(xOffset + (double)this.chart.getStyler().getAxisTickMarkLength(), yOffset, xOffset + (double)this.chart.getStyler().getAxisTickMarkLength(), yOffset + this.chart.getYAxis().getBounds().getHeight());
                g.draw(line);
            }
        } else if (this.direction == Axis.Direction.X && this.chart.getStyler().isXAxisTicksVisible()) {
            double xOffset = this.chart.getXAxis().getBounds().getX();
            double yOffset = this.chart.getXAxis().getAxisTick().getAxisTickLabels().getBounds().getY() - (double)this.chart.getStyler().getAxisTickPadding();
            this.bounds = new Rectangle2D.Double(xOffset, yOffset - (double)this.chart.getStyler().getAxisTickMarkLength(), this.chart.getXAxis().getBounds().getWidth(), this.chart.getStyler().getAxisTickMarkLength());
            if (this.chart.getStyler().isAxisTicksMarksVisible()) {
                for (int i = 0; i < this.chart.getXAxis().getAxisTickCalculator().getTickLabels().size(); ++i) {
                    double tickLocation = this.chart.getXAxis().getAxisTickCalculator().getTickLocations().get(i);
                    double shiftedTickLocation = xOffset + tickLocation;
                    if (!(shiftedTickLocation > this.bounds.getX()) || !(shiftedTickLocation < this.bounds.getX() + this.bounds.getWidth())) continue;
                    Line2D.Double line = new Line2D.Double(shiftedTickLocation, yOffset, xOffset + tickLocation, yOffset - (double)this.chart.getStyler().getAxisTickMarkLength());
                    g.draw(line);
                }
            }
            if (this.chart.getStyler().isAxisTicksLineVisible()) {
                g.setStroke(this.chart.getStyler().getAxisTickMarksStroke());
                g.drawLine((int)xOffset, (int)(yOffset - (double)this.chart.getStyler().getAxisTickMarkLength()), (int)(xOffset + this.chart.getXAxis().getBounds().getWidth()), (int)(yOffset - (double)this.chart.getStyler().getAxisTickMarkLength()));
            }
        } else {
            this.bounds = new Rectangle2D.Double();
        }
    }

    @Override
    public Rectangle2D getBounds() {
        return this.bounds;
    }
}

